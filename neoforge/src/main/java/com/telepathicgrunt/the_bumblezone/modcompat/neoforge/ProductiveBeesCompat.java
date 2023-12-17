package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.BroodBlockModdedCompatDispenseBehavior;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.common.block.ExpansionBox;
import cy.jdkdigital.productivebees.common.entity.bee.ConfigurableBee;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.init.ModEntities;
import cy.jdkdigital.productivebees.setup.BeeReloadListener;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductiveBeesCompat implements ModCompat {
//
//    private static final Lazy<List<String>> SPIDER_DUNGEON_HONEYCOMBS = Lazy.of(() ->
//            BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(e -> {
//                CompoundTag tag = e.getValue();
//                int primary = tag.getInt("primaryColor");
//                return BzModCompatibilityConfigs.allowedCombsForDungeons.contains(e.getKey()) &&
//                        tag.getBoolean("createComb") &&
//                        (colorsAreClose(GeneralUtils.colorToInt(106, 127, 0), primary, 150) ||
//                        colorsAreClose(GeneralUtils.colorToInt(129, 198, 0), primary, 150) ||
//                        colorsAreClose(GeneralUtils.colorToInt(34, 45, 0), primary, 150));
//            }).map(Map.Entry::getKey).toList());
//
//    private static final Lazy<List<String>> BEE_DUNGEON_HONEYCOMBS = Lazy.of(() ->
//            BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(e -> {
//                CompoundTag tag = e.getValue();
//                return BzModCompatibilityConfigs.allowedCombsForDungeons.contains(e.getKey()) &&
//                        tag.getBoolean("createComb") &&
//                        !SPIDER_DUNGEON_HONEYCOMBS.get().contains(e.getKey());
//            }).map(Map.Entry::getKey).toList());
//
//    private static final Lazy<List<String>> ALL_BEES = Lazy.of(() -> BeeReloadListener.INSTANCE.getData().keySet().stream().filter(e -> BzModCompatibilityConfigs.allowedBees.contains(e)).toList());
//
//    public static final TagKey<Block> SOLITARY_OVERWORLD_NESTS_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation("productivebees", "solitary_overworld_nests"));
//
//    protected static Optional<Item> BEE_CAGE;
//    protected static Optional<Item> STURDY_BEE_CAGE;
//
//    public ProductiveBeesCompat() {
//        BEE_CAGE = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("productivebees", "bee_cage"));
//        STURDY_BEE_CAGE = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("productivebees", "sturdy_bee_cage"));
//
//        if (BEE_CAGE.isPresent() && BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock) {
//            setupDispenserCompat(BEE_CAGE.get()); // adds compatibility with bee cage in dispensers
//        }
//
//        if (STURDY_BEE_CAGE.isPresent() && BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock) {
//            setupDispenserCompat(STURDY_BEE_CAGE.get()); // adds compatibility with sturdy bee cage in dispensers
//        }
//
//        // Keep at end so it is only set to true if no exceptions was thrown during setup
//        ModChecker.productiveBeesPresent = true;
//    }
//    private static void setupDispenserCompat(Item containerItem) {
//        BroodBlockModdedCompatDispenseBehavior newDispenseBehavior = new BroodBlockModdedCompatDispenseBehavior(
//                ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(containerItem)),
//                (originalModdedDispenseBehavior, blockSource, itemStack, serverLevel, blockPos, blockState) -> {
//                    serverLevel.setBlockAndUpdate(blockPos, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
//                            .setValue(HoneycombBrood.FACING, blockState.getValue(EmptyHoneycombBrood.FACING))
//                            .setValue(HoneycombBrood.STAGE, ProductiveBeesCompat.isFilledBabyBeeCageItem(itemStack) ? 2 : 3));
//
//                    boolean isSturdy = ProductiveBeesCompat.STURDY_BEE_CAGE.isPresent() && itemStack.is(ProductiveBeesCompat.STURDY_BEE_CAGE.get());
//                    itemStack.shrink(1);
//
//                    if (!itemStack.isEmpty()) {
//                        if (blockSource.getEntity() instanceof DispenserBlockEntity) {
//                            DispenserBlockEntity dispenser = blockSource.getEntity();
//                            ItemStack emptyCage = ItemStack.EMPTY;
//                            if (isSturdy && ProductiveBeesCompat.STURDY_BEE_CAGE.isPresent()) {
//                                emptyCage = new ItemStack(ProductiveBeesCompat.STURDY_BEE_CAGE.get());
//                            }
//                            else if (ProductiveBeesCompat.BEE_CAGE.isPresent()) {
//                                emptyCage = new ItemStack(ProductiveBeesCompat.BEE_CAGE.get());
//                            }
//
//                            if (!HopperBlockEntity.addItem(null, dispenser, emptyCage, null).isEmpty()) {
//                                BroodBlockModdedCompatDispenseBehavior.DEFAULT_DROP_ITEM_BEHAVIOR.dispense(blockSource, emptyCage);
//                            }
//                        }
//                    }
//                    else {
//                        if (isSturdy) {
//                            itemStack = new ItemStack(ProductiveBeesCompat.STURDY_BEE_CAGE.get());
//                        }
//                        else if (BEE_CAGE.isPresent()) {
//                            itemStack = new ItemStack(ProductiveBeesCompat.BEE_CAGE.get());
//                        }
//                    }
//
//                    return itemStack;
//                }
//        );
//
//        DispenserBlock.registerBehavior(containerItem, newDispenseBehavior);
//    }
//
//    @Override
//    public EnumSet<Type> compatTypes() {
//        return EnumSet.of(Type.SPAWNS, Type.COMBS, Type.HIVE_TELEPORT, Type.COMB_ORE, Type.EMPTY_BROOD, Type.BEE_COLOR);
//    }
//
//    private static boolean colorsAreClose(int a, int z, int threshold) {
//        int r = GeneralUtils.getRed(a) - GeneralUtils.getRed(z);
//        int g = GeneralUtils.getGreen(a) - GeneralUtils.getGreen(z);
//        int b = GeneralUtils.getBlue(a) - GeneralUtils.getBlue(z);
//        return (r * r + g * g + b * b) <= threshold * threshold;
//    }
//
//    @Override
//    public boolean isValidBeeHiveForTeleportation(BlockState state) {
//        if (state.getBlock() instanceof ExpansionBox && state.getValue(AdvancedBeehive.EXPANDED) != VerticalHive.NONE) {
//            return true; // expansion boxes only count as beenest when they expand a hive.
//        }
//        else if (state.is(SOLITARY_OVERWORLD_NESTS_TAG)) {
//            // Solitary nests are technically AdvancedBeehiveAbstract and will pass the next check.
//            // But this is still done in case they do change that in the future to extend something else or something.
//            return true;
//        }
//        else {
//            return state.getBlock() instanceof AdvancedBeehiveAbstract; // all other nests/hives we somehow missed here so return true
//        }
//    }
//
//    @Override
//    public boolean onBeeSpawn(EntitySpawnEvent event, boolean isChild) {
//        if (!BzModCompatibilityConfigs.spawnProductiveBeesBeesMob) {
//            return false;
//        }
//
//        if (event.entity().getRandom().nextFloat() >= BzModCompatibilityConfigs.spawnrateOfProductiveBeesMobs) {
//            return false;
//        }
//
//        if (event.spawnType() == MobSpawnType.DISPENSER && !BzModCompatibilityConfigs.allowProductiveBeesSpawnFromDispenserFedBroodBlock) {
//            return false;
//        }
//
//        if (ALL_BEES.get().size() == 0) {
//            return false;
//        }
//
//        Mob entity = event.entity();
//        LevelAccessor world = event.level();
//
//        // randomly pick a productive bee (the nbt determines the bee)
//        ConfigurableBee productiveBeeEntity = ModEntities.CONFIGURABLE_BEE.get().create(entity.level());
//        if (productiveBeeEntity == null) {
//            return false;
//        }
//
//        productiveBeeEntity.moveTo(
//                entity.getX(),
//                entity.getY(),
//                entity.getZ(),
//                productiveBeeEntity.getRandom().nextFloat() * 360.0F,
//                0.0F);
//
//        productiveBeeEntity.setBaby(isChild);
//
//        CompoundTag newTag = new CompoundTag();
//        newTag.putString("type", ALL_BEES.get().get(productiveBeeEntity.getRandom().nextInt(ALL_BEES.get().size())));
//        productiveBeeEntity.finalizeSpawn(
//                (ServerLevelAccessor) world,
//                world.getCurrentDifficultyAt(productiveBeeEntity.blockPosition()),
//                event.spawnType(),
//                null,
//                newTag);
//        productiveBeeEntity.setBeeType(newTag.getString("type"));
//
//        world.addFreshEntity(productiveBeeEntity);
//        return true;
//    }
//
//    @Override
//    public OptionalBoolean validateCombType(CompoundTag tag) {
//        if (tag.contains("type")) {
//            CompoundTag productiveBeesData = BeeReloadListener.INSTANCE.getData().get(tag.getString("type"));
//            if (productiveBeesData != null && productiveBeesData.getBoolean("createComb")) {
//                return OptionalBoolean.TRUE;
//            }
//        }
//
//        return OptionalBoolean.EMPTY;
//    }
//
//    @Override
//    public boolean checkCombSpawn(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
//        if (spiderDungeon) {
//            return random.nextFloat() < BzModCompatibilityConfigs.PBOreHoneycombSpawnRateSpiderBeeDungeon;
//        }
//        return random.nextFloat() < BzModCompatibilityConfigs.PBOreHoneycombSpawnRateBeeDungeon;
//    }
//
//    @Override
//    public StructureTemplate.StructureBlockInfo getHoneycomb(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
//        if (spiderDungeon) {
//            return PBGetRottenedHoneycomb(pos, random);
//        } else {
//            return PBGetRandomHoneycomb(pos, random);
//        }
//    }
//
//    /**
//     * Safely get Rottened Honeycomb. If Rottened Honeycomb wasn't found, return
//     * Vanilla's Honeycomb
//     */
//    public static StructureTemplate.StructureBlockInfo PBGetRottenedHoneycomb(BlockPos worldPos, RandomSource random) {
//        if (!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants || SPIDER_DUNGEON_HONEYCOMBS.get().size() == 0) {
//            return null;
//        } else {
//            CompoundTag newTag = new CompoundTag();
//            newTag.putString("type", SPIDER_DUNGEON_HONEYCOMBS.get().get(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.get().size())));
//            return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
//        }
//    }
//
//    /**
//     * Picks a random Productive Bees Honeycomb with lower index of
//     * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
//     */
//    public static StructureTemplate.StructureBlockInfo PBGetRandomHoneycomb(BlockPos worldPos, RandomSource random) {
//        if (!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants || BEE_DUNGEON_HONEYCOMBS.get().size() == 0) {
//            return null;
//        } else {
//            CompoundTag newTag = new CompoundTag();
//            newTag.putString("type", BEE_DUNGEON_HONEYCOMBS.get().get(random.nextInt(BEE_DUNGEON_HONEYCOMBS.get().size())));
//            return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
//        }
//    }
//
//    public static boolean isFilledBeeCageItem(ItemStack stack) {
//        return ((BEE_CAGE.isPresent() && stack.is(BEE_CAGE.get())) || (STURDY_BEE_CAGE.isPresent() && stack.is(STURDY_BEE_CAGE.get()))) &&
//                !stack.isEmpty() && stack.hasTag() && stack.getOrCreateTag().contains("entity");
//    }
//
//    public static boolean isFilledBabyBeeCageItem(ItemStack stack) {
//        return isFilledBeeCageItem(stack) && stack.getOrCreateTag().getInt("Age") < 0;
//    }
//
//    @Override
//    public InteractionResult onEmptyBroodInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
//        if (!BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock) return InteractionResult.PASS;
//        if (isFilledBeeCageItem(itemstack)) {
//            if (!playerEntity.isCrouching()) {
//                if (!playerEntity.getAbilities().instabuild) {
//                    ItemStack itemToGive = ItemStack.EMPTY;
//
//                    if (ProductiveBeesCompat.STURDY_BEE_CAGE.isPresent() && itemstack.is(ProductiveBeesCompat.STURDY_BEE_CAGE.get())) {
//                        itemToGive = ProductiveBeesCompat.STURDY_BEE_CAGE.get().getDefaultInstance();
//                    }
//                    else if (ProductiveBeesCompat.BEE_CAGE.isPresent() && itemstack.is(ProductiveBeesCompat.BEE_CAGE.get())) {
//                        itemToGive = ProductiveBeesCompat.BEE_CAGE.get().getDefaultInstance();
//                    }
//
//                    GeneralUtils.givePlayerItem(
//                            playerEntity,
//                            playerHand,
//                            itemToGive,
//                            true,
//                            true);
//                }
//
//                return isFilledBabyBeeCageItem(itemstack) ? InteractionResult.CONSUME_PARTIAL : InteractionResult.SUCCESS;
//            }
//        }
//
//        return InteractionResult.FAIL;
//    }
//
//    @Override
//    public Pair<Integer, Integer> getModdedBeePrimaryAndSecondaryColors(Entity entity) {
//        if (entity instanceof ConfigurableBee configurableBee && !configurableBee.hasBeeTexture()) {
//            CompoundTag nbt = configurableBee.getNBTData();
//            int primary = 0xE59900;
//            int secondary = 0x231100;
//
//            if (nbt.contains("primaryColor")) {
//                primary = nbt.getInt("primaryColor");
//            }
//            if (nbt.contains("secondaryColor")) {
//                secondary = nbt.getInt("secondaryColor");
//            }
//
//            return Pair.of(primary, secondary);
//        }
//
//        return null;
//    }
}
