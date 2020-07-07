package net.telepathicgrunt.bumblezone.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzPlayerPlacement;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.telepathicgrunt.bumblezone.entities.PlayerTeleportation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public class PlayerTeleportationTickMixin {
    // Handles player teleportation out of Bumblezone dimension
    @Inject(method = "tick",
            at = @At(value = "TAIL"))
    private void onEntityTick(CallbackInfo ci) {
        PlayerTeleportation.playerTick(((PlayerEntity) (Object) this));
        BeeAggression.playerTick(((PlayerEntity) (Object) this));
    }

}