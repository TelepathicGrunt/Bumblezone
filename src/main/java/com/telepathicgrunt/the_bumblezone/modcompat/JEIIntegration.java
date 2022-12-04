package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Optional;


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
        addInfo(registration, BzItems.BEE_QUEEN_SPAWN_EGG);
        addInfo(registration, BzFluids.SUGAR_WATER_FLUID);
        addInfo(registration, BzFluids.ROYAL_JELLY_FLUID);
        addInfo(registration, BzItems.ROYAL_JELLY_BOTTLE);
        addInfo(registration, BzItems.ROYAL_JELLY_BUCKET);
        addInfo(registration, BzItems.ROYAL_JELLY_BLOCK);
        addInfo(registration, BzItems.POLLEN_PUFF);
        addInfo(registration, BzItems.BEE_BREAD);
        addInfo(registration, BzFluids.HONEY_FLUID);
        addInfo(registration, BzItems.HONEY_BUCKET);
        addInfo(registration, BzItems.HONEY_WEB);
        addInfo(registration, BzItems.REDSTONE_HONEY_WEB);
        addInfo(registration, BzItems.HONEY_COCOON);
        addInfo(registration, BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        addInfo(registration, BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        addInfo(registration, BzItems.MUSIC_DISC_LA_BEE_DA_LOCA);
        addInfo(registration, BzItems.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES);
        addInfo(registration, BzItems.STINGER_SPEAR);
        addInfo(registration, BzItems.HONEY_COMPASS);
        addInfo(registration, BzItems.BEE_STINGER);
        addInfo(registration, BzItems.BEE_CANNON);
        addInfo(registration, BzItems.CRYSTAL_CANNON);
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
        addInfo(registration, BzItems.ESSENCE_OF_THE_BEES);
        addInfo(registration, BzItems.GLISTERING_HONEY_CRYSTAL);
        addInfo(registration, BzItems.CARVABLE_WAX);
        addInfo(registration, BzItems.CARVABLE_WAX_WAVY);
        addInfo(registration, BzItems.CARVABLE_WAX_FLOWER);
        addInfo(registration, BzItems.CARVABLE_WAX_CHISELED);
        addInfo(registration, BzItems.CARVABLE_WAX_DIAMOND);
        addInfo(registration, BzItems.CARVABLE_WAX_BRICKS);
        addInfo(registration, BzItems.CARVABLE_WAX_CHAINS);
        addInfo(registration, BzItems.SUPER_CANDLE);
        addInfo(registration, BzItems.SUPER_CANDLE_BLACK);
        addInfo(registration, BzItems.SUPER_CANDLE_BLUE);
        addInfo(registration, BzItems.SUPER_CANDLE_BROWN);
        addInfo(registration, BzItems.SUPER_CANDLE_CYAN);
        addInfo(registration, BzItems.SUPER_CANDLE_GRAY);
        addInfo(registration, BzItems.SUPER_CANDLE_GREEN);
        addInfo(registration, BzItems.SUPER_CANDLE_LIGHT_BLUE);
        addInfo(registration, BzItems.SUPER_CANDLE_LIGHT_GRAY);
        addInfo(registration, BzItems.SUPER_CANDLE_LIME);
        addInfo(registration, BzItems.SUPER_CANDLE_MAGENTA);
        addInfo(registration, BzItems.SUPER_CANDLE_ORANGE);
        addInfo(registration, BzItems.SUPER_CANDLE_PINK);
        addInfo(registration, BzItems.SUPER_CANDLE_PURPLE);
        addInfo(registration, BzItems.SUPER_CANDLE_RED);
        addInfo(registration, BzItems.SUPER_CANDLE_WHITE);
        addInfo(registration, BzItems.SUPER_CANDLE_YELLOW);
        addInfo(registration, BzItems.INCENSE_CANDLE);
        addInfo(registration, BzItems.CRYSTALLINE_FLOWER);

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;
		level.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
				.ifPresent(recipe -> registerExtraRecipes(recipe, registration, false));
		level.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
				.ifPresent(recipe -> registerExtraRecipes(recipe, registration, true));
    }

    private static void addInfo(IRecipeRegistration registration, Item item) {
        registration.addIngredientInfo(
                new ItemStack(item),
                VanillaTypes.ITEM_STACK,
                Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".jei_description"));
    }

    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
        registration.addIngredientInfo(
                new FluidStack(fluid, 1),
                FabricTypes.FLUID_STACK,
                Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".jei_description"));
    }

	private static void registerExtraRecipes(Recipe<?> baseRecipe, IRecipeRegistration registration, boolean oneRecipeOnly) {
		if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
			List<CraftingRecipe> extraRecipes = FakeIncenseCandleRecipeCreator.constructFakeRecipes(incenseCandleRecipe, oneRecipeOnly);
			registration.addRecipes(RecipeTypes.CRAFTING, extraRecipes);
		}
	}

    private static class FluidStack implements IJeiFluidIngredient {

        private final Fluid fluid;
        private final int count;

        private FluidStack(Fluid fluid, int count) {
            this.fluid = fluid;
            this.count = count;
        }


        @Override
        public Fluid getFluid() {
            return fluid;
        }

        @Override
        public long getAmount() {
            return count;
        }

        @Override
        public Optional<CompoundTag> getTag() {
            return Optional.empty();
        }
    }
}
