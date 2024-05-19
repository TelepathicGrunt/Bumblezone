package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.potions.ConfigSafePotion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;

public class BzPotions {
    public static final ResourcefulRegistry<Potion> POTIONS = ResourcefulRegistries.create(BuiltInRegistries.POTION, Bumblezone.MODID);

    public static final HolderRegistryEntry<Potion> NEUROTOXIN = POTIONS.registerHolder("neurotoxin", () -> new ConfigSafePotion(() -> List.of(new MobEffectInstance(BzEffects.PARALYZED.holder(), Math.min(BzGeneralConfigs.paralyzedMaxTickDuration / 2, 200)))));
    public static final HolderRegistryEntry<Potion> LONG_NEUROTOXIN = POTIONS.registerHolder("long_neurotoxin", () -> new ConfigSafePotion(() -> List.of(new MobEffectInstance(BzEffects.PARALYZED.holder(), Math.min(BzGeneralConfigs.paralyzedMaxTickDuration, 600)))));
}
