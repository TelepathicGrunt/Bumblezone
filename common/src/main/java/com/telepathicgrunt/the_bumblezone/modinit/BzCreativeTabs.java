package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.AddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public class BzCreativeTabs {

    public static final List<RegistryEntry<? extends Item>> CUSTOM_CREATIVE_TAB_ITEMS = List.of(
            BzItems.POROUS_HONEYCOMB,
            BzItems.FILLED_POROUS_HONEYCOMB,
            BzItems.EMPTY_HONEYCOMB_BROOD,
            BzItems.HONEYCOMB_BROOD,
            BzItems.STICKY_HONEY_RESIDUE,
            BzItems.STICKY_HONEY_REDSTONE,
            BzItems.HONEY_WEB,
            BzItems.REDSTONE_HONEY_WEB,
            BzItems.BEEHIVE_BEESWAX,
            BzItems.GLISTERING_HONEY_CRYSTAL,
            BzItems.HONEY_CRYSTAL,
            BzItems.CARVABLE_WAX,
            BzItems.CARVABLE_WAX_WAVY,
            BzItems.CARVABLE_WAX_FLOWER,
            BzItems.CARVABLE_WAX_CHISELED,
            BzItems.CARVABLE_WAX_DIAMOND,
            BzItems.CARVABLE_WAX_BRICKS,
            BzItems.CARVABLE_WAX_CHAINS,
            BzItems.HONEY_COCOON,
            BzItems.CRYSTALLINE_FLOWER,
            BzItems.PILE_OF_POLLEN,
            BzItems.PILE_OF_POLLEN_SUSPICIOUS,
            BzItems.SUGAR_INFUSED_STONE,
            BzItems.SUGAR_INFUSED_COBBLESTONE,
            BzItems.INCENSE_CANDLE,
            BzItems.SUPER_CANDLE,
            BzItems.SUPER_CANDLE_WHITE,
            BzItems.SUPER_CANDLE_LIGHT_GRAY,
            BzItems.SUPER_CANDLE_GRAY,
            BzItems.SUPER_CANDLE_BLACK,
            BzItems.SUPER_CANDLE_BROWN,
            BzItems.SUPER_CANDLE_RED,
            BzItems.SUPER_CANDLE_ORANGE,
            BzItems.SUPER_CANDLE_YELLOW,
            BzItems.SUPER_CANDLE_LIME,
            BzItems.SUPER_CANDLE_GREEN,
            BzItems.SUPER_CANDLE_CYAN,
            BzItems.SUPER_CANDLE_LIGHT_BLUE,
            BzItems.SUPER_CANDLE_BLUE,
            BzItems.SUPER_CANDLE_PURPLE,
            BzItems.SUPER_CANDLE_MAGENTA,
            BzItems.SUPER_CANDLE_PINK,
            BzItems.STRING_CURTAIN_WHITE,
            BzItems.STRING_CURTAIN_LIGHT_GRAY,
            BzItems.STRING_CURTAIN_GRAY,
            BzItems.STRING_CURTAIN_BLACK,
            BzItems.STRING_CURTAIN_BROWN,
            BzItems.STRING_CURTAIN_RED,
            BzItems.STRING_CURTAIN_ORANGE,
            BzItems.STRING_CURTAIN_YELLOW,
            BzItems.STRING_CURTAIN_LIME,
            BzItems.STRING_CURTAIN_GREEN,
            BzItems.STRING_CURTAIN_CYAN,
            BzItems.STRING_CURTAIN_LIGHT_BLUE,
            BzItems.STRING_CURTAIN_BLUE,
            BzItems.STRING_CURTAIN_PURPLE,
            BzItems.STRING_CURTAIN_MAGENTA,
            BzItems.STRING_CURTAIN_PINK,
            BzItems.SUGAR_WATER_BOTTLE,
            BzItems.SUGAR_WATER_BUCKET,
            BzItems.ROYAL_JELLY_BOTTLE,
            BzItems.ROYAL_JELLY_BUCKET,
            BzItems.ROYAL_JELLY_BLOCK,
            BzItems.HONEY_BUCKET,
            BzItems.HONEY_COMPASS,
            BzItems.BEE_BREAD,
            BzItems.POLLEN_PUFF,
            BzItems.DIRT_PELLET,
            BzItems.HONEY_CRYSTAL_SHARDS,
            BzItems.BEE_STINGER,
            BzItems.STINGER_SPEAR,
            BzItems.BEE_CANNON,
            BzItems.CRYSTAL_CANNON,
            BzItems.HONEY_CRYSTAL_SHIELD,
            BzItems.STINGLESS_BEE_HELMET_1,
            BzItems.STINGLESS_BEE_HELMET_2,
            BzItems.BUMBLE_BEE_CHESTPLATE_1,
            BzItems.BUMBLE_BEE_CHESTPLATE_2,
            BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1,
            BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2,
            BzItems.HONEY_BEE_LEGGINGS_1,
            BzItems.HONEY_BEE_LEGGINGS_2,
            BzItems.CARPENTER_BEE_BOOTS_1,
            BzItems.CARPENTER_BEE_BOOTS_2,
            BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV,
            BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY,
            BzItems.MUSIC_DISC_LA_BEE_DA_LOCA,
            BzItems.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES,
            BzItems.HONEY_SLIME_SPAWN_EGG,
            BzItems.VARIANT_BEE_SPAWN_EGG,
            BzItems.BEEHEMOTH_SPAWN_EGG,
            BzItems.BEE_QUEEN_SPAWN_EGG,
            BzItems.SENTRY_WATCHER_SPAWN_EGG,
            BzItems.ESSENCE_OF_THE_BEES,
            BzItems.ANCIENT_WAX_BRICKS,
            BzItems.ANCIENT_WAX_DIAMOND,
            BzItems.ANCIENT_WAX_COMPOUND_EYES,
            BzItems.ANCIENT_WAX_BRICKS_STAIRS,
            BzItems.ANCIENT_WAX_DIAMOND_STAIRS,
            BzItems.ANCIENT_WAX_COMPOUND_EYES_STAIRS,
            BzItems.ANCIENT_WAX_BRICKS_SLAB,
            BzItems.ANCIENT_WAX_DIAMOND_SLAB,
            BzItems.ANCIENT_WAX_COMPOUND_EYES_SLAB,
            BzItems.LUMINESCENT_WAX_CHANNEL,
            BzItems.LUMINESCENT_WAX_CHANNEL_RED,
            BzItems.LUMINESCENT_WAX_CHANNEL_PURPLE,
            BzItems.LUMINESCENT_WAX_CHANNEL_BLUE,
            BzItems.LUMINESCENT_WAX_CHANNEL_GREEN,
            BzItems.LUMINESCENT_WAX_CHANNEL_YELLOW,
            BzItems.LUMINESCENT_WAX_CHANNEL_WHITE,
            BzItems.LUMINESCENT_WAX_CORNER,
            BzItems.LUMINESCENT_WAX_CORNER_RED,
            BzItems.LUMINESCENT_WAX_CORNER_PURPLE,
            BzItems.LUMINESCENT_WAX_CORNER_BLUE,
            BzItems.LUMINESCENT_WAX_CORNER_GREEN,
            BzItems.LUMINESCENT_WAX_CORNER_YELLOW,
            BzItems.LUMINESCENT_WAX_CORNER_WHITE,
            BzItems.LUMINESCENT_WAX_NODE,
            BzItems.LUMINESCENT_WAX_NODE_RED,
            BzItems.LUMINESCENT_WAX_NODE_PURPLE,
            BzItems.LUMINESCENT_WAX_NODE_BLUE,
            BzItems.LUMINESCENT_WAX_NODE_GREEN,
            BzItems.LUMINESCENT_WAX_NODE_YELLOW,
            BzItems.LUMINESCENT_WAX_NODE_WHITE,
            BzItems.BEE_SOUP,
            BzItems.BUZZING_BRIEFCASE,
            BzItems.ESSENCE_RED,
            BzItems.ESSENCE_PURPLE,
            BzItems.ESSENCE_BLUE,
            BzItems.ESSENCE_GREEN,
            BzItems.ESSENCE_YELLOW,
            BzItems.ESSENCE_WHITE,
            BzItems.HEAVY_AIR,
            BzItems.WINDY_AIR
    );

    public static final ResourcefulRegistry<CreativeModeTab> CREATIVE_MODE_TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, Bumblezone.MODID);

    public static final RegistryEntry<CreativeModeTab> BUMBLEZONE_MAIN_TAB = CREATIVE_MODE_TABS.register("main_tab", () ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + Bumblezone.MODID + ".main_tab"))
                    .icon(() -> {
                        ItemStack iconStack = BzItems.HONEYCOMB_BROOD.get().getDefaultInstance();
                        iconStack.getOrCreateTag().putBoolean("isCreativeTabIcon", true);
                        return iconStack;
                    })
                    .displayItems((itemDisplayParameters, output) ->
                            CUSTOM_CREATIVE_TAB_ITEMS.stream().map(item -> item.get().getDefaultInstance()).forEach(output::accept)).build());

    public static void addCreativeTabEntries(AddCreativeTabEntriesEvent event) {
        if (event.type() == AddCreativeTabEntriesEvent.Type.REDSTONE) {
            Stream.of(
                    BzItems.STICKY_HONEY_REDSTONE,
                    BzItems.REDSTONE_HONEY_WEB,
                    BzItems.STRING_CURTAIN_WHITE,
                    BzItems.ROYAL_JELLY_BLOCK
            ).map(item -> item.get().getDefaultInstance()).forEach(event::add);
        }

        if (event.type() == AddCreativeTabEntriesEvent.Type.FUNCTIONAL) {
            Stream.of(
                    BzItems.HONEYCOMB_BROOD,
                    BzItems.GLISTERING_HONEY_CRYSTAL,
                    BzItems.HONEY_COCOON,
                    BzItems.CRYSTALLINE_FLOWER,
                    BzItems.STICKY_HONEY_RESIDUE,
                    BzItems.HONEY_WEB,
                    BzItems.PILE_OF_POLLEN_SUSPICIOUS,
                    BzItems.INCENSE_CANDLE,
                    BzItems.SUPER_CANDLE,
                    BzItems.SUPER_CANDLE_WHITE,
                    BzItems.SUPER_CANDLE_LIGHT_GRAY,
                    BzItems.SUPER_CANDLE_GRAY,
                    BzItems.SUPER_CANDLE_BLACK,
                    BzItems.SUPER_CANDLE_BROWN,
                    BzItems.SUPER_CANDLE_RED,
                    BzItems.SUPER_CANDLE_ORANGE,
                    BzItems.SUPER_CANDLE_YELLOW,
                    BzItems.SUPER_CANDLE_LIME,
                    BzItems.SUPER_CANDLE_GREEN,
                    BzItems.SUPER_CANDLE_CYAN,
                    BzItems.SUPER_CANDLE_LIGHT_BLUE,
                    BzItems.SUPER_CANDLE_BLUE,
                    BzItems.SUPER_CANDLE_PURPLE,
                    BzItems.SUPER_CANDLE_MAGENTA,
                    BzItems.SUPER_CANDLE_PINK
            ).map(item -> item.get().getDefaultInstance()).forEach(event::add);
        }

        if (event.type() == AddCreativeTabEntriesEvent.Type.COLORED) {
            Stream.of(
                    BzItems.SUPER_CANDLE,
                    BzItems.SUPER_CANDLE_WHITE,
                    BzItems.SUPER_CANDLE_LIGHT_GRAY,
                    BzItems.SUPER_CANDLE_GRAY,
                    BzItems.SUPER_CANDLE_BLACK,
                    BzItems.SUPER_CANDLE_BROWN,
                    BzItems.SUPER_CANDLE_RED,
                    BzItems.SUPER_CANDLE_ORANGE,
                    BzItems.SUPER_CANDLE_YELLOW,
                    BzItems.SUPER_CANDLE_LIME,
                    BzItems.SUPER_CANDLE_GREEN,
                    BzItems.SUPER_CANDLE_CYAN,
                    BzItems.SUPER_CANDLE_LIGHT_BLUE,
                    BzItems.SUPER_CANDLE_BLUE,
                    BzItems.SUPER_CANDLE_PURPLE,
                    BzItems.SUPER_CANDLE_MAGENTA,
                    BzItems.SUPER_CANDLE_PINK,
                    BzItems.STRING_CURTAIN_WHITE,
                    BzItems.STRING_CURTAIN_LIGHT_GRAY,
                    BzItems.STRING_CURTAIN_GRAY,
                    BzItems.STRING_CURTAIN_BLACK,
                    BzItems.STRING_CURTAIN_BROWN,
                    BzItems.STRING_CURTAIN_RED,
                    BzItems.STRING_CURTAIN_ORANGE,
                    BzItems.STRING_CURTAIN_YELLOW,
                    BzItems.STRING_CURTAIN_LIME,
                    BzItems.STRING_CURTAIN_GREEN,
                    BzItems.STRING_CURTAIN_CYAN,
                    BzItems.STRING_CURTAIN_LIGHT_BLUE,
                    BzItems.STRING_CURTAIN_BLUE,
                    BzItems.STRING_CURTAIN_PURPLE,
                    BzItems.STRING_CURTAIN_MAGENTA,
                    BzItems.STRING_CURTAIN_PINK
            ).map(item -> item.get().getDefaultInstance()).forEach(event::add);
        }

        if (event.type() == AddCreativeTabEntriesEvent.Type.COMBAT) {
            Stream.of(
                    BzItems.BEE_STINGER,
                    BzItems.STINGER_SPEAR,
                    BzItems.BEE_CANNON,
                    BzItems.CRYSTAL_CANNON,
                    BzItems.HONEY_CRYSTAL_SHIELD,
                    BzItems.STINGLESS_BEE_HELMET_1,
                    BzItems.STINGLESS_BEE_HELMET_2,
                    BzItems.BUMBLE_BEE_CHESTPLATE_1,
                    BzItems.BUMBLE_BEE_CHESTPLATE_2,
                    BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1,
                    BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2,
                    BzItems.HONEY_BEE_LEGGINGS_1,
                    BzItems.HONEY_BEE_LEGGINGS_2,
                    BzItems.CARPENTER_BEE_BOOTS_1,
                    BzItems.CARPENTER_BEE_BOOTS_2
            ).map(item -> item.get().getDefaultInstance()).forEach(event::add);
        }

        if (event.type() == AddCreativeTabEntriesEvent.Type.SPAWN_EGGS) {
            Stream.of(
                    BzItems.VARIANT_BEE_SPAWN_EGG,
                    BzItems.HONEY_SLIME_SPAWN_EGG,
                    BzItems.BEEHEMOTH_SPAWN_EGG,
                    BzItems.BEE_QUEEN_SPAWN_EGG
            ).map(item -> item.get().getDefaultInstance()).forEach(event::add);
        }
    }
}
