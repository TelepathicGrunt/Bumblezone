package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.BeeBread;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.items.BzBlockItem;
import com.telepathicgrunt.the_bumblezone.items.BzHoneyCrystalBlockItem;
import com.telepathicgrunt.the_bumblezone.items.BzMusicDiscs;
import com.telepathicgrunt.the_bumblezone.items.BzSmartBucket;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import com.telepathicgrunt.the_bumblezone.items.PollenPuff;
import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.items.SugarWaterBottle;
import com.telepathicgrunt.the_bumblezone.items.materials.BeeArmorMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;


public class BzItems {
    /**
     * creative tab to hold our block items
     */
    public static final CreativeModeTab BUMBLEZONE_CREATIVE_TAB = QuiltItemGroup
            .builder(new ResourceLocation(Bumblezone.MODID, "main_tab"))
            .icon(() -> new ItemStack(BzBlocks.FILLED_POROUS_HONEYCOMB))
            .build();

    //blocks
    public static final Item POROUS_HONEYCOMB = new BlockItem(BzBlocks.POROUS_HONEYCOMB, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item FILLED_POROUS_HONEYCOMB = new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item EMPTY_HONEYCOMB_BROOD = new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEYCOMB_BROOD = new BlockItem(BzBlocks.HONEYCOMB_BROOD, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item SUGAR_INFUSED_STONE = new BlockItem(BzBlocks.SUGAR_INFUSED_STONE, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item SUGAR_INFUSED_COBBLESTONE = new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item BEEHIVE_BEESWAX = new BlockItem(BzBlocks.BEEHIVE_BEESWAX, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STICKY_HONEY_RESIDUE = new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STICKY_HONEY_REDSTONE = new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEY_WEB = new BlockItem(BzBlocks.HONEY_WEB, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item REDSTONE_HONEY_WEB = new BlockItem(BzBlocks.REDSTONE_HONEY_WEB, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item PILE_OF_POLLEN = new BlockItem(BzBlocks.PILE_OF_POLLEN, new Item.Properties());
    public static final Item HONEY_CRYSTAL = new BzHoneyCrystalBlockItem(BzBlocks.HONEY_CRYSTAL, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEY_COCOON = new BzBlockItem(BzBlocks.HONEY_COCOON, new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB), false);

    //items
    public static final Item HONEY_CRYSTAL_SHARDS = new Item(new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)
            .food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.15F).build()));

    public static final Item HONEY_CRYSTAL_SHIELD = new HoneyCrystalShield();

    public static final Item HONEY_SLIME_SPAWN_EGG = new SpawnEggItem(
            BzEntities.HONEY_SLIME, 0xFFCC00, 0xFCA800, (new Item.Properties()).tab(BUMBLEZONE_CREATIVE_TAB));

    public static final Item BEEHEMOTH_SPAWN_EGG = new SpawnEggItem(
            BzEntities.BEEHEMOTH, 0xEDC343, 0x43241B, (new Item.Properties()).tab(BUMBLEZONE_CREATIVE_TAB));

    public static final Item SUGAR_WATER_BUCKET = new BzSmartBucket(BzFluids.SUGAR_WATER_FLUID, new Item.Properties()
            .craftRemainder(Items.BUCKET).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB));

    public static final Item SUGAR_WATER_BOTTLE = new SugarWaterBottle((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE)
            .food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.05F).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0), 1.0F).build())
            .tab(BUMBLEZONE_CREATIVE_TAB).stacksTo(16));

    public static final Item MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = new BzMusicDiscs(14, BzSounds.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV, (new Item.Properties()).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB).rarity(Rarity.RARE));
    public static final Item MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = new BzMusicDiscs(15, BzSounds.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY, (new Item.Properties()).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB).rarity(Rarity.RARE));
    public static final Item POLLEN_PUFF = new PollenPuff(new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB).stacksTo(16));
    public static final Item BEE_BREAD = new BeeBread(new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.12F).alwaysEat().effect(new MobEffectInstance(BzEffects.BEENERGIZED, 6000, 0), 1.0F).effect(new MobEffectInstance(MobEffects.CONFUSION, 120, 1), 1.0F).build()).tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEY_BUCKET = new BucketItem(BzFluids.HONEY_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STINGER_SPEAR = new StingerSpearItem(new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB));
    public static final HoneyBeeLeggings HONEY_BEE_LEGGINGS_1 = new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 1);
    public static final HoneyBeeLeggings HONEY_BEE_LEGGINGS_2 = new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 2);
    public static final BumbleBeeChestplate BUMBLE_BEE_CHESTPLATE_1 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), false, 1);
    public static final BumbleBeeChestplate BUMBLE_BEE_CHESTPLATE_2 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), false, 2);
    public static final BumbleBeeChestplate TRANS_BUMBLE_BEE_CHESTPLATE_1 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), true, 1);
    public static final BumbleBeeChestplate TRANS_BUMBLE_BEE_CHESTPLATE_2 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), true, 2);
    public static final StinglessBeeHelmet STINGLESS_BEE_HELMET_1 = new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 1);
    public static final StinglessBeeHelmet STINGLESS_BEE_HELMET_2 = new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 2);

    /**
     * registers the item version of the Blocks so they now exist in the registry
     */
    public static void registerItems() {
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "empty_honeycomb_brood_block"), EMPTY_HONEYCOMB_BROOD);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honeycomb_brood_block"), HONEYCOMB_BROOD);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), SUGAR_INFUSED_STONE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), SUGAR_INFUSED_COBBLESTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "beehive_beeswax"), BEEHIVE_BEESWAX);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), STICKY_HONEY_RESIDUE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), STICKY_HONEY_REDSTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_web"), HONEY_WEB);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "redstone_honey_web"), REDSTONE_HONEY_WEB);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "pile_of_pollen"), PILE_OF_POLLEN);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal"), HONEY_CRYSTAL);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_cocoon"), HONEY_COCOON);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shards"), HONEY_CRYSTAL_SHARDS);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield"), HONEY_CRYSTAL_SHIELD);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_water_bucket"), SUGAR_WATER_BUCKET);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_water_bottle"), SUGAR_WATER_BOTTLE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_slime_spawn_egg"), HONEY_SLIME_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "beehemoth_spawn_egg"), BEEHEMOTH_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "music_disc_flight_of_the_bumblebee_rimsky_korsakov"), MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "music_disc_honey_bee_rat_faced_boy"), MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN_PUFF);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "bee_bread"), BEE_BREAD);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_bucket"), HONEY_BUCKET);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "stinger_spear"), STINGER_SPEAR);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_bee_leggings_1"), HONEY_BEE_LEGGINGS_1);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_bee_leggings_2"), HONEY_BEE_LEGGINGS_2);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_1"), BUMBLE_BEE_CHESTPLATE_1);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_2"), BUMBLE_BEE_CHESTPLATE_2);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_trans_1"), TRANS_BUMBLE_BEE_CHESTPLATE_1);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_trans_2"), TRANS_BUMBLE_BEE_CHESTPLATE_2);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_1"), STINGLESS_BEE_HELMET_1);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_2"), STINGLESS_BEE_HELMET_2);
    }
}