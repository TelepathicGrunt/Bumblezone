package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow @Final private Map<RegistryKey<World>, ServerWorld> worlds;

    //Make list of mobs to attack upon creation of the world as we need
    //the world to make the mobs to check their classification.
    //Thanks Mojang
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;createWorlds(Lnet/minecraft/server/WorldGenerationProgressListener;)V",
            at = @At("TAIL"))
    private void onWorldCreation(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        BeeAggression.setupBeeHatingList(worlds.get(World.OVERWORLD));
    }
}