package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BzEffects {
    public final static MobEffect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(MobEffectCategory.HARMFUL, 16748549);
    public final static MobEffect PROTECTION_OF_THE_HIVE = new ProtectionOfTheHiveEffect(MobEffectCategory.BENEFICIAL, 16570117);

    public static void registerEffects() {
        Registry.register(Registry.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "wrath_of_the_hive"), WRATH_OF_THE_HIVE);
        Registry.register(Registry.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "protection_of_the_hive"), PROTECTION_OF_THE_HIVE);
    }
}
