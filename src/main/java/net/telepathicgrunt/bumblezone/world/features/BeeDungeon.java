package net.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;

import java.util.Random;


public class BeeDungeon extends NbtFeature{

    public BeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos position, NbtFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity>= 1000 ||
                random.nextInt(Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity) != 0) return false;

        // generate dungeon
        super.generate(world, generator, random, position, config);

        return true;
    }
}
