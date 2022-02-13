package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.BeenergizedEffect;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import com.telepathicgrunt.the_bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Bumblezone.MODID);

    public static final RegistryObject<MobEffect> WRATH_OF_THE_HIVE = EFFECTS.register("wrath_of_the_hive", () -> new WrathOfTheHiveEffect(MobEffectCategory.HARMFUL, 16737285));
    public static final RegistryObject<MobEffect> PROTECTION_OF_THE_HIVE = EFFECTS.register("protection_of_the_hive", () -> new ProtectionOfTheHiveEffect(MobEffectCategory.BENEFICIAL, 15049988));
    public static final RegistryObject<MobEffect> BEENERGIZED = EFFECTS.register("beenergized", () -> new BeenergizedEffect(MobEffectCategory.BENEFICIAL, 16768000).addAttributeModifier(Attributes.FLYING_SPEED, "9ed2fcd5-061e-4e25-a033-4306b824e941", 0.04D, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> HIDDEN = EFFECTS.register("hidden", () -> new HiddenEffect(MobEffectCategory.BENEFICIAL, 5308540));
    public static final RegistryObject<MobEffect> PARALYZED = EFFECTS.register("paralyzed", () -> new ParalyzedEffect(MobEffectCategory.HARMFUL, 15662848));
}
