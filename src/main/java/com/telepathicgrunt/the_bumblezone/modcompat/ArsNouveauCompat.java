package com.telepathicgrunt.the_bumblezone.modcompat;

import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import com.hollingsworth.arsnouveau.common.spell.method.MethodOrbit;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.spell.method.MethodUnderfoot;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.function.Consumer;

public class ArsNouveauCompat {
	private static final ResourceLocation SPELL_PROJ_RL = new ResourceLocation("ars_nouveau", "spell_proj");
	private static final ResourceLocation SPELL_FOLLOW_PROJ_RL = new ResourceLocation("ars_nouveau", "follow_proj");

	protected static final Set<AbstractCastMethod> ALLOWED_CAST_METHODS = Sets.newHashSet(
		MethodProjectile.INSTANCE,
		MethodOrbit.INSTANCE,
		MethodTouch.INSTANCE,
		MethodUnderfoot.INSTANCE
	);

	public static void setupCompat() {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		Consumer<EffectResolveEvent.Post> handler = ArsNouveauCompat::isArsSpellProjectile;
		forgeBus.addListener(handler);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.arsNouveauPresent = true;
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
					EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), player.getLevel().getBlockState(blockHitResult.getBlockPos()), stack);
				}

				return;
			}

			if (event.spell.getCastMethod() == MethodProjectile.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
				return;
			}
			else if (event.spell.getCastMethod() == MethodOrbit.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_FOLLOW_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
				return;
			}
			else if (ModChecker.arsElementalPresent) {
				if (ArsElementalCompat.isArsElementalSpellDisallowed(event)) {
					return;
				}
			}

			if(event.rayTraceResult instanceof BlockHitResult) {
				EntityTeleportationHookup.runTeleportProjectileImpact(event.rayTraceResult.getLocation(), event.shooter, null);
			}
			else if (event.rayTraceResult instanceof EntityHitResult entityHitResult) {
				EntityTeleportationHookup.runEntityHitCheck(entityHitResult, event.shooter, event.world, null);
			}
		}
	}

	public static boolean isArsSpellProjectile(Projectile projectile) {
		ResourceLocation projectileRL = ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType());
		if (ModChecker.arsElementalPresent) {
			if (ArsElementalCompat.isArsElementalSpellProjectile(projectileRL)) {
				return true;
			}
		}

		return projectileRL.equals(SPELL_PROJ_RL) || projectileRL.equals(SPELL_FOLLOW_PROJ_RL);
	}

	public static boolean isArsSpellBook(ItemStack stack) {
		return stack.getItem() instanceof SpellBook;
	}
}
