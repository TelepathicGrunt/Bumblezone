package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;
import java.util.Optional;

public class HoneyCompass extends Item implements Vanishable {
    public static final String TAG_TARGET_POS = "TargetPos";
    public static final String TAG_TARGET_DIMENSION = "TargetDimension";
    public static final String TAG_TYPE = "CompassType";
    public static final String TAG_LOADING = "IsLoading";
    public static final String TAG_FAILED = "IsFailed";
    public static final String TAG_STRUCTURE_TAG = "TargetStructureTag";
    public static final String TAG_TARGET_BLOCK = "TargetBlock";
    public static final String TAG_IS_THRONE_TYPE = "IsThrone";
    public static final String TAG_CUSTOM_NAME_TYPE = "CustomName";
    public static final String TAG_CUSTOM_DESCRIPTION_TYPE = "CustomDescription";
    public static final String TAG_LOCKED = "Locked";

    public HoneyCompass(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return getBooleanTag(itemStack.getTag(), TAG_IS_THRONE_TYPE) || getBooleanTag(itemStack.getTag(), TAG_LOCKED) || super.isFoil(itemStack);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        if (getBooleanTag(itemStack.getTag(), TAG_LOADING)) {
            return "item.the_bumblezone.honey_compass_structure_loading";
        }

        if (getBooleanTag(itemStack.getTag(), TAG_FAILED)) {
            return "item.the_bumblezone.honey_compass_structure_failed";
        }

        if (hasTagSafe(itemStack.getTag(), TAG_CUSTOM_NAME_TYPE)) {
            return itemStack.getTag().getString(TAG_CUSTOM_NAME_TYPE);
        }

        if(isStructureCompass(itemStack)) {
            if(getBooleanTag(itemStack.getTag(), TAG_IS_THRONE_TYPE)) {
                return "item.the_bumblezone.honey_compass_throne_structure";
            }
            else {
                return "item.the_bumblezone.honey_compass_structure";
            }
        }

        if(isBlockCompass(itemStack)) {
            return "item.the_bumblezone.honey_compass_block";
        }

        return super.getDescriptionId(itemStack);
    }

