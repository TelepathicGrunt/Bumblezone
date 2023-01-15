package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record RegisterPotionBrewsEvent(List<Brew> brews) {

    public static final EventHandler<RegisterPotionBrewsEvent> EVENT = new EventHandler<>();

    public void register(Brew brew) {
        brews.add(brew);
    }

    public void register(Potion input, Ingredient ingredient, Potion output) {
        register(new Brew(input, ingredient, output));
    }

    public void register(Potion input, ItemStack stack, Potion output) {
        register(input, Ingredient.of(stack), output);
    }

    public void register(Potion input, Item item, Potion output) {
        register(input, item.getDefaultInstance(), output);
    }

    public record Brew(Potion input, Ingredient ingredient, Potion output) {

    }
}
