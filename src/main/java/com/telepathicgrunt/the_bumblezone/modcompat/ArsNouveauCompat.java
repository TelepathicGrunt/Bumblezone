package com.telepathicgrunt.the_bumblezone.modcompat;

import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.event.SpellResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.CastResolveType;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBlink;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.ProjectileImpact;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class ArsNouveauCompat {
	private static final ResourceLocation SPELL_PROJ_RL = new ResourceLocation("ars_nouveau", "spell_proj");

	public static void setupCompat() {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		Consumer<EffectResolveEvent.Post> handler = ArsNouveauCompat::isArsSpellProjectile;
		forgeBus.addListener(handler);

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.arsNouveauPresent = true;
	}

	private static void isArsSpellProjectile(EffectResolveEvent.Post event) {
		if (event.shooter instanceof Player && event.spell.getCastMethod() == MethodProjectile.INSTANCE && event.resolveEffect == EffectBlink.INSTANCE) {
			if (!ForgeRegistries.ENTITY_TYPES.getValue(SPELL_PROJ_RL).is(BzTags.TELEPORT_PROJECTILES)) {
				return;
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
		return ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType()).equals(SPELL_PROJ_RL);
	}
}
