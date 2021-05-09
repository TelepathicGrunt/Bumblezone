package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modcompat.ResourcefulBeesRedirection;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow @Final
    private Map<RegistryKey<World>, ServerWorld> levels;

    //Make list of mobs to attack upon creation of the world as we need
    //the world to make the mobs to check their classification.
    //Thanks Mojang
    @Inject(method = "createLevels(Lnet/minecraft/world/chunk/listener/IChunkStatusListener;)V",
            at = @At("RETURN"))
    private void onWorldCreation(CallbackInfo ci) {
        BeeAggression.setupBeeHatingList(levels.get(World.OVERWORLD));
    }

    @Inject(
            method = "<init>(Ljava/lang/Thread;Lnet/minecraft/util/registry/DynamicRegistries$Impl;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lnet/minecraft/world/storage/IServerConfiguration;Lnet/minecraft/resources/ResourcePackList;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/resources/DataPackRegistries;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/server/management/PlayerProfileCache;Lnet/minecraft/world/chunk/listener/IChunkStatusListenerFactory;)V",
            at = @At(value = "TAIL")
    )
    private void modifyBiomeRegistry(Thread thread, DynamicRegistries.Impl impl, SaveFormat.LevelSave session,
                                     IServerConfiguration saveProperties, ResourcePackList resourcePackManager,
                                     Proxy proxy, DataFixer dataFixer, DataPackRegistries serverResourceManager,
                                     MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository,
                                     PlayerProfileCache userCache, IChunkStatusListenerFactory worldGenerationProgressListenerFactory,
                                     CallbackInfo ci)
    {
        // BiomeLoadEvent is too early for our tag blacklist to allow blacklisting features.
        // Please use BiomeLoadEvent for everything else. I'm just doing this because I have an edge case.

        if(ModChecker.productiveBeesPresent || ModChecker.resourcefulBeesPresent){
            // make my biomes mutable

            List<Biome> bumblezone_biomes = impl.registryOrThrow(Registry.BIOME_REGISTRY).entrySet().stream()
                    .filter(entry -> entry.getKey().location().getNamespace().equals(Bumblezone.MODID))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            // Only bumblezone biomes will be mutable
            bumblezone_biomes.forEach(GeneralUtils::makeBiomeMutable);

            if(ModChecker.resourcefulBeesPresent){
                ResourcefulBeesRedirection.RBAddWorldgen(bumblezone_biomes);
            }

            if(ModChecker.productiveBeesPresent){
                ProductiveBeesRedirection.PBAddWorldgen(bumblezone_biomes);
            }
        }
    }
}