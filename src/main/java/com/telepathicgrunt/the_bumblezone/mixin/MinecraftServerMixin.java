package com.telepathicgrunt.the_bumblezone.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ProductiveBeesRedirection;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.*;
import java.util.function.Supplier;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;

    @Shadow @Final
    protected DynamicRegistries.Impl registryManager;

    //Make list of mobs to attack upon creation of the world as we need
    //the world to make the mobs to check their classification.
    //Thanks Mojang
    @Inject(method = "Lnet/minecraft/server/MinecraftServer;createWorlds(Lnet/minecraft/world/chunk/listener/IChunkStatusListener;)V",
            at = @At("TAIL"))
    private void onWorldCreation(CallbackInfo ci) {
        BeeAggression.setupBeeHatingList(worlds.get(World.OVERWORLD));

    }

    // Used for adding mod compat dependant features to Bumblezone biomes
    @Inject(
            method = "<init>",
            at = @At(value = "TAIL")
    )
    private void modifyBiomeRegistry(Thread p_i232576_1_, DynamicRegistries.Impl impl, SaveFormat.LevelSave p_i232576_3_, IServerConfiguration p_i232576_4_, ResourcePackList p_i232576_5_, Proxy p_i232576_6_, DataFixer p_i232576_7_, DataPackRegistries p_i232576_8_, MinecraftSessionService p_i232576_9_, GameProfileRepository p_i232576_10_, PlayerProfileCache p_i232576_11_, IChunkStatusListenerFactory p_i232576_12_, CallbackInfo ci) {
        boolean needToAddModCompatFeatures = ModChecker.productiveBeesPresent;

        if(needToAddModCompatFeatures && registryManager.getOptional(Registry.BIOME_KEY).isPresent()) {
            for(ResourceLocation biomeRL : Bumblezone.BIOME_ID_LIST){
                addModdedFeatures(biomeRL, registryManager);
            }
        }
    }

    private static void addModdedFeatures(ResourceLocation moddedBiomeRL, DynamicRegistries.Impl registryManager){
        // Make the structure and features list mutable for modification later
        Biome biome = registryManager.getOptional(Registry.BIOME_KEY).get().getOrDefault(moddedBiomeRL);
        if(biome == null){
            Bumblezone.LOGGER.log(Level.WARN, "Error trying to add mod compat to : "+moddedBiomeRL+". Please contact the Bumblezone developer TelepathicGrunt!");
            return;
        }

        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((GenerationSettingsAccessor)biome.getGenerationSettings()).getGSFeatures();
        List<List<Supplier<ConfiguredFeature<?, ?>>>> mutableFeatures = new ArrayList<>();
        for(int i = 0; i < Math.max(10, tempFeature.size()); i++){
            if(i >= tempFeature.size()){
                mutableFeatures.add(new ArrayList<>());
            }
            else{
                mutableFeatures.add(new ArrayList<>(tempFeature.get(i)));
            }
        }
        ((GenerationSettingsAccessor)biome.getGenerationSettings()).setGSFeatures(mutableFeatures);

        //Add our features to the bumblezone biomes
        if(ModChecker.productiveBeesPresent && registryManager.getOptional(Registry.BIOME_KEY).get().getKey(biome).getNamespace().equals("the_bumblezone"))
            ProductiveBeesRedirection.PBAddHoneycombs(biome);
    }
}