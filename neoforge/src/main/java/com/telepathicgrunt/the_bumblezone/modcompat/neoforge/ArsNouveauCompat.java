package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.common.entity.AnimBlockSummon;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectAnimate;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBreak;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectConjureWater;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectExchange;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectGlide;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectGravity;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIntangible;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLaunch;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLeap;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectPhantomBlock;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectPlaceBlock;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSlowfall;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectWall;
import com.hollingsworth.arsnouveau.common.spell.method.MethodOrbit;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.spell.method.MethodUnderfoot;
import com.telepathicgrunt.the_bumblezone.blocks.HeavyAir;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

public class ArsNouveauCompat implements ModCompat {
//	private static final ResourceLocation SPELL_PROJ_RL = new ResourceLocation("ars_nouveau", "spell_proj");
//	private static final ResourceLocation SPELL_FOLLOW_PROJ_RL = new ResourceLocation("ars_nouveau", "follow_proj");
//
//	protected static final Set<AbstractCastMethod> ALLOWED_CAST_METHODS = Sets.newHashSet(
//		MethodProjectile.INSTANCE,
//		MethodOrbit.INSTANCE,
//		MethodTouch.INSTANCE,
//		MethodUnderfoot.INSTANCE
//	);
//
//	protected static final Set<AbstractEffect> DISALLOWED_HEAVY_AIR_EFFECTS = Sets.newHashSet(
//		EffectGlide.INSTANCE,
//		EffectSlowfall.INSTANCE,
//		EffectLaunch.INSTANCE,
//		EffectLeap.INSTANCE
//	);
//
//	protected static final Set<AbstractEffect> DISALLOWED_INFINITY_AND_ESSENCE_EFFECTS = Sets.newHashSet(
//		EffectExchange.INSTANCE,
//		EffectAnimate.INSTANCE,
//		EffectGravity.INSTANCE,
//		EffectBreak.INSTANCE,
//		EffectIntangible.INSTANCE,
//		EffectConjureWater.INSTANCE,
//		EffectPlaceBlock.INSTANCE,
//		EffectPhantomBlock.INSTANCE,
//		EffectWall.INSTANCE
//	);
//
//	public ArsNouveauCompat() {
//		IEventBus eventBus = NeoForge.EVENT_BUS;
//		Consumer<EffectResolveEvent.Post> projectHandler = ArsNouveauCompat::handleArsSpellProjectile;
//		eventBus.addListener(projectHandler);
//
//		Consumer<EffectResolveEvent.Pre> heavyAirHandler = ArsNouveauCompat::handleArsSpellHeavyAir;
//		eventBus.addListener(heavyAirHandler);
//
//		Consumer<EffectResolveEvent.Pre> essenceAndInfinityHandler = ArsNouveauCompat::handleArsSpellEssenceAndInfinity;
//		eventBus.addListener(essenceAndInfinityHandler);
//
//		// Keep at end so it is only set to true if no exceptions was thrown during setup
//		ModChecker.arsNouveauPresent = true;
//	}
//
//	@Override
//	public EnumSet<Type> compatTypes() {
//		return EnumSet.of(Type.PROJECTILE_IMPACT_HANDLED, Type.RIGHT_CLICKED_HIVE_HANDLED);
//	}
//
//	private static void handleArsSpellEssenceAndInfinity(EffectResolveEvent.Pre event) {
//		boolean isDisallowedEffect = DISALLOWED_INFINITY_AND_ESSENCE_EFFECTS.contains(event.resolveEffect);
//
//		if (isDisallowedEffect && event.rayTraceResult instanceof BlockHitResult blockHitResult) {
//			BlockState hitState = event.world.getBlockState(blockHitResult.getBlockPos());
//			if (hitState.is(BzTags.ESSENCE_BLOCKS) || hitState.is(BzBlocks.INFINITY_BARRIER.get())) {
//				if (event.shooter instanceof ServerPlayer serverPlayer) {
//					serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_spell")
//							.withStyle(ChatFormatting.ITALIC)
//							.withStyle(ChatFormatting.RED), true);
//				}
//				event.setCanceled(true);
//			}
//		}
//	}
//
//	private static void handleArsSpellHeavyAir(EffectResolveEvent.Pre event) {
//		if (DISALLOWED_HEAVY_AIR_EFFECTS.contains(event.resolveEffect)) {
//			if (event.rayTraceResult instanceof EntityHitResult entityHitResult) {
//				if (event.shooter instanceof Player player && HeavyAir.isInHeavyAir(entityHitResult.getEntity().level(), entityHitResult.getEntity().getBoundingBox())) {
//					if (player instanceof ServerPlayer serverPlayer) {
//						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_spell")
//								.withStyle(ChatFormatting.ITALIC)
//								.withStyle(ChatFormatting.RED), true);
//					}
//					event.setCanceled(true);
//				}
//			}
//		}
//	}
//
//	@SuppressWarnings("ConstantConditions")
//	private static void handleArsSpellProjectile(EffectResolveEvent.Post event) {
//		if (event.shooter instanceof Player player &&
//			event.resolveEffect == EffectBlink.INSTANCE &&
//			ALLOWED_CAST_METHODS.contains(event.spell.getCastMethod()))
//		{
//			if (event.spell.getCastMethod() == MethodTouch.INSTANCE || event.spell.getCastMethod() == MethodUnderfoot.INSTANCE) {
//				ItemStack stack = player.getMainHandItem();
//				if (event.rayTraceResult instanceof BlockHitResult &&
//					stack.is(BzTags.TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE) ||
//					(stack.is(BzTags.TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE_CROUCHING) && player.isShiftKeyDown()))
//				{
//					BlockHitResult blockHitResult = (BlockHitResult) event.rayTraceResult;
//					EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), stack);
//				}
//
//				return;
//			}
//
//			if (event.spell.getCastMethod() == MethodProjectile.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
//				return;
//			}
//			else if (event.spell.getCastMethod() == MethodOrbit.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_FOLLOW_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
//				return;
//			}
//			else if (ModChecker.arsElementalPresent) {
//				if (ArsElementalCompat.isArsElementalCasting(event)) {
//					return;
//				}
//			}
//
//
//			if(event.rayTraceResult instanceof BlockHitResult) {
//				EntityTeleportationHookup.runTeleportProjectileImpact(event.rayTraceResult, event.shooter, null);
//			}
//			else if (event.rayTraceResult instanceof EntityHitResult entityHitResult) {
//				if (ArsNouveauCompat.isArsWalkingBlock(entityHitResult.getEntity())) {
//					if (!ArsNouveauCompat.isArsWalkingBlockAValidBeeHive(entityHitResult.getEntity())) {
//						return;
//					}
//				}
//
//				EntityTeleportationHookup.runEntityHitCheck(entityHitResult, event.shooter, null);
//			}
//		}
//	}
//
//	public InteractionResult isProjectileTeleportHandled(HitResult hitResult, Entity owner, Projectile projectile) {
//		ResourceLocation projectileRL = ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType());
//		if (projectileRL != null && (projectileRL.equals(SPELL_PROJ_RL) || projectileRL.equals(SPELL_FOLLOW_PROJ_RL))) {
//			return InteractionResult.FAIL;
//		}
//
//		if (hitResult instanceof EntityHitResult entityHitResult && ArsNouveauCompat.isArsWalkingBlock(entityHitResult.getEntity())) {
//			if (!ArsNouveauCompat.isArsWalkingBlockAValidBeeHive(entityHitResult.getEntity())) {
//				return InteractionResult.FAIL;
//			}
//		}
//
//		return InteractionResult.PASS;
//	}
//
//	@Override
//	public boolean isRightClickTeleportHandled(Entity owner, ItemStack itemStack) {
//		return isArsSpellBook(itemStack);
//	}
//
//	public static boolean isArsSpellBook(ItemStack stack) {
//		return stack.getItem() instanceof SpellBook;
//	}
//
//	public static boolean isArsWalkingBlock(Entity entity) {
//		return entity instanceof AnimBlockSummon ;
//	}
//
//	public static boolean isArsWalkingBlockAValidBeeHive(Entity entity) {
//		if (entity instanceof AnimBlockSummon animBlockSummon) {
//			return EntityTeleportationBackend.isValidBeeHive(animBlockSummon.blockState);
//		}
//
//		return false;
//	}
}
