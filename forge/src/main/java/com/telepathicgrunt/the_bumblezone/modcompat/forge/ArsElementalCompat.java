package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import alexthw.ars_elemental.common.glyphs.EffectConjureTerrain;
import alexthw.ars_elemental.common.glyphs.MethodArcProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

import java.util.Set;

public class ArsElementalCompat implements ModCompat {
	private static final Set<AbstractCastMethod> ALLOWED_ELEMENTAL_CAST_METHODS = Sets.newHashSet(
			MethodHomingProjectile.INSTANCE,
			MethodArcProjectile.INSTANCE
	);

	protected static final Set<AbstractEffect> DISALLOWED_ELEMENTAL_INFINITY_AND_ESSENCE_EFFECTS = Sets.newHashSet(
			EffectConjureTerrain.INSTANCE
	);

	public ArsElementalCompat() {
		ArsNouveauCompat.ALLOWED_CAST_METHODS.addAll(ALLOWED_ELEMENTAL_CAST_METHODS);
		ArsNouveauCompat.DISALLOWED_INFINITY_AND_ESSENCE_EFFECTS.addAll(DISALLOWED_ELEMENTAL_INFINITY_AND_ESSENCE_EFFECTS);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.arsElementalPresent = true;
	}
}
