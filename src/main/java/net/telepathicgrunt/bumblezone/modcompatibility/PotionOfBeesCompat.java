package net.telepathicgrunt.bumblezone.modcompatibility;

import org.apache.logging.log4j.Level;

import com.github.commoble.potionofbees.ResourceLocations;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class PotionOfBeesCompat
{
	private static final DefaultDispenseItemBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new PotionOfBeesBeePotionDispenseBehavior();
	
	public static void setupPotionOfBees() 
	{
		ModChecking.potionOfBeesPresent = true;
		
		if(ForgeRegistries.ITEMS.containsKey(ResourceLocations.POTION_OF_BEES)) {
			DispenserBlock.registerDispenseBehavior(ForgeRegistries.ITEMS.getValue(ResourceLocations.POTION_OF_BEES), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with bee potions in dispensers
		}
		else {
			Bumblezone.LOGGER.log(Level.WARN, "Error trying to change the dispenser behavior for Potion of Bee's bee potion item. Please report this issue to Bumblezone author.");
		}
		
		if(ForgeRegistries.ITEMS.containsKey(ResourceLocations.SPLASH_POTION_OF_BEES)) {
			DispenserBlock.registerDispenseBehavior(ForgeRegistries.ITEMS.getValue(ResourceLocations.SPLASH_POTION_OF_BEES), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with bee splash potion in dispensers
		}
		else {
			Bumblezone.LOGGER.log(Level.WARN, "Error trying to change the dispenser behavior for Potion of Bee's bee splash potion item. Please report this issue to Bumblezone author.");
		}
	}
}
