package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.fabric.ingredients.fluid.JeiFluidIngredient;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
		return new ResourceLocation(Bumblezone.MODID, "jei_plugin");
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
		addInfo(registration, BzItems.EMPTY_HONEYCOMB_BROOD);
		addInfo(registration, BzItems.FILLED_POROUS_HONEYCOMB);
		addInfo(registration, BzItems.HONEY_CRYSTAL);
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHARDS);
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHIELD);
		addInfo(registration, BzItems.HONEYCOMB_BROOD);
		addInfo(registration, BzItems.POROUS_HONEYCOMB);
		addInfo(registration, BzItems.STICKY_HONEY_REDSTONE);
		addInfo(registration, BzItems.STICKY_HONEY_RESIDUE);
		addInfo(registration, BzItems.SUGAR_INFUSED_COBBLESTONE);
		addInfo(registration, BzItems.SUGAR_INFUSED_STONE);
		addInfo(registration, BzItems.SUGAR_WATER_BOTTLE);
		addInfo(registration, BzItems.SUGAR_WATER_BUCKET);
		addInfo(registration, BzItems.BEEHIVE_BEESWAX);
		addInfo(registration, BzItems.HONEY_SLIME_SPAWN_EGG);
		addInfo(registration, BzItems.BEEHEMOTH_SPAWN_EGG);
		addInfo(registration, BzFluids.SUGAR_WATER_FLUID);
		addInfo(registration, BzItems.POLLEN_PUFF);
		addInfo(registration, BzItems.BEE_BREAD);
		addInfo(registration, BzFluids.HONEY_FLUID);
		addInfo(registration, BzItems.HONEY_BUCKET);
		addInfo(registration, BzItems.HONEY_WEB);
		addInfo(registration, BzItems.REDSTONE_HONEY_WEB);
		addInfo(registration, BzItems.HONEY_COCOON);
		addInfo(registration, BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
		addInfo(registration, BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
		addInfo(registration, BzItems.STINGER_SPEAR);
		addInfo(registration, BzItems.HONEY_COMPASS);
		addInfo(registration, BzItems.BEE_CANNON);
		addInfo(registration, BzItems.HONEY_BEE_LEGGINGS_1);
		addInfo(registration, BzItems.HONEY_BEE_LEGGINGS_2);
		addInfo(registration, BzItems.BUMBLE_BEE_CHESTPLATE_1);
		addInfo(registration, BzItems.BUMBLE_BEE_CHESTPLATE_2);
		addInfo(registration, BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1);
		addInfo(registration, BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2);
		addInfo(registration, BzItems.STINGLESS_BEE_HELMET_1);
		addInfo(registration, BzItems.STINGLESS_BEE_HELMET_2);
		addInfo(registration, BzItems.CARPENTER_BEE_BOOTS_1);
		addInfo(registration, BzItems.CARPENTER_BEE_BOOTS_2);
    }

    
    private static void addInfo(IRecipeRegistration registration, Item item) {
	registration.addIngredientInfo(
		new ItemStack(item),
		VanillaTypes.ITEM_STACK,
		Component.translatable(Bumblezone.MODID + "." + Registry.ITEM.getKey(item).getPath() + ".jei_description"));
    }
    
    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
	registration.addIngredientInfo(
		new JeiFluidIngredient(fluid, 1),
		FabricTypes.FLUID_STACK,
		Component.translatable(Bumblezone.MODID + "." + Registry.FLUID.getKey(fluid).getPath() + ".jei_description"));
    }
}
