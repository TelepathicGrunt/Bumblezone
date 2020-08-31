package com.telepathicgrunt.bumblezone.effects;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class BzEffects {
    public final static Effect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(EffectType.HARMFUL, 16748549);
    public final static Effect PROTECTION_OF_THE_HIVE = new ProtectionOfTheHiveEffect(EffectType.BENEFICIAL, 16570117);

    public static void registerEffects() {
        Registry.register(Registry.EFFECTS, new ResourceLocation(Bumblezone.MODID, "wrath_of_the_hive"), WRATH_OF_THE_HIVE);
        Registry.register(Registry.EFFECTS, new ResourceLocation(Bumblezone.MODID, "protection_of_the_hive"), PROTECTION_OF_THE_HIVE);
    }
}
