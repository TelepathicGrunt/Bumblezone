package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.datacomponents.HoneyCompassBaseData;
import com.telepathicgrunt.the_bumblezone.datacomponents.HoneyCompassStateData;
import com.telepathicgrunt.the_bumblezone.datacomponents.HoneyCompassTargetData;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HoneyCompass extends Item {
    public HoneyCompass(Item.Properties properties) {
        super(properties
                .component(BzDataComponents.HONEY_COMPASS_BASE_DATA.get(), new HoneyCompassBaseData())
                .component(BzDataComponents.HONEY_COMPASS_STATE_DATA.get(), new HoneyCompassStateData())
                .component(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get(), new HoneyCompassTargetData()));
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get()) == null) {
            itemStack.set(BzDataComponents.HONEY_COMPASS_BASE_DATA.get(), new HoneyCompassBaseData());
        }
        if (itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get()) == null) {
            itemStack.set(BzDataComponents.HONEY_COMPASS_STATE_DATA.get(), new HoneyCompassStateData());
        }
        if (itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get()) == null) {
            itemStack.set(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get(), new HoneyCompassTargetData());
        }
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
        return honeyCompassStateData.locked() || super.isFoil(itemStack);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
        if (honeyCompassStateData.isLoading()) {
            return "item.the_bumblezone.honey_compass_structure_loading";
        }

        if (honeyCompassStateData.isFailed()) {
            return "item.the_bumblezone.honey_compass_structure_failed";
        }

        HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
        if (honeyCompassBaseData.customName().isPresent()) {
            return honeyCompassBaseData.customName().get();
        }

        if (honeyCompassBaseData.isStructureCompass()) {
            return "item.the_bumblezone.honey_compass_structure";
        }

        if (honeyCompassBaseData.isBlockCompass()) {
            return "item.the_bumblezone.honey_compass_block";
        }

        return super.getDescriptionId(itemStack);
    }

    public Component getName(ItemStack itemStack) {
        HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
        if (honeyCompassBaseData.isBlockCompass()) {
            HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
            Optional<Block> block = honeyCompassTargetData.getStoredBlock();
            if (block.isPresent() && block.get() != Blocks.AIR) {
                return Component.translatable(this.getDescriptionId(itemStack), block.get().getName());
            }
            return Component.translatable(this.getDescriptionId(itemStack), Component.translatable("item.the_bumblezone.honey_compass_unknown_block"));
        }
        return Component.translatable(this.getDescriptionId(itemStack));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> components, TooltipFlag tooltipFlag) {
        HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
        if (honeyCompassStateData.isFailed()) {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_structure_failed_description"));
            return;
        }

        HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
        if (honeyCompassBaseData.customDescription().isPresent()) {
            components.add(Component.translatable(honeyCompassBaseData.customDescription().get()));
            appendAdvancedTooltipInfo(itemStack, tooltipContext, components, tooltipFlag);
            return;
        }

        if (honeyCompassBaseData.isBlockCompass()) {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description1"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description2"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description3"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description4"));
        }
        else if (honeyCompassBaseData.isStructureCompass()) {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_structure_description1"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_structure_description2"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_structure_description3"));
        }
        else {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_description1"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_description2"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_description3"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_description4"));
        }

        appendAdvancedTooltipInfo(itemStack, tooltipContext, components, tooltipFlag);
    }

    private static void appendAdvancedTooltipInfo(ItemStack itemStack, TooltipContext tooltipContext, List<Component> components, TooltipFlag tooltipFlag) {
        if (tooltipContext != null && PlatformHooks.isClientEnvironment()) {
            Player player = GeneralUtilsClient.getClientPlayer();
            HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
            if (player != null && tooltipFlag.isAdvanced() && (honeyCompassBaseData.isBlockCompass() || honeyCompassBaseData.isStructureCompass())) {
                HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
                Optional<BlockPos> targetPos = honeyCompassTargetData.targetPos();
                Optional<ResourceKey<Level>> storedDimension = honeyCompassTargetData.targetDimension();
                if (targetPos.isPresent() && storedDimension.isPresent() && player.level().dimension().equals(storedDimension.get())) {
                    int distance;
                    if (honeyCompassBaseData.isStructureCompass()) {
                        float xDist = Math.abs(player.blockPosition().getX() - targetPos.get().getX());
                        float zDist = Math.abs(player.blockPosition().getZ() - targetPos.get().getZ());
                        distance = (int) (xDist + zDist);
                    }
                    else {
                        distance = player.blockPosition().distManhattan(targetPos.get());
                    }
                    components.add(Component.translatable("item.the_bumblezone.honey_compass_distance", distance).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (!level.isClientSide) {
            HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
            boolean locked = honeyCompassStateData.locked();
            Optional<UUID> searchId = honeyCompassStateData.searchId();
            boolean isLoading = honeyCompassStateData.isLoading();
            boolean isFailed = honeyCompassStateData.isFailed();
            boolean locatedSpecialStructure = honeyCompassStateData.locatedSpecialStructure();

            HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
            String compassType = honeyCompassBaseData.compassType();

            HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
            Optional<BlockPos> targetPos = honeyCompassTargetData.targetPos();
            Optional<ResourceKey<Level>> targetDimension = honeyCompassTargetData.targetDimension();
            Optional<String> targetBlock = honeyCompassTargetData.targetBlock();
            Optional<String> targetStructureTag = honeyCompassTargetData.targetStructureTag();

            if (level.getGameTime() % 20 == 0) {
                if (searchId.isPresent()) {
                    // Location was found and already saved.
                    if (targetPos.isPresent()) {
                        ThreadExecutor.removeSearchResult(searchId.get());
                        searchId = Optional.empty();
                    }
                    else {
                        Optional<BlockPos> searchResult = ThreadExecutor.getSearchResult(searchId.get());
                        // null return mean no search queued up for this compass
                        if (searchResult == null) {
                            isFailed = true;
                        }
                        else if (searchResult.isPresent()) {
                            BlockPos newPos = searchResult.get();

                            compassType = "structure";

                            targetPos = Optional.of(newPos);
                            targetDimension = Optional.of(level.dimension());
                            targetBlock = Optional.empty();
                            targetStructureTag = Optional.empty();

                            isLoading = false;
                            isFailed = false;
                        }
                    }
                }
            }

            if (!isFailed && !isLoading && targetStructureTag.isPresent() && targetPos.isEmpty()) {
                isFailed = true;
            }

            if (isLoading && !ThreadExecutor.isRunningASearch() && !ThreadExecutor.hasQueuedSearch()) {
                isLoading = false;
                isFailed = true;
            }

            if (honeyCompassBaseData.isBlockCompass()) {
                if (targetBlock.isPresent() && targetPos.isPresent() && targetDimension.isPresent()) {
                    if (targetDimension.get().equals(level.dimension())) {
                        if (!level.isInWorldBounds(targetPos.get())) {
                            targetPos = Optional.empty();
                            targetDimension = Optional.empty();
                            targetBlock = Optional.empty();
                            compassType = "";

                            setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
                            setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);
                            return;
                        }

                        ChunkAccess chunk = level.getChunk(targetPos.get().getX() >> 4, targetPos.get().getZ() >> 4, ChunkStatus.FULL, false);
                        if(chunk != null && !(BuiltInRegistries.BLOCK.getKey(chunk.getBlockState(targetPos.get()).getBlock()).toString().equals(targetBlock.get()))) {
                            targetPos = Optional.empty();
                            targetDimension = Optional.empty();
                            targetBlock = Optional.empty();
                            compassType = "";
                        }
                    }
                }
                else {
                    targetPos = Optional.empty();
                    targetDimension = Optional.empty();
                    targetBlock = Optional.empty();
                    compassType = "";
                }
            }

            setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
            setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
            setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)  {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockPos playerPos = player.blockPosition();

        HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
        boolean locked = honeyCompassStateData.locked();
        Optional<UUID> searchId = honeyCompassStateData.searchId();
        boolean isLoading = honeyCompassStateData.isLoading();
        boolean isFailed = honeyCompassStateData.isFailed();
        boolean locatedSpecialStructure = honeyCompassStateData.locatedSpecialStructure();

        HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
        String compassType = honeyCompassBaseData.compassType();

        HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
        Optional<BlockPos> targetPos = honeyCompassTargetData.targetPos();
        Optional<ResourceKey<Level>> targetDimension = honeyCompassTargetData.targetDimension();
        Optional<String> targetBlock = honeyCompassTargetData.targetBlock();
        Optional<String> targetStructureTag = honeyCompassTargetData.targetStructureTag();

        InteractionResultHolder<ItemStack> interactionResult = null;
        if (isFailed && targetStructureTag.isPresent()) {
            if (level instanceof ServerLevel serverLevel && serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
                TagKey<Structure> structureTagKey = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(targetStructureTag.get()));
                Optional<HolderSet.Named<Structure>> optional = serverLevel.registryAccess().registry(Registries.STRUCTURE).flatMap(registry -> registry.getTag(structureTagKey));
                boolean structureExists = optional.isPresent() && optional.get().stream().anyMatch(structureHolder -> !serverLevel.getChunkSource().getGeneratorState().getPlacementsForStructure(structureHolder).isEmpty());
                if (structureExists) {
                    isLoading = true;
                    isFailed = false;
                    ThreadExecutor.locate((ServerLevel) level, structureTagKey, playerPos, 100, false)
                            .thenOnServerThread(foundPos -> setCompassData((ServerLevel) level, (ServerPlayer) player, interactionHand, itemStack, foundPos));
                    interactionResult = InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
                }
                else {
                    player.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_wrong_dimension"), true);
                    interactionResult = InteractionResultHolder.pass(itemStack);
                }
            }
        }

        if (interactionResult != null) {
            setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
            setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
            setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);
            return interactionResult;
        }

        if (isLoading) {
            if (ThreadExecutor.isRunningASearch() || ThreadExecutor.hasQueuedSearch()) {
                setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
                setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
                setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);
                return InteractionResultHolder.fail(itemStack);
            }
            else {
                isLoading = false;
            }
        }

        if (locked) {
            return super.use(level, player, interactionHand);
        }

        if (level instanceof ServerLevel serverLevel && !honeyCompassBaseData.isStructureCompass()) {
            Optional<HolderSet.Named<Structure>> optional = serverLevel.registryAccess().registry(Registries.STRUCTURE).flatMap(registry -> registry.getTag(BzTags.HONEY_COMPASS_DEFAULT_LOCATING));
            boolean structureExists = optional.isPresent() && optional.get().stream().anyMatch(structureHolder -> !serverLevel.getChunkSource().getGeneratorState().getPlacementsForStructure(structureHolder).isEmpty());
            if (structureExists) {
                isLoading = true;
                ThreadExecutor.locate((ServerLevel) level, BzTags.HONEY_COMPASS_DEFAULT_LOCATING, playerPos, 100, false)
                        .thenOnServerThread(foundPos -> setCompassData((ServerLevel) level, (ServerPlayer) player, interactionHand, itemStack, foundPos));
                interactionResult = InteractionResultHolder.success(itemStack);
            }
            else {
                player.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_wrong_dimension"), true);
                interactionResult = InteractionResultHolder.pass(itemStack);
            }
        }

        setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
        setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
        setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);

        if (interactionResult != null) {
            return interactionResult;
        }
        return super.use(level, player, interactionHand);
    }


    private void setCompassData(ServerLevel serverLevel, ServerPlayer serverPlayer, InteractionHand interactionHand, ItemStack itemStack, BlockPos structurePos) {
        HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
        boolean locked = honeyCompassStateData.locked();
        Optional<UUID> searchId = honeyCompassStateData.searchId();
        boolean isLoading = false;
        boolean isFailed = honeyCompassStateData.isFailed();
        boolean locatedSpecialStructure = honeyCompassStateData.locatedSpecialStructure();

        HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
        String compassType = honeyCompassBaseData.compassType();

        HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());
        Optional<BlockPos> targetPos = honeyCompassTargetData.targetPos();
        Optional<ResourceKey<Level>> targetDimension = honeyCompassTargetData.targetDimension();
        Optional<String> targetBlock = honeyCompassTargetData.targetBlock();
        Optional<String> targetStructureTag = honeyCompassTargetData.targetStructureTag();

        serverLevel.playSound(null, serverPlayer.blockPosition(), BzSounds.HONEY_COMPASS_STRUCTURE_LOCK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        serverPlayer.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

        if (structurePos == null) {
            serverPlayer.swing(interactionHand);
            serverPlayer.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_failed"), false);
            return;
        }

        BzCriterias.HONEY_COMPASS_USE_TRIGGER.get().trigger(serverPlayer);
        boolean singleCompass = !serverPlayer.getAbilities().instabuild && itemStack.getCount() == 1;
        if (singleCompass) {
            compassType = "structure";

            targetPos = Optional.of(structurePos);
            targetDimension = Optional.of(serverLevel.dimension());
            targetBlock = Optional.empty();
            targetStructureTag = Optional.empty();

            isFailed = false;
        }
        else {
            ItemStack newCompass = itemStack.copyWithCount(1);
            if (!serverPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            else {
                isFailed = false;
                serverPlayer.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_creative").withStyle(ChatFormatting.YELLOW), true);
            }

            HoneyCompassStateData newHoneyCompassStateData = newCompass.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
            HoneyCompassBaseData newHoneyCompassBaseData = newCompass.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
            HoneyCompassTargetData newHoneyCompassTargetData = newCompass.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());

            setCompassStateData(newHoneyCompassStateData, newHoneyCompassStateData.locked(), Optional.empty(), false, false, newHoneyCompassStateData.locatedSpecialStructure(), newCompass);
            setCompassBaseData(newHoneyCompassBaseData, "structure", newCompass);
            setCompassTargetData(newHoneyCompassTargetData, Optional.empty(), Optional.empty(), Optional.of(structurePos), Optional.of(serverLevel.dimension()), newCompass);

            if (!serverPlayer.getInventory().add(newCompass)) {
                serverPlayer.drop(newCompass, false);
            }
        }

        setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
        setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
        setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockPos blockPos = useOnContext.getClickedPos();
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockState blockState = level.getBlockState(blockPos);

        HoneyCompassStateData honeyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
        boolean locked = honeyCompassStateData.locked();
        Optional<UUID> searchId = honeyCompassStateData.searchId();
        boolean isLoading = honeyCompassStateData.isLoading();
        boolean isFailed = honeyCompassStateData.isFailed();
        boolean locatedSpecialStructure = honeyCompassStateData.locatedSpecialStructure();

        if (isLoading) {
            if (ThreadExecutor.isRunningASearch() || ThreadExecutor.hasQueuedSearch()) {
                return InteractionResult.FAIL;
            }
            else {
                isLoading = false;
            }
        }

        if (locked) {
            setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
            return super.useOn(useOnContext);
        }

        if (player != null && isValidBeeHive(blockState)) {
            level.playSound(null, blockPos, BzSounds.HONEY_COMPASS_BLOCK_LOCK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            if(player instanceof ServerPlayer serverPlayer) {
                BzCriterias.HONEY_COMPASS_USE_TRIGGER.get().trigger(serverPlayer);
            }

            if (!level.isClientSide()) {
                boolean singleCompass = !player.getAbilities().instabuild && itemStack.getCount() == 1;

                if (singleCompass) {
                    HoneyCompassBaseData honeyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
                    HoneyCompassTargetData honeyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());

                    String compassType = "block";

                    Optional<BlockPos> targetPos = Optional.of(blockPos);
                    Optional<ResourceKey<Level>> targetDimension = Optional.of(level.dimension());
                    Optional<String> targetBlock = Optional.of(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString());
                    Optional<String> targetStructureTag = Optional.empty();

                    isFailed = false;

                    setCompassStateData(honeyCompassStateData, locked, searchId, isLoading, isFailed, locatedSpecialStructure, itemStack);
                    setCompassBaseData(honeyCompassBaseData, compassType, itemStack);
                    setCompassTargetData(honeyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, itemStack);
                }
                else {
                    ItemStack newCompass = itemStack.copyWithCount(1);
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    HoneyCompassStateData newHoneyCompassStateData = itemStack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
                    HoneyCompassBaseData newHoneyCompassBaseData = itemStack.get(BzDataComponents.HONEY_COMPASS_BASE_DATA.get());
                    HoneyCompassTargetData newHoneyCompassTargetData = itemStack.get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get());

                    String compassType = "block";

                    Optional<BlockPos> targetPos = Optional.of(blockPos);
                    Optional<ResourceKey<Level>> targetDimension = Optional.of(level.dimension());
                    Optional<String> targetBlock = Optional.of(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString());
                    Optional<String> targetStructureTag = Optional.empty();

                    setCompassStateData(newHoneyCompassStateData, locked, searchId, false, false, locatedSpecialStructure, newCompass);
                    setCompassBaseData(newHoneyCompassBaseData, compassType, newCompass);
                    setCompassTargetData(newHoneyCompassTargetData, targetBlock, targetStructureTag, targetPos, targetDimension, newCompass);

                    if (!player.getInventory().add(newCompass)) {
                        player.drop(newCompass, false);
                    }
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(useOnContext);
    }

    public static boolean isValidBeeHive(BlockState block) {
        if (block.is(BzTags.FORCED_ALLOWED_POSITION_TRACKING_BLOCKS)) return true;

        if(block.is(BzTags.DISALLOWED_POSITION_TRACKING_BLOCKS)) return false;

        if (block.is(BlockTags.BEEHIVES) || block.getBlock() instanceof BeehiveBlock) {
            return true;
        }

        return false;
    }

    public static void setCompassTargetData(HoneyCompassTargetData honeyCompassTargetData, Optional<String> targetBlock, Optional<String> targetStructureTag, Optional<BlockPos> targetPos, Optional<ResourceKey<Level>> targetDimension, ItemStack itemStack) {
        if (honeyCompassTargetData.isDifferent(targetBlock, targetStructureTag, targetPos, targetDimension)) {
            itemStack.set(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get(),
                    new HoneyCompassTargetData(targetBlock, targetStructureTag, targetPos, targetDimension));
        }
    }

    public static void setCompassBaseData(HoneyCompassBaseData honeyCompassBaseData, String compassType, ItemStack itemStack) {
        if (!honeyCompassBaseData.compassType().equals(compassType)) {
            itemStack.set(BzDataComponents.HONEY_COMPASS_BASE_DATA.get(),
                    new HoneyCompassBaseData(compassType, honeyCompassBaseData.customName(), honeyCompassBaseData.customDescription()));
        }
    }

    public static void setCompassStateData(HoneyCompassStateData honeyCompassStateData, boolean locked, Optional<UUID> searchId, boolean isLoading, boolean isFailed, boolean locatedSpecialStructure, ItemStack itemStack) {
        if (honeyCompassStateData.isDifferent(locked, searchId, isLoading, isFailed, locatedSpecialStructure)) {
            itemStack.set(BzDataComponents.HONEY_COMPASS_STATE_DATA.get(),
                    new HoneyCompassStateData(locked, searchId, isLoading, isFailed, locatedSpecialStructure));
        }
    }
}
