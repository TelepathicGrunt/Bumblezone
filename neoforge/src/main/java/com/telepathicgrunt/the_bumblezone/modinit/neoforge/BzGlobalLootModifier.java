package com.telepathicgrunt.the_bumblezone.modinit.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.loot.neoforge.BeeStingerLootApplier;
import com.telepathicgrunt.the_bumblezone.loot.neoforge.DimensionFishingLootApplier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class BzGlobalLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Bumblezone.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BeeStingerLootApplier>> INJECT_DROPS_TO_NON_BZ_MOBS = GLM.register("inject_drops_to_non_bz_mobs", BeeStingerLootApplier.CODEC);
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<DimensionFishingLootApplier>> DIMENSION_FISHING = GLM.register("dimension_fishing", DimensionFishingLootApplier.CODEC);
}
