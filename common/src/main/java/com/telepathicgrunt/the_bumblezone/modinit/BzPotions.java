package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class BzPotions {
    public static final ResourcefulRegistry<Potion> POTIONS = ResourcefulRegistries.create(BuiltInRegistries.POTION, Bumblezone.MODID);

    public static final RegistryEntry<Potion> NEUROTOXIN = POTIONS.register("neurotoxin", () -> new Potion(new MobEffectInstance(BzEffects.PARALYZED.get(), 200)));
    public static final RegistryEntry<Potion> LONG_NEUROTOXIN = POTIONS.register("long_neurotoxin", () -> new Potion(new MobEffectInstance(BzEffects.PARALYZED.get(), 600)));
}
