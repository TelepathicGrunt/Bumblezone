package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class BeeDungeon extends NbtFeature{

    public BeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NbtFeatureConfig> context) {
        //affect rarity
        if (BzWorldgenConfigs.beeDungeonRarity.get()>= 1000 ||
                context.random().nextInt(BzWorldgenConfigs.beeDungeonRarity.get()) != 0) return false;

        // generate dungeon
        super.place(context);

        return true;
    }
}
