package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.Lazy;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductiveBeesCompat implements ModCompat {

    private static final Lazy<List<String>> SPIDER_DUNGEON_HONEYCOMBS = Lazy.of(() ->
            BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(e -> {
                CompoundTag tag = e.getValue();
                int primary = tag.getInt("primaryColor");
                return BzModCompatibilityConfigs.allowedCombsForDungeons.contains(e.getKey()) &&
                        tag.getBoolean("createComb") &&
                        (colorsAreClose(new Color(106, 127, 0), new Color(primary), 150) ||
                                colorsAreClose(new Color(129, 198, 0), new Color(primary), 150) ||
                                colorsAreClose(new Color(34, 45, 0), new Color(primary), 150));
            }).map(Map.Entry::getKey).toList());

    private static final Lazy<List<String>> BEE_DUNGEON_HONEYCOMBS = Lazy.of(() ->
            BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(e -> {
                CompoundTag tag = e.getValue();
                return BzModCompatibilityConfigs.allowedCombsForDungeons.contains(e.getKey()) &&
                        tag.getBoolean("createComb") &&
                        !SPIDER_DUNGEON_HONEYCOMBS.get().contains(e.getKey());
            }).map(Map.Entry::getKey).toList());

    private static final Lazy<List<String>> ALL_BEES = Lazy.of(() -> BeeReloadListener.INSTANCE.getData().keySet().stream().filter(e -> BzModCompatibilityConfigs.allowedBees.contains(e)).toList());

    public static final TagKey<Block> SOLITARY_OVERWORLD_NESTS_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation("productivebees", "solitary_overworld_nests"));

    protected static Optional<Item> BEE_CAGE;
    protected static Optional<Item> STURDY_BEE_CAGE;

    public ProductiveBeesCompat() {
        BEE_CAGE = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("productivebees", "bee_cage"));
        STURDY_BEE_CAGE = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("productivebees", "sturdy_bee_cage"));

        if (BEE_CAGE.isPresent() && BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock) {
            ProductiveBeesDispenseBehavior.DEFAULT_BEE_CAGED_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(BEE_CAGE.get()));
            DispenserBlock.registerBehavior(BEE_CAGE.get(), new ProductiveBeesDispenseBehavior()); // adds compatibility with caged bee in dispensers
        }

        if (STURDY_BEE_CAGE.isPresent() && BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock) {
            ProductiveBeesDispenseBehavior.DEFAULT_STURDY_BEE_CAGED_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(STURDY_BEE_CAGE.get()));
            DispenserBlock.registerBehavior(STURDY_BEE_CAGE.get(), new ProductiveBeesDispenseBehavior()); // adds compatibility with caged bee in dispensers
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.productiveBeesPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.SPAWNS, Type.COMBS, Type.BLOCK_TELEPORT, Type.COMB_ORE, Type.EMPTY_BROOD);
    }

    private static boolean colorsAreClose(Color a, Color z, int threshold) {
        int r = a.getRed() - z.getRed();
        int g = a.getGreen() - z.getGreen();
        int b = a.getBlue() - z.getBlue();
        return (r * r + g * g + b * b) <= threshold * threshold;
    }

    @Override
    public boolean isValidBeeHiveForTeleportation(BlockState state) {
        if (state.getBlock() instanceof ExpansionBox && state.getValue(AdvancedBeehive.EXPANDED) != VerticalHive.NONE) {
            return true; // expansion boxes only count as beenest when they expand a hive.
        } else if (state.is(SOLITARY_OVERWORLD_NESTS_TAG)) {
            // Solitary nests are technically AdvancedBeehiveAbstract and will pass the next check.
            // But this is still done in case they do change that in the future to extend something else or something.
            return true;
        } else {
            return state.getBlock() instanceof AdvancedBeehiveAbstract; // all other nests/hives we somehow missed here so return true
        }
    }

    @Override
    public boolean onBeeSpawn(EntitySpawnEvent event, boolean isChild) {
        if (!BzModCompatibilityConfigs.spawnProductiveBeesBeesMob) return false;
        if (event.entity().getRandom().nextFloat() >= BzModCompatibilityConfigs.spawnrateOfProductiveBeesMobs)
            return false;
        if (ALL_BEES.get().size() == 0) return false;

        Mob entity = event.entity();
        LevelAccessor world = event.level();

        // randomly pick a productive bee (the nbt determines the bee)
        ConfigurableBee productiveBeeEntity = ModEntities.CONFIGURABLE_BEE.get().create(entity.level);
        if (productiveBeeEntity == null) return false;

        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
        productiveBeeEntity.moveTo(
                blockpos.getX() + 0.5f,
                blockpos.getY() + 0.5f,
                blockpos.getZ() + 0.5f,
                productiveBeeEntity.getRandom().nextFloat() * 360.0F,
                0.0F);

        productiveBeeEntity.setBaby(isChild);

        CompoundTag newTag = new CompoundTag();
        newTag.putString("type", ALL_BEES.get().get(productiveBeeEntity.getRandom().nextInt(ALL_BEES.get().size())));
        productiveBeeEntity.finalizeSpawn(
                (ServerLevelAccessor) world,
                world.getCurrentDifficultyAt(productiveBeeEntity.blockPosition()),
                event.spawnType(),
                null,
                newTag);
        productiveBeeEntity.setBeeType(newTag.getString("type"));

        world.addFreshEntity(productiveBeeEntity);
        return true;
    }

    @Override
    public OptionalBoolean validateCombType(CompoundTag tag) {
        if (tag.contains("type")) {
            CompoundTag productiveBeesData = BeeReloadListener.INSTANCE.getData().get(tag.getString("type"));
            if (productiveBeesData != null && productiveBeesData.getBoolean("createComb")) {
                return OptionalBoolean.TRUE;
            }
        }

        return OptionalBoolean.EMPTY;
    }

    @Override
    public boolean checkCombSpawn(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
        if (spiderDungeon) {
            return random.nextFloat() < BzModCompatibilityConfigs.PBOreHoneycombSpawnRateSpiderBeeDungeon;
        }
        return random.nextFloat() < BzModCompatibilityConfigs.PBOreHoneycombSpawnRateBeeDungeon;
    }

    @Override
    public StructureTemplate.StructureBlockInfo getHoneycomb(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
        if (spiderDungeon) {
            return PBGetRottenedHoneycomb(pos, random);
        } else {
            return PBGetRandomHoneycomb(pos, random);
        }
    }

    /**
     * Safely get Rottened Honeycomb. If Rottened Honeycomb wasn't found, return
     * Vanilla's Honeycomb
     */
    public static StructureTemplate.StructureBlockInfo PBGetRottenedHoneycomb(BlockPos worldPos, RandomSource random) {
        if (!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants || SPIDER_DUNGEON_HONEYCOMBS.get().size() == 0) {
            return null;
        } else {
            CompoundTag newTag = new CompoundTag();
            newTag.putString("type", SPIDER_DUNGEON_HONEYCOMBS.get().get(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.get().size())));
            return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
        }
    }

    /**
     * Picks a random Productive Bees Honeycomb with lower index of
     * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
     */
    public static StructureTemplate.StructureBlockInfo PBGetRandomHoneycomb(BlockPos worldPos, RandomSource random) {
        if (!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants || BEE_DUNGEON_HONEYCOMBS.get().size() == 0) {
            return null;
        } else {
            CompoundTag newTag = new CompoundTag();
            newTag.putString("type", BEE_DUNGEON_HONEYCOMBS.get().get(random.nextInt(BEE_DUNGEON_HONEYCOMBS.get().size())));
            return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
        }
    }

    public static boolean isFilledBeeCageItem(ItemStack stack) {
        return ((BEE_CAGE.isPresent() && stack.is(BEE_CAGE.get())) || (STURDY_BEE_CAGE.isPresent() && stack.is(STURDY_BEE_CAGE.get()))) &&
                !stack.isEmpty() && stack.hasTag() && stack.getOrCreateTag().contains("entity");
    }

    @Override
    public InteractionResult onEmptyBroodInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        if (!BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock) return InteractionResult.PASS;
        if (isFilledBeeCageItem(itemstack)) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    ItemStack itemToGive = ItemStack.EMPTY;

                    if (ProductiveBeesCompat.STURDY_BEE_CAGE.isPresent() && itemstack.is(ProductiveBeesCompat.STURDY_BEE_CAGE.get())) {
                        itemToGive = ProductiveBeesCompat.STURDY_BEE_CAGE.get().getDefaultInstance();
                    }
                    else if (ProductiveBeesCompat.BEE_CAGE.isPresent() && itemstack.is(ProductiveBeesCompat.BEE_CAGE.get())) {
                        itemToGive = ProductiveBeesCompat.BEE_CAGE.get().getDefaultInstance();
                    }

                    GeneralUtils.givePlayerItem(
                            playerEntity,
                            playerHand,
                            itemToGive,
                            true,
                            true);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }
}
