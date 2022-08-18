package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
		return new ResourceLocation(Bumblezone.MODID, "jei_plugin");
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
		addInfo(registration, BzItems.EMPTY_HONEYCOMB_BROOD.get());
		addInfo(registration, BzItems.FILLED_POROUS_HONEYCOMB.get());
		addInfo(registration, BzItems.HONEY_CRYSTAL.get());
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHARDS.get());
		addInfo(registration, BzItems.HONEY_CRYSTAL_SHIELD.get());
		addInfo(registration, BzItems.HONEYCOMB_BROOD.get());
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
		addInfo(registration, BzItems.BEE_QUEEN_SPAWN_EGG.get());
		addInfo(registration, BzFluids.SUGAR_WATER_FLUID.get());
		addInfo(registration, BzFluids.ROYAL_JELLY_FLUID.get());
		addInfo(registration, BzItems.ROYAL_JELLY_BOTTLE.get());
		addInfo(registration, BzItems.ROYAL_JELLY_BUCKET.get());
		addInfo(registration, BzItems.ROYAL_JELLY_BLOCK.get());
		addInfo(registration, BzItems.POLLEN_PUFF.get());
		addInfo(registration, BzItems.BEE_BREAD.get());
		addInfo(registration, BzFluids.HONEY_FLUID.get());
		addInfo(registration, BzItems.HONEY_BUCKET.get());
		addInfo(registration, BzItems.HONEY_WEB.get());
		addInfo(registration, BzItems.REDSTONE_HONEY_WEB.get());
		addInfo(registration, BzItems.HONEY_COCOON.get());
		addInfo(registration, BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.get());
		addInfo(registration, BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY.get());
		addInfo(registration, BzItems.STINGER_SPEAR.get());
		addInfo(registration, BzItems.HONEY_COMPASS.get());
		addInfo(registration, BzItems.BEE_STINGER.get());
		addInfo(registration, BzItems.BEE_CANNON.get());
		addInfo(registration, BzItems.CRYSTAL_CANNON.get());
		addInfo(registration, BzItems.HONEY_BEE_LEGGINGS_1.get());
		addInfo(registration, BzItems.HONEY_BEE_LEGGINGS_2.get());
		addInfo(registration, BzItems.BUMBLE_BEE_CHESTPLATE_1.get());
		addInfo(registration, BzItems.BUMBLE_BEE_CHESTPLATE_2.get());
		addInfo(registration, BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1.get());
		addInfo(registration, BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2.get());
		addInfo(registration, BzItems.STINGLESS_BEE_HELMET_1.get());
		addInfo(registration, BzItems.STINGLESS_BEE_HELMET_2.get());
		addInfo(registration, BzItems.CARPENTER_BEE_BOOTS_1.get());
		addInfo(registration, BzItems.CARPENTER_BEE_BOOTS_2.get());
		addInfo(registration, BzItems.ESSENCE_OF_THE_BEES.get());
		addInfo(registration, BzItems.GLISTERING_HONEY_CRYSTAL.get());
		addInfo(registration, BzItems.SUPER_CANDLE.get());
		addInfo(registration, BzItems.SUPER_CANDLE_BLACK.get());
		addInfo(registration, BzItems.SUPER_CANDLE_BLUE.get());
		addInfo(registration, BzItems.SUPER_CANDLE_BROWN.get());
		addInfo(registration, BzItems.SUPER_CANDLE_CYAN.get());
		addInfo(registration, BzItems.SUPER_CANDLE_GRAY.get());
		addInfo(registration, BzItems.SUPER_CANDLE_GREEN.get());
		addInfo(registration, BzItems.SUPER_CANDLE_LIGHT_BLUE.get());
		addInfo(registration, BzItems.SUPER_CANDLE_LIGHT_GRAY.get());
		addInfo(registration, BzItems.SUPER_CANDLE_LIME.get());
		addInfo(registration, BzItems.SUPER_CANDLE_MAGENTA.get());
		addInfo(registration, BzItems.SUPER_CANDLE_ORANGE.get());
		addInfo(registration, BzItems.SUPER_CANDLE_PINK.get());
		addInfo(registration, BzItems.SUPER_CANDLE_PURPLE.get());
		addInfo(registration, BzItems.SUPER_CANDLE_RED.get());
		addInfo(registration, BzItems.SUPER_CANDLE_WHITE.get());
		addInfo(registration, BzItems.SUPER_CANDLE_YELLOW.get());
		addInfo(registration, BzItems.INCENSE_CANDLE.get());

		ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;
		RecipeManager recipeManager = level.getRecipeManager();
		recipeManager.byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
				.ifPresent(recipe -> registerExtraRecipes(recipe, registration));
    }

    
    private static void addInfo(IRecipeRegistration registration, Item item) {
	registration.addIngredientInfo(
		new ItemStack(item),
		VanillaTypes.ITEM_STACK,
		Component.translatable(Bumblezone.MODID + "." + ForgeRegistries.ITEMS.getKey(item).getPath() + ".jei_description"));
    }
    
    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
	registration.addIngredientInfo(
		new FluidStack(fluid, 1),
		ForgeTypes.FLUID_STACK,
		Component.translatable(Bumblezone.MODID + "." + ForgeRegistries.FLUIDS.getKey(fluid).getPath() + ".jei_description"));
    }

	/**
	 * If your item has subtypes that depend on NBT or capabilities, use this to
	 * help JEI identify those subtypes correctly.
	 */
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(BzItems.INCENSE_CANDLE.get(), JEIIntegration::getIncenseCandleSubtype);
	}

	private static String getIncenseCandleSubtype(ItemStack itemStack, UidContext context) {
		if (itemStack.hasTag()) {
			CompoundTag blockEntityTag = itemStack.getOrCreateTag().getCompound("BlockEntityTag");
			if (blockEntityTag.contains(IncenseCandleBlockEntity.STATUS_EFFECT_TAG)) {
				ResourceLocation rl = new ResourceLocation(blockEntityTag.getString(IncenseCandleBlockEntity.STATUS_EFFECT_TAG));
				return rl.toString();
			}
		}
		return "N/A";
	}

	private static void registerExtraRecipes(Recipe<?> baseRecipe, IRecipeRegistration registration) {
		if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
			List<CraftingRecipe> extraRecipes = new ArrayList<>();
			for (Potion potion : Registry.POTION) {
				addRecipeIfValid(extraRecipes, JEIIncenseCandleRecipe.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.POTION.getDefaultInstance()));
				addRecipeIfValid(extraRecipes, JEIIncenseCandleRecipe.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.SPLASH_POTION.getDefaultInstance()));
				addRecipeIfValid(extraRecipes, JEIIncenseCandleRecipe.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.LINGERING_POTION.getDefaultInstance()));
			}
			registration.addRecipes(RecipeTypes.CRAFTING, extraRecipes);
		}
	}

	private static void addRecipeIfValid(List<CraftingRecipe> extraRecipes, ShapedRecipe recipe) {
		if (!recipe.getResultItem().isEmpty()) {
			extraRecipes.add(recipe);
		}
	}
}
