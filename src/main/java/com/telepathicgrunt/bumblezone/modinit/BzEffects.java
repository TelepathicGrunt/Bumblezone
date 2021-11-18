package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.effects.BeenergizedEffect;
import com.telepathicgrunt.bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BzEffects {
    public final static MobEffect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(MobEffectCategory.HARMFUL, 16737285);
    public final static MobEffect PROTECTION_OF_THE_HIVE = new ProtectionOfTheHiveEffect(MobEffectCategory.BENEFICIAL, 15049988);
    public final static MobEffect BEENERGIZED = new BeenergizedEffect(MobEffectCategory.BENEFICIAL, 16768000).addAttributeModifier(Attributes.FLYING_SPEED, "9ed2fcd5-061e-4e25-a033-4306b824e941", 0.04D, AttributeModifier.Operation.MULTIPLY_BASE);

    public static void registerEffects() {
        Registry.register(Registry.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "wrath_of_the_hive"), WRATH_OF_THE_HIVE);
        Registry.register(Registry.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "protection_of_the_hive"), PROTECTION_OF_THE_HIVE);
        Registry.register(Registry.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "beenergized"), BEENERGIZED);
    }
}
