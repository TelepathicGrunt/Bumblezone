package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;


public class BeeDungeon extends NbtFeature{

    public BeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NbtFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BzDungeonsConfig.beeDungeonRarity.get() >= 1000 ||
                random.nextInt(Bumblezone.BzDungeonsConfig.beeDungeonRarity.get()) != 0) return false;

        // generate dungeon
        super.place(world, generator, random, position, config);

        return true;
    }
}
