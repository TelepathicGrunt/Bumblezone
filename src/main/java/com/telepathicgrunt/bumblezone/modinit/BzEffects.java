package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BzEffects {
    public final static StatusEffect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(StatusEffectType.HARMFUL, 16748549);
    public final static StatusEffect PROTECTION_OF_THE_HIVE = new ProtectionOfTheHiveEffect(StatusEffectType.BENEFICIAL, 16570117);

    public static void registerEffects() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Bumblezone.MODID, "wrath_of_the_hive"), WRATH_OF_THE_HIVE);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Bumblezone.MODID, "protection_of_the_hive"), PROTECTION_OF_THE_HIVE);
    }
}
