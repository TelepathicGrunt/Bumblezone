package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinDataPacks;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class ForestryCompat implements ModCompat {

    public ForestryCompat() {

        Bumblezone.MOD_COMPAT_DATAPACKS.add(addBuiltinDataPacks ->
            addBuiltinDataPacks.add(
                ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "forestry_compat"),
                Component.literal("Bumblezone - Forestry Compat"),
                BzAddBuiltinDataPacks.PackMode.FORCE_ENABLED
            )
        );

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.forestryPresent = true;
    }
}