    public Component getName(ItemStack itemStack) {
        if(isBlockCompass(itemStack)) {
            String blockString = getStoredBlock(itemStack);
            if (blockString != null) {
                Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockString));
                if (block != Blocks.AIR) {
                    return Component.translatable(this.getDescriptionId(itemStack), block.getName());
                }
            }
            return Component.translatable(this.getDescriptionId(itemStack), Component.translatable("item.the_bumblezone.honey_compass_unknown_block"));
        }
        return Component.translatable(this.getDescriptionId(itemStack));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (getBooleanTag(itemStack.getTag(), TAG_FAILED)) {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_structure_failed_description"));
            return;
        }

        if (hasTagSafe(itemStack.getTag(), TAG_CUSTOM_DESCRIPTION_TYPE)) {
            components.add(Component.translatable(itemStack.getTag().getString(TAG_CUSTOM_DESCRIPTION_TYPE)));
            return;
        }

        if (getBooleanTag(itemStack.getTag(), TAG_IS_THRONE_TYPE)) {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_throne_description"));
            components.forEach(component -> {
                if (component instanceof MutableComponent mutableComponent) {
                    mutableComponent.withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD);
                }
            });
        }
        else if (isBlockCompass(itemStack)) {
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description1"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description2"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description3"));
            components.add(Component.translatable("item.the_bumblezone.honey_compass_block_description4"));
        }
        else if (isStructureCompass(itemStack)) {
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
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (!level.isClientSide) {
            if (!getBooleanTag(itemStack.getTag(), TAG_FAILED) &&
                !getBooleanTag(itemStack.getTag(), TAG_LOADING) &&
                hasTagSafe(itemStack.getTag(), TAG_STRUCTURE_TAG) &&
                !hasTagSafe(itemStack.getTag(), TAG_TARGET_POS))
            {
                itemStack.getOrCreateTag().putBoolean(TAG_FAILED, true);
            }

            if (getBooleanTag(itemStack.getTag(), TAG_LOADING) && !ThreadExecutor.isRunningASearch() && !ThreadExecutor.hasQueuedSearch()) {
                itemStack.getOrCreateTag().putBoolean(TAG_LOADING, false);
                itemStack.getOrCreateTag().putBoolean(TAG_FAILED, true);
            }

            if (!getBooleanTag(itemStack.getTag(), TAG_IS_THRONE_TYPE) && isBlockCompass(itemStack)) {
                CompoundTag compoundTag = itemStack.getOrCreateTag();
                if (compoundTag.contains(TAG_TARGET_POS) && compoundTag.contains(TAG_TARGET_BLOCK) && compoundTag.contains(TAG_TARGET_DIMENSION)) {

                    Optional<ResourceKey<Level>> optional = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get(HoneyCompass.TAG_TARGET_DIMENSION)).result();
                    if (optional.isPresent() && optional.equals(level.dimension())) {
                        BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound(TAG_TARGET_POS));
                        if (!level.isInWorldBounds(blockPos)) {
                            compoundTag.remove(TAG_TARGET_POS);
                            compoundTag.remove(TAG_TARGET_DIMENSION);
                            compoundTag.remove(TAG_TARGET_BLOCK);
                            compoundTag.remove(TAG_TYPE);
                            return;
                        }

                        ChunkAccess chunk = level.getChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4, ChunkStatus.FULL, false);
                        if(chunk != null && !(BuiltInRegistries.BLOCK.getKey(chunk.getBlockState(blockPos).getBlock()).toString().equals(compoundTag.getString(TAG_TARGET_BLOCK)))) {
                            compoundTag.remove(TAG_TARGET_POS);
                            compoundTag.remove(TAG_TARGET_DIMENSION);
                            compoundTag.remove(TAG_TARGET_BLOCK);
                            compoundTag.remove(TAG_TYPE);
                        }
                    }
                }
                else {
                    compoundTag.remove(TAG_TARGET_POS);
                    compoundTag.remove(TAG_TARGET_DIMENSION);
                    compoundTag.remove(TAG_TARGET_BLOCK);
                    compoundTag.remove(TAG_TYPE);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)  {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockPos playerPos = player.blockPosition();

        if (getBooleanTag(itemStack.getTag(), TAG_FAILED) && hasTagSafe(itemStack.getTag(), TAG_STRUCTURE_TAG)) {
            if (level instanceof ServerLevel serverLevel && serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
                TagKey<Structure> structureTagKey = TagKey.create(Registries.STRUCTURE, new ResourceLocation(itemStack.getOrCreateTag().getString(TAG_STRUCTURE_TAG)));
                Optional<HolderSet.Named<Structure>> optional = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(structureTagKey);
                boolean structureExists = optional.isPresent() && optional.get().stream().anyMatch(structureHolder -> serverLevel.getChunkSource().getGeneratorState().getPlacementsForStructure(structureHolder).size() > 0);
                if (structureExists) {
                    itemStack.getOrCreateTag().putBoolean(TAG_LOADING, true);
                    itemStack.getOrCreateTag().putBoolean(TAG_FAILED, false);
                    ThreadExecutor.locate((ServerLevel) level, structureTagKey, playerPos, 100, false)
                            .thenOnServerThread(foundPos -> setCompassData((ServerLevel) level, (ServerPlayer) player, interactionHand, itemStack, foundPos));
                }
                else {
                    player.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_wrong_dimension"), true);
                    return InteractionResultHolder.pass(itemStack);
                }
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }

        if (itemStack.hasTag() && getBooleanTag(itemStack.getOrCreateTag(), TAG_LOADING)) {
            if (ThreadExecutor.isRunningASearch() || ThreadExecutor.hasQueuedSearch()) {
                return InteractionResultHolder.fail(itemStack);
            }
            else {
                itemStack.getOrCreateTag().putBoolean(TAG_LOADING, false);
            }
        }

        if (getBooleanTag(itemStack.getTag(), TAG_LOCKED) || getBooleanTag(itemStack.getTag(), TAG_IS_THRONE_TYPE)) {
            return super.use(level, player, interactionHand);
        }

        if (level instanceof ServerLevel serverLevel && !isStructureCompass(itemStack)) {
            Optional<HolderSet.Named<Structure>> optional = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(BzTags.HONEY_COMPASS_DEFAULT_LOCATING);
            boolean structureExists = optional.isPresent() && optional.get().stream().anyMatch(structureHolder -> serverLevel.getChunkSource().getGeneratorState().getPlacementsForStructure(structureHolder).size() > 0);
                if (structureExists) {
                itemStack.getOrCreateTag().putBoolean(TAG_LOADING, true);
                ThreadExecutor.locate((ServerLevel) level, BzTags.HONEY_COMPASS_DEFAULT_LOCATING, playerPos, 100, false)
                        .thenOnServerThread(foundPos -> setCompassData((ServerLevel) level, (ServerPlayer) player, interactionHand, itemStack, foundPos));
            }
            else {
                player.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_wrong_dimension"), true);
                return InteractionResultHolder.pass(itemStack);
            }
            return InteractionResultHolder.success(itemStack);
        }

        return super.use(level, player, interactionHand);
    }

    private void setCompassData(ServerLevel serverLevel, ServerPlayer serverPlayer, InteractionHand interactionHand, ItemStack itemStack, BlockPos structurePos) {
        itemStack.getOrCreateTag().putBoolean(TAG_LOADING, false);
        serverLevel.playSound(null, serverPlayer.blockPosition(), BzSounds.HONEY_COMPASS_STRUCTURE_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (structurePos == null) {
            serverPlayer.swing(interactionHand);
            serverPlayer.displayClientMessage(Component.translatable("item.the_bumblezone.honey_compass_structure_failed"), false);
            return;
        }

        BzCriterias.HONEY_COMPASS_USE_TRIGGER.trigger(serverPlayer);
        boolean singleCompass = !serverPlayer.getAbilities().instabuild && itemStack.getCount() == 1;
        if (singleCompass) {
            addStructureTags(serverLevel.dimension(), structurePos, itemStack.getOrCreateTag());
        }
        else {
            ItemStack newCompass = new ItemStack(BzItems.HONEY_COMPASS, 1);
            CompoundTag newCompoundTag = itemStack.hasTag() ? itemStack.getTag().copy() : new CompoundTag();
            newCompass.setTag(newCompoundTag);
            if (!serverPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            addStructureTags(serverLevel.dimension(), structurePos, newCompoundTag);
            if (!serverPlayer.getInventory().add(newCompass)) {
                serverPlayer.drop(newCompass, false);
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockPos blockPos = useOnContext.getClickedPos();
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        ItemStack handCompass = useOnContext.getItemInHand();
        BlockState targetBlock = level.getBlockState(blockPos);

        if (handCompass.hasTag() && getBooleanTag(handCompass.getOrCreateTag(), TAG_LOADING)) {
            if (ThreadExecutor.isRunningASearch() || ThreadExecutor.hasQueuedSearch()) {
                return InteractionResult.FAIL;
            }
            else {
                handCompass.getOrCreateTag().putBoolean(TAG_LOADING, false);
            }
        }

        if (getBooleanTag(handCompass.getTag(), TAG_LOCKED) || getBooleanTag(handCompass.getTag(), TAG_IS_THRONE_TYPE)) {
            return super.useOn(useOnContext);
        }

        if (player != null && isValidBeeHive(targetBlock)) {
            level.playSound(null, blockPos, BzSounds.HONEY_COMPASS_BLOCK_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            if(player instanceof ServerPlayer serverPlayer) {
                BzCriterias.HONEY_COMPASS_USE_TRIGGER.trigger(serverPlayer);
            }

            if (!level.isClientSide()) {
                boolean singleCompass = !player.getAbilities().instabuild && handCompass.getCount() == 1;
                if (singleCompass) {
                    addBlockTags(level.dimension(), blockPos, handCompass.getOrCreateTag(), targetBlock.getBlock());
                }
                else {
                    ItemStack newCompass = new ItemStack(BzItems.HONEY_COMPASS, 1);
                    CompoundTag newCompoundTag = handCompass.hasTag() ? handCompass.getTag().copy() : new CompoundTag();
                    newCompass.setTag(newCompoundTag);
                    if (!player.getAbilities().instabuild) {
                        handCompass.shrink(1);
                    }

                    addBlockTags(level.dimension(), blockPos, newCompoundTag, targetBlock.getBlock());
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
        if(block.is(BzTags.BLACKLISTED_HONEY_COMPASS_BLOCKS)) return false;

        if(block.is(BlockTags.BEEHIVES) || block.getBlock() instanceof BeehiveBlock) {
            return true;
        }

        return false;
    }

    public static boolean getBooleanTag(CompoundTag compoundTag, String tagName) {
        if (compoundTag == null || !compoundTag.contains(tagName)) {
            return false;
        }
        return compoundTag.getBoolean(tagName);
    }

    public static boolean hasTagSafe(CompoundTag compoundTag, String tagName) {
        return compoundTag != null && compoundTag.contains(tagName);
    }

    public static void setStructureTags(CompoundTag compoundTag, TagKey<Structure> structureTagKey) {
        compoundTag.putString(TAG_STRUCTURE_TAG, structureTagKey.location().toString());
    }

    public static void addStructureTags(ResourceKey<Level> resourceKey, BlockPos blockPos, CompoundTag compoundTag) {
        compoundTag.put(TAG_TARGET_POS, NbtUtils.writeBlockPos(blockPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, resourceKey).resultOrPartial(Bumblezone.LOGGER::error).ifPresent(tag -> compoundTag.put(TAG_TARGET_DIMENSION, tag));
        compoundTag.putString(TAG_TYPE, "structure");
        compoundTag.remove(TAG_TARGET_BLOCK);
        compoundTag.remove(HoneyCompass.TAG_LOADING);
        compoundTag.remove(HoneyCompass.TAG_FAILED);
        compoundTag.remove(HoneyCompass.TAG_STRUCTURE_TAG);
    }

    public static void addBlockTags(ResourceKey<Level> resourceKey, BlockPos blockPos, CompoundTag compoundTag, Block block) {
        compoundTag.put(TAG_TARGET_POS, NbtUtils.writeBlockPos(blockPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, resourceKey).resultOrPartial(Bumblezone.LOGGER::error).ifPresent(tag -> compoundTag.put(TAG_TARGET_DIMENSION, tag));
        compoundTag.putString(TAG_TYPE, "block");
        compoundTag.putString(TAG_TARGET_BLOCK, BuiltInRegistries.BLOCK.getKey(block).toString());
        compoundTag.remove(HoneyCompass.TAG_LOADING);
        compoundTag.remove(HoneyCompass.TAG_FAILED);
        compoundTag.remove(HoneyCompass.TAG_STRUCTURE_TAG);
    }

    public static void addThroneTags(CompoundTag compoundTag) {
        compoundTag.putBoolean(TAG_IS_THRONE_TYPE, true);
    }

    public static boolean isBlockCompass(ItemStack compassItem) {
        if(compassItem.hasTag()) {
            CompoundTag tag = compassItem.getTag();
            return tag != null && tag.contains(TAG_TYPE) && tag.getString(TAG_TYPE).equals("block");
        }
        return false;
    }

    public static String getStoredBlock(ItemStack compassItem) {
        if(compassItem.hasTag()) {
            CompoundTag tag = compassItem.getTag();
            if (tag != null && tag.contains(TAG_TARGET_BLOCK)) {
                return tag.getString(TAG_TARGET_BLOCK);
            }
        }
        return null;
    }

    public static boolean isStructureCompass(ItemStack compassItem) {
        if(compassItem.hasTag()) {
            CompoundTag tag = compassItem.getTag();
            return tag != null && tag.contains(TAG_TYPE) && tag.getString(TAG_TYPE).equals("structure");
        }
        return false;
    }
}
