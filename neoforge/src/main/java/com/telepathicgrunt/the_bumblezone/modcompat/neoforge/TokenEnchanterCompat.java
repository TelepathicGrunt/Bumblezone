package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinDataPacks;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

import java.util.EnumSet;
import java.util.Optional;

public class TokenEnchanterCompat implements ModCompat {

    public TokenEnchanterCompat() {

        Bumblezone.MOD_COMPAT_DATAPACKS.add(addBuiltinDataPacks ->
                addBuiltinDataPacks.add(
                        new ResourceLocation(Bumblezone.MODID, "enchanted_token_compat"),
                        Component.literal("Bumblezone - Token Enchanter Compat"),
                        AddBuiltinDataPacks.PackMode.FORCE_ENABLED
                )
        );

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.tokenEnchanterPresent = true;
    }
}
