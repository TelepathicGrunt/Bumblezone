package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class REICompat implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        addInfo(BzItems.EMPTY_HONEYCOMB_BROOD);
        addInfo(BzItems.FILLED_POROUS_HONEYCOMB);
        addInfo(BzItems.HONEY_CRYSTAL);
        addInfo(BzItems.HONEY_CRYSTAL_SHARDS);
        addInfo(BzItems.HONEY_CRYSTAL_SHIELD);
        addInfo(BzItems.HONEYCOMB_BROOD);
        addInfo(BzItems.POROUS_HONEYCOMB);
        addInfo(BzItems.STICKY_HONEY_REDSTONE);
        addInfo(BzItems.STICKY_HONEY_RESIDUE);
        addInfo(BzItems.SUGAR_INFUSED_COBBLESTONE);
        addInfo(BzItems.SUGAR_INFUSED_STONE);
        addInfo(BzItems.SUGAR_WATER_BOTTLE);
        addInfo(BzItems.SUGAR_WATER_BUCKET);
        addInfo(BzItems.BEEHIVE_BEESWAX);
        addInfo(BzItems.HONEY_SLIME_SPAWN_EGG);
        addInfo(BzItems.BEEHEMOTH_SPAWN_EGG);
        addInfo(BzFluids.SUGAR_WATER_FLUID);
        addInfo(BzItems.POLLEN_PUFF);
        addInfo(BzItems.BEE_BREAD);
        addInfo(BzFluids.HONEY_FLUID);
        addInfo(BzItems.HONEY_BUCKET);
        addInfo(BzItems.HONEY_WEB);
        addInfo(BzItems.REDSTONE_HONEY_WEB);
        addInfo(BzItems.HONEY_COCOON);
        addInfo(BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        addInfo(BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        addInfo(BzItems.STINGER_SPEAR);
        addInfo(BzItems.HONEY_COMPASS);
        addInfo(BzItems.BEE_CANNON);
        addInfo(BzItems.HONEY_BEE_LEGGINGS_1);
        addInfo(BzItems.HONEY_BEE_LEGGINGS_2);
        addInfo(BzItems.BUMBLE_BEE_CHESTPLATE_1);
        addInfo(BzItems.BUMBLE_BEE_CHESTPLATE_2);
        addInfo(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1);
        addInfo(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2);
        addInfo(BzItems.STINGLESS_BEE_HELMET_1);
        addInfo(BzItems.STINGLESS_BEE_HELMET_2);
    }

    private static void addInfo(Item item) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(item),
                Component.translatable(Registry.ITEM.getKey(item).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + Registry.ITEM.getKey(item).getPath() + ".jei_description"));
                    return text;
                });
    }

    private static void addInfo(Fluid fluid) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(fluid, 1),
                Component.translatable(Registry.FLUID.getKey(fluid).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + Registry.FLUID.getKey(fluid).getPath() + ".jei_description"));
                    return text;
                });
    }
}
