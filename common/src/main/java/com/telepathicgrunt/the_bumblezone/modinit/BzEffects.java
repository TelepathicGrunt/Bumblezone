package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.BeenergizedEffect;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.effects.ParalyzedEffect;
import com.telepathicgrunt.the_bumblezone.effects.ProtectionOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BzEffects {
    public static final ResourcefulRegistry<MobEffect> EFFECTS = ResourcefulRegistries.create(BuiltInRegistries.MOB_EFFECT, Bumblezone.MODID);

    public static final RegistryEntry<MobEffect> WRATH_OF_THE_HIVE = EFFECTS.register("wrath_of_the_hive", () -> new WrathOfTheHiveEffect(MobEffectCategory.HARMFUL, 16737285));
    public static final RegistryEntry<MobEffect> PROTECTION_OF_THE_HIVE = EFFECTS.register("protection_of_the_hive", () -> new ProtectionOfTheHiveEffect(MobEffectCategory.BENEFICIAL, 15049988));
    public static final RegistryEntry<MobEffect> BEENERGIZED = EFFECTS.register("beenergized", () -> new BeenergizedEffect(MobEffectCategory.BENEFICIAL, 16768000).addAttributeModifier(Attributes.FLYING_SPEED, "9ed2fcd5-061e-4e25-a033-4306b824e941", 0.04D, AttributeModifier.Operation.ADDITION));
    public static final RegistryEntry<MobEffect> HIDDEN = EFFECTS.register("hidden", () -> new HiddenEffect(MobEffectCategory.BENEFICIAL, 5308540));
    public static final RegistryEntry<MobEffect> PARALYZED = EFFECTS.register("paralyzed", () -> new ParalyzedEffect(MobEffectCategory.HARMFUL, 15662848));
}
