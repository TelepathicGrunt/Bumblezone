package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public class ArsNouveauCompat implements ModCompat {
//	private static final ResourceLocation SPELL_PROJ_RL = ResourceLocation.fromNamespaceAndPath("ars_nouveau", "spell_proj");
//	private static final ResourceLocation SPELL_FOLLOW_PROJ_RL = ResourceLocation.fromNamespaceAndPath("ars_nouveau", "follow_proj");
//
//	protected static final Set<AbstractCastMethod> ALLOWED_CAST_METHODS = Sets.newHashSet(
//		MethodProjectile.INSTANCE,
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
