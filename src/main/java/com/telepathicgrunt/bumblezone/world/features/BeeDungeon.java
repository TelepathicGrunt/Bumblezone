package com.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;


public class BeeDungeon extends NbtFeature{

    public BeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(FeatureContext<NbtFeatureConfig> context) {
        //affect rarity
        if (Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity>= 1000 ||
                context.getRandom().nextInt(Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity) != 0) return false;

        // generate dungeon
        super.generate(context);

        return true;
    }
}
