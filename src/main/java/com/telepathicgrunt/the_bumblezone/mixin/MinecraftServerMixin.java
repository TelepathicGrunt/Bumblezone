package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
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
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;createWorlds(Lnet/minecraft/world/chunk/listener/IChunkStatusListener;)V",
            at = @At("TAIL"))
    private void onWorldCreation(CallbackInfo ci) {
        BeeAggression.setupBeeHatingList(worlds.get(World.OVERWORLD));
    }
}