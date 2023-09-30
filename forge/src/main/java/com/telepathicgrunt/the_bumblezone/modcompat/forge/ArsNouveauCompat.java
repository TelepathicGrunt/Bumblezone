package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.common.entity.AnimBlockSummon;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import com.hollingsworth.arsnouveau.common.spell.method.MethodOrbit;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.spell.method.MethodUnderfoot;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

public class ArsNouveauCompat implements ModCompat {
	private static final ResourceLocation SPELL_PROJ_RL = new ResourceLocation("ars_nouveau", "spell_proj");
	private static final ResourceLocation SPELL_FOLLOW_PROJ_RL = new ResourceLocation("ars_nouveau", "follow_proj");

	protected static final Set<AbstractCastMethod> ALLOWED_CAST_METHODS = Sets.newHashSet(
		MethodProjectile.INSTANCE,
		MethodOrbit.INSTANCE,
		MethodTouch.INSTANCE,
		MethodUnderfoot.INSTANCE
	);

	public ArsNouveauCompat() {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		Consumer<EffectResolveEvent.Post> handler = ArsNouveauCompat::isArsSpellProjectile;
		forgeBus.addListener(handler);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.arsNouveauPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.PROJECTILE_IMPACT_HANDLED, Type.RIGHT_CLICKED_HIVE_HANDLED);
	}

	private static void isArsSpellProjectile(EffectResolveEvent.Post event) {
		if (event.shooter instanceof Player player &&
			event.resolveEffect == EffectBlink.INSTANCE &&
			ALLOWED_CAST_METHODS.contains(event.spell.getCastMethod()))
		{
			if (event.spell.getCastMethod() == MethodTouch.INSTANCE || event.spell.getCastMethod() == MethodUnderfoot.INSTANCE) {
				ItemStack stack = player.getMainHandItem();
				if (event.rayTraceResult instanceof BlockHitResult &&
					stack.is(BzTags.TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE) ||
					(stack.is(BzTags.TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE_CROUCHING) && player.isShiftKeyDown()))
				{
					BlockHitResult blockHitResult = (BlockHitResult) event.rayTraceResult;
					EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), stack);
				}

				return;
			}

			if (event.spell.getCastMethod() == MethodProjectile.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
				return;
			}
			else if (event.spell.getCastMethod() == MethodOrbit.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_FOLLOW_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
				return;
			}
//			else if (ModChecker.arsElementalPresent) {
//				if (ArsElementalCompat.isArsElementalSpellDisallowed(event)) {
//					return;
//				}
//			}


			if(event.rayTraceResult instanceof BlockHitResult) {
				EntityTeleportationHookup.runTeleportProjectileImpact(event.rayTraceResult, event.shooter, null);
			}
			else if (event.rayTraceResult instanceof EntityHitResult entityHitResult) {
				if (ArsNouveauCompat.isArsWalkingBlock(entityHitResult.getEntity())) {
					if (!ArsNouveauCompat.isArsWalkingBlockAValidBeeHive(entityHitResult.getEntity())) {
						return;
					}
				}

				EntityTeleportationHookup.runEntityHitCheck(entityHitResult, event.shooter, null);
			}
		}
	}

	public InteractionResult isProjectileTeleportHandled(HitResult hitResult, Entity owner, Projectile projectile) {
		ResourceLocation projectileRL = ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType());
		boolean isArsProjectile = projectileRL.equals(SPELL_PROJ_RL) || projectileRL.equals(SPELL_FOLLOW_PROJ_RL);
//		if (ModChecker.arsElementalPresent) {
//			if (ArsElementalCompat.isArsElementalSpellProjectile(projectileRL)) {
//				return true;
//			}
//		}

		if (isArsProjectile) {
			return InteractionResult.FAIL;
		}

		if (hitResult instanceof EntityHitResult entityHitResult && ArsNouveauCompat.isArsWalkingBlock(entityHitResult.getEntity())) {
			if (!ArsNouveauCompat.isArsWalkingBlockAValidBeeHive(entityHitResult.getEntity())) {
				return InteractionResult.FAIL;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean isRightClickTeleportHandled(Entity owner, ItemStack itemStack) {
		return isArsSpellBook(itemStack);
	}

	public static boolean isArsSpellBook(ItemStack stack) {
		return stack.getItem() instanceof SpellBook;
	}

	public static boolean isArsWalkingBlock(Entity entity) {
		return entity instanceof AnimBlockSummon ;
	}

	public static boolean isArsWalkingBlockAValidBeeHive(Entity entity) {
		if (entity instanceof AnimBlockSummon animBlockSummon) {
			return EntityTeleportationBackend.isValidBeeHive(animBlockSummon.blockState);
		}

		return false;
	}
}
