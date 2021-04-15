package net.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;

import java.util.Random;


public class SpiderInfestedBeeDungeon extends NbtFeature{

    public SpiderInfestedBeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos position, NbtFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BZ_CONFIG.BZDungeonsConfig.spiderInfestedBeeDungeonRarity >= 1000 ||
            random.nextInt(Bumblezone.BZ_CONFIG.BZDungeonsConfig.spiderInfestedBeeDungeonRarity) != 0) return false;

        // generate dungeon
        super.generate(world, generator, random, position, config);

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable();
        for(int x = -8; x <= 12; x++) {
            for(int y = -6; y <= 10; y++) {
                for(int z = -8; z <= 12; z++) {
                    blockpos$Mutable.set(position).move(x, y, z);
                    if(random.nextFloat() < 0.07f && world.getBlockState(blockpos$Mutable).getBlock() == Blocks.CAVE_AIR) {
                        world.setBlockState(blockpos$Mutable, Blocks.COBWEB.getDefaultState(), 3);
                    }
                }
            }
        }

        return true;
    }
}
