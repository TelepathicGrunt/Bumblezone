package com.telepathicgrunt.the_bumblezone.modCompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

@JeiPlugin
public class JEIIntegration implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid() {
	return new ResourceLocation(Bumblezone.MODID+"jei_plugin");
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
		addInfo(registration, BzItems.EMPTY_HONEYCOMB_LARVA);
		addInfo(registration, BzItems.FILLED_POROUS_HONEYCOMB);
		addInfo(registration, BzItems.HONEY_CRYSTAL);
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHARDS);
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHIELD);
		addInfo(registration, BzItems.HONEYCOMB_LARVA);
		addInfo(registration, BzItems.POROUS_HONEYCOMB);
		addInfo(registration, BzItems.STICKY_HONEY_REDSTONE);
		addInfo(registration, BzItems.STICKY_HONEY_RESIDUE);
		addInfo(registration, BzItems.SUGAR_INFUSED_COBBLESTONE);
		addInfo(registration, BzItems.SUGAR_INFUSED_STONE);
		addInfo(registration, BzItems.SUGAR_WATER_BOTTLE);
		addInfo(registration, BzItems.SUGAR_WATER_BUCKET);
		addInfo(registration, BzItems.BEESWAX_PLANKS);
		addInfo(registration, BzItems.HONEY_SLIME_SPAWN_EGG);
		addInfo(registration, BzBlocks.SUGAR_WATER_FLUID);
    }

    
    private static void addInfo(IRecipeRegistration registration, Item item) {
	registration.addIngredientInfo(
		new ItemStack(item), 
		VanillaTypes.ITEM, 
		Bumblezone.MODID+"."+item.getRegistryName().getPath()+".jei_description");
    }
    
    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
	registration.addIngredientInfo(
		new FluidStack(fluid, 1), 
		VanillaTypes.FLUID, 
		Bumblezone.MODID+"."+fluid.getRegistryName().getPath()+".jei_description");
    }
}
