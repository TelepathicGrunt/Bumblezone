package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.BeenergizedEffect;
import com.telepathicgrunt.the_bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Bumblezone.MODID);

    public static final RegistryObject<Effect> WRATH_OF_THE_HIVE = EFFECTS.register("wrath_of_the_hive", () -> new WrathOfTheHiveEffect(EffectType.HARMFUL, 16737285));
    public static final RegistryObject<Effect> PROTECTION_OF_THE_HIVE = EFFECTS.register("protection_of_the_hive", () -> new ProtectionOfTheHiveEffect(EffectType.BENEFICIAL, 15049988));
    public static final RegistryObject<Effect> BEENERGIZED = EFFECTS.register("beenergized", () -> new BeenergizedEffect(EffectType.BENEFICIAL, 16768000).addAttributeModifier(Attributes.FLYING_SPEED, "9ed2fcd5-061e-4e25-a033-4306b824e941", 0.04D, AttributeModifier.Operation.MULTIPLY_BASE));
}
