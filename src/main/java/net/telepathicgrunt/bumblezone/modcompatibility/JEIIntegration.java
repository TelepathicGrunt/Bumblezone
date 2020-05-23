package net.telepathicgrunt.bumblezone.modcompatibility;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.items.BzItems;

@JeiPlugin
public class JEIIntegration implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid() {
	return new ResourceLocation(Bumblezone.MODID+"jei_plugin");
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
	addInfo(registration, BzItems.DEAD_HONEYCOMB_LARVA_ITEM.get());
	addInfo(registration, BzItems.FILLED_POROUS_HONEYCOMB_ITEM.get());
	addInfo(registration, BzItems.HONEY_CRYSTAL_BLOCK.get());
	addInfo(registration, BzItems.HONEY_CRYSTAL_SHARDS.get());
	addInfo(registration, BzItems.HONEY_CRYSTAL_SHIELD.get());
	addInfo(registration, BzItems.HONEYCOMB_LARVA_ITEM.get());
	addInfo(registration, BzItems.POROUS_HONEYCOMB_ITEM.get());
	addInfo(registration, BzItems.STICKY_HONEY_REDSTONE.get());
	addInfo(registration, BzItems.STICKY_HONEY_RESIDUE.get());
	addInfo(registration, BzItems.SUGAR_INFUSED_COBBLESTONE_ITEM.get());
	addInfo(registration, BzItems.SUGAR_INFUSED_STONE_ITEM.get());
	addInfo(registration, BzItems.SUGAR_WATER_BOTTLE.get());
	addInfo(registration, BzItems.SUGAR_WATER_BUCKET.get());
    }

    
    private static void addInfo(IRecipeRegistration registration, Item item) {
	registration.addIngredientInfo(
		new ItemStack(item), 
		VanillaTypes.ITEM, 
		Bumblezone.MODID+"."+item.getRegistryName().getPath()+".jei_description");
    }
}
