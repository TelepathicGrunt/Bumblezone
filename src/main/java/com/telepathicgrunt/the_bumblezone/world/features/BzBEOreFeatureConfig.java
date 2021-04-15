package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;

public class BzBEOreFeatureConfig implements IFeatureConfig {
    public static final Codec<BzBEOreFeatureConfig> CODEC = RecordCodecBuilder.create((bzBEOreFeatureConfigInstance) -> bzBEOreFeatureConfigInstance.group(
                    RuleTest.field_237127_c_.fieldOf("target").forGetter((bzBEOreFeatureConfig) -> bzBEOreFeatureConfig.target),
                    BlockState.CODEC.fieldOf("state").forGetter((bzBEOreFeatureConfig) -> bzBEOreFeatureConfig.state),
                    Codec.intRange(0, 64).fieldOf("size").forGetter((bzBEOreFeatureConfig) -> bzBEOreFeatureConfig.size),
                    Codec.STRING.fieldOf("type").forGetter((bzBEOreFeatureConfig) -> bzBEOreFeatureConfig.type))
                .apply(bzBEOreFeatureConfigInstance, BzBEOreFeatureConfig::new));

    public final RuleTest target;
    public final int size;
    public final BlockState state;
    public final String type;

    public BzBEOreFeatureConfig(RuleTest ruleTest, BlockState blockState, int size, String type) {
        this.type = type;
        this.size = size;
        this.state = blockState;
        this.target = ruleTest;
    }
}
