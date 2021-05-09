package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;


public class SpiderInfestedBeeDungeon extends NbtFeature{

    public SpiderInfestedBeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NbtFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BzDungeonsConfig.spiderInfestedBeeDungeonRarity.get() >= 1000 ||
            random.nextInt(Bumblezone.BzDungeonsConfig.spiderInfestedBeeDungeonRarity.get()) != 0) return false;

        // generate dungeon
        super.place(world, generator, random, position, config);

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
        for(int x = -8; x <= 12; x++) {
            for(int y = -6; y <= 10; y++) {
                for(int z = -8; z <= 12; z++) {
                    blockpos$Mutable.set(position).move(x, y, z);
                    if(random.nextFloat() < 0.07f && world.getBlockState(blockpos$Mutable).getBlock() == Blocks.CAVE_AIR) {
                        world.setBlock(blockpos$Mutable, Blocks.COBWEB.defaultBlockState(), 3);
                    }
                }
            }
        }

        return true;
    }
}
