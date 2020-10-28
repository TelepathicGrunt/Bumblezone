package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BzEffects
{
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Bumblezone.MODID);
	
    public static final RegistryObject<Effect> WRATH_OF_THE_HIVE = createEffect("wrath_of_the_hive", () -> new WrathOfTheHiveEffect(EffectType.HARMFUL, 16748549));
    public static final RegistryObject<Effect> PROTECTION_OF_THE_HIVE = createEffect("protection_of_the_hive", () -> new ProtectionOfTheHiveEffect(EffectType.BENEFICIAL, 16570117));
    
    public static <E extends Effect> RegistryObject<E> createEffect(String name, Supplier<? extends E> effect)
	{
		return EFFECTS.register(name, effect);
	}
}
