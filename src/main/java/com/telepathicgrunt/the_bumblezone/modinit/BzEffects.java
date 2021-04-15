package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Bumblezone.MODID);

    public static final RegistryObject<Effect> WRATH_OF_THE_HIVE = EFFECTS.register("wrath_of_the_hive", () -> new WrathOfTheHiveEffect(EffectType.HARMFUL, 16748549));
    public static final RegistryObject<Effect> PROTECTION_OF_THE_HIVE = EFFECTS.register("protection_of_the_hive", () -> new ProtectionOfTheHiveEffect(EffectType.BENEFICIAL, 16570117));
}
