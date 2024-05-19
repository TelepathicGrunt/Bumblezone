package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public class ArsElementalCompat implements ModCompat {
//	private static final ResourceLocation SPELL_HOMING_PROJ_RL = new ResourceLocation("ars_nouveau", "homing_spell_proj");
//	private static final ResourceLocation SPELL_CURVED_PROJ_RL = new ResourceLocation("ars_nouveau", "arcing_spell_proj");
//
//	private static final Set<AbstractCastMethod> ALLOWED_ELEMENTAL_CAST_METHODS = Sets.newHashSet(
//			MethodHomingProjectile.INSTANCE,
//			MethodArcProjectile.INSTANCE
//	);
//
//	protected static final Set<AbstractEffect> DISALLOWED_ELEMENTAL_INFINITY_AND_ESSENCE_EFFECTS = Sets.newHashSet(
//			EffectConjureTerrain.INSTANCE
//	);
//
//	public ArsElementalCompat() {
//		ArsNouveauCompat.ALLOWED_CAST_METHODS.addAll(ALLOWED_ELEMENTAL_CAST_METHODS);
//		ArsNouveauCompat.DISALLOWED_INFINITY_AND_ESSENCE_EFFECTS.addAll(DISALLOWED_ELEMENTAL_INFINITY_AND_ESSENCE_EFFECTS);
//
//		// Keep at end so it is only set to true if no exceptions was thrown during setup
//		ModChecker.arsElementalPresent = true;
//	}
//
//	@SuppressWarnings("ConstantConditions")
//	public static boolean isArsElementalCasting(EffectResolveEvent.Post event) {
//		if (event.spell.getCastMethod() == MethodHomingProjectile.INSTANCE && !BuiltInRegistries.ENTITY_TYPE.get(SPELL_HOMING_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
//			return true;
//		}
//		else if (event.spell.getCastMethod() == MethodArcProjectile.INSTANCE && !BuiltInRegistries.ENTITY_TYPE.get(SPELL_CURVED_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
//			return true;
//		}
//		return false;
//	}
}
