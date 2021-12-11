package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class BeeDungeon extends NbtFeature{

    public BeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NbtFeatureConfig> context) {
        //affect rarity
        if (Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity>= 1000 ||
                context.random().nextInt(Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity) != 0) return false;

        // generate dungeon
        super.place(context);

        return true;
    }
}
