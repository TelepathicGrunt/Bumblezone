package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import cy.jdkdigital.productivebees.init.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ProductiveBeesCompatRegs {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, Bumblezone.MODID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, Bumblezone.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> PB_CONFIGURABLE_COMB_CF = CONFIGURED_FEATURES.register("productivebees_be_comb_configured_feature", () ->
        new ConfiguredFeature<>(BzFeatures.BLOCKENTITY_COMBS_FEATURE.get(),
            new OreConfiguration(
                new TagMatchTest(BzTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE),
                ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(),
                16
            )
        )
    );

    public static final RegistryObject<PlacedFeature> PB_CONFIGURABLE_COMB_PF = PLACED_FEATURES.register("productivebees_be_comb_placed_feature", () ->
        new PlacedFeature(Holder.direct(PB_CONFIGURABLE_COMB_CF.get()),
            List.of(
                RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(
                        VerticalAnchor.aboveBottom(10),
                        VerticalAnchor.belowTop(10)),
                BiomeFilter.biome()
            )
        )
    );
}
