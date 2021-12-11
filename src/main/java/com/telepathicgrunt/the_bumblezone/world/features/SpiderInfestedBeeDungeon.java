package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class SpiderInfestedBeeDungeon extends NbtFeature{

    public SpiderInfestedBeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NbtFeatureConfig> context) {
        //affect rarity
        if (BzWorldgenConfigs.spiderInfestedBeeDungeonRarity.get() >= 1000 ||
                context.random().nextInt(BzWorldgenConfigs.spiderInfestedBeeDungeonRarity.get()) != 0) return false;

        // generate dungeon
        super.place(context);

        BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos();
        for(int x = -8; x <= 12; x++) {
            for(int y = -6; y <= 10; y++) {
                for(int z = -8; z <= 12; z++) {
                    blockpos$Mutable.set(context.origin()).move(x, y, z);
                    if(context.random().nextFloat() < 0.07f && context.level().getBlockState(blockpos$Mutable).getBlock() == Blocks.CAVE_AIR) {
                        context.level().setBlock(blockpos$Mutable, Blocks.COBWEB.defaultBlockState(), 3);
                    }
                }
            }
        }

        return true;
    }
}
