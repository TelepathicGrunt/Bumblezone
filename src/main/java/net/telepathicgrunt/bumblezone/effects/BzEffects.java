package net.telepathicgrunt.bumblezone.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class BzEffects
{
	public final static StatusEffect WRATH_OF_THE_HIVE = new WrathOfTheHiveEffect(StatusEffectType.HARMFUL, 16748549);
	
    public static void registerEffects()
    {
    	Registry.register(Registry.STATUS_EFFECT, new Identifier(Bumblezone.MODID, "wrath_of_the_hive"), WRATH_OF_THE_HIVE);
    }
}
