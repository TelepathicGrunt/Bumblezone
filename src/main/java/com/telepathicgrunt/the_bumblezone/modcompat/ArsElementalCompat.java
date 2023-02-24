package com.telepathicgrunt.the_bumblezone.modcompat;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class ArsElementalCompat {
	private static final ResourceLocation SPELL_HOMING_PROJ_RL = new ResourceLocation("ars_elemental", "homing_projectile");
	private static final ResourceLocation SPELL_CURVED_PROJ_RL = new ResourceLocation("ars_elemental", "curved_projectile");

	private static final Set<AbstractCastMethod> ALLOWED_ELEMENTAL_CAST_METHODS = Sets.newHashSet(
			MethodHomingProjectile.INSTANCE,
			MethodCurvedProjectile.INSTANCE
	);

	public static void setupCompat() {
		ArsNouveauCompat.ALLOWED_CAST_METHODS.addAll(ALLOWED_ELEMENTAL_CAST_METHODS);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.arsElementalPresent = true;
	}

	protected static boolean isArsElementalSpellDisallowed(EffectResolveEvent.Post event) {
		if (event.spell.getCastMethod() == MethodHomingProjectile.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_HOMING_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
			return true;
		}
		else if (event.spell.getCastMethod() == MethodCurvedProjectile.INSTANCE && !ForgeRegistries.ENTITY_TYPES.getValue(SPELL_CURVED_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
			return true;
		}

		return false;
	}

	public static boolean isArsElementalSpellProjectile(ResourceLocation projectileRL) {
		return projectileRL.equals(SPELL_HOMING_PROJ_RL) || projectileRL.equals(SPELL_CURVED_PROJ_RL);
	}
}
