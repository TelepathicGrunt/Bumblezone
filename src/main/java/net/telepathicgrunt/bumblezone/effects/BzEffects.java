package net.telepathicgrunt.bumblezone.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class BzEffects
{
	public final static Effect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(EffectType.HARMFUL, 16748549);
	
    public static void registerEffects(RegistryEvent.Register<Effect> event)
    {
    	IForgeRegistry<Effect> registry = event.getRegistry();
    	Bumblezone.register(registry, WRATH_OF_THE_HIVE, "wrath_of_the_hive");
    }
}
