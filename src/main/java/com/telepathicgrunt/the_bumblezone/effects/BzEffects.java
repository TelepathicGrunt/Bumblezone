package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;

public class BzEffects {
    public final static Effect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(EffectType.HARMFUL, 16748549);
    public final static Effect PROTECTION_OF_THE_HIVE = new ProtectionOfTheHiveEffect(EffectType.BENEFICIAL, 16570117);

    public static void registerEffects(final RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(WRATH_OF_THE_HIVE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "wrath_of_the_hive")));
        event.getRegistry().register(PROTECTION_OF_THE_HIVE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "protection_of_the_hive")));
    }
}
