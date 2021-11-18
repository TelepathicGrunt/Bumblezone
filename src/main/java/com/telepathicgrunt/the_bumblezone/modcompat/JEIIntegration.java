package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
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
		addInfo(registration, BzItems.EMPTY_HONEYCOMB_LARVA.get());
		addInfo(registration, BzItems.FILLED_POROUS_HONEYCOMB.get());
		addInfo(registration, BzItems.HONEY_CRYSTAL.get());
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHARDS.get());
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHIELD.get());
		addInfo(registration, BzItems.HONEYCOMB_LARVA.get());
		addInfo(registration, BzItems.POROUS_HONEYCOMB.get());
		addInfo(registration, BzItems.STICKY_HONEY_REDSTONE.get());
		addInfo(registration, BzItems.STICKY_HONEY_RESIDUE.get());
		addInfo(registration, BzItems.SUGAR_INFUSED_COBBLESTONE.get());
		addInfo(registration, BzItems.SUGAR_INFUSED_STONE.get());
		addInfo(registration, BzItems.SUGAR_WATER_BOTTLE.get());
		addInfo(registration, BzItems.SUGAR_WATER_BUCKET.get());
		addInfo(registration, BzItems.BEEHIVE_BEESWAX.get());
		addInfo(registration, BzItems.HONEY_SLIME_SPAWN_EGG.get());
		addInfo(registration, BzItems.BEEHEMOTH_SPAWN_EGG.get());
		addInfo(registration, BzFluids.SUGAR_WATER_FLUID.get());
		addInfo(registration, BzItems.POLLEN_PUFF.get());
		addInfo(registration, BzItems.BEE_BREAD.get());
		addInfo(registration, BzFluids.HONEY_FLUID.get());
		addInfo(registration, BzItems.HONEY_BUCKET.get());
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
