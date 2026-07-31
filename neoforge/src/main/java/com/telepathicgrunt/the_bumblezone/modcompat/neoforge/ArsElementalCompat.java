package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public class ArsElementalCompat implements ModCompat {
//	private static final Identifier SPELL_HOMING_PROJ_RL = Identifier.fromNamespaceAndPath("ars_nouveau", "homing_spell_proj");
//	private static final Identifier SPELL_CURVED_PROJ_RL = Identifier.fromNamespaceAndPath("ars_nouveau", "arcing_spell_proj");
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
//	public static boolean isArsElementalCasting(AbstractCastMethod closestCastMethod) {
//		if (closestCastMethod == MethodHomingProjectile.INSTANCE && !BuiltInRegistries.ENTITY_TYPE.get(SPELL_HOMING_PROJ_RL).get().is(BzTags.TELEPORT_PROJECTILES)) {
//			return true;
//		}
//		else if (closestCastMethod == MethodArcProjectile.INSTANCE && !BuiltInRegistries.ENTITY_TYPE.get(SPELL_CURVED_PROJ_RL).get().is(BzTags.TELEPORT_PROJECTILES)) {
//			return true;
//		}
//		return false;
//	}
//
//	public static boolean isArsElementalProjectile(Identifier projectileRL) {
//		if (projectileRL != null && (projectileRL.equals(SPELL_HOMING_PROJ_RL) || projectileRL.equals(SPELL_CURVED_PROJ_RL))) {
//			return true;
//		}
//		return false;
//	}
}
