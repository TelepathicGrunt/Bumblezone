package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
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

import java.util.List;
import java.util.Optional;

public class HoneyCompass extends Item implements Vanishable {
    public static final String TAG_TARGET_POS = "TargetPos";
    public static final String TAG_TARGET_DIMENSION = "TargetDimension";
    public static final String TAG_TYPE = "CompassType";
    public static final String TAG_LOADING = "IsLoading";
    public static final String TARGET_BLOCK = "TargetBlock";
    public static final String IS_THRONE_TYPE = "IsThrone";

    public HoneyCompass(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return isStructureCompass(itemStack) || super.isFoil(itemStack);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack) {
        if (isLoadingCompass(itemStack)) {
            return "item.the_bumblezone.honey_compass_structure_loading";
        }

        if(isStructureCompass(itemStack)) {
            if(isThroneStructureCompass(itemStack)) {
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
                Block block = Registry.BLOCK.get(new ResourceLocation(blockString));
                if (block != Blocks.AIR) {
                    return Component.translatable(this.getDescriptionId(itemStack), block.getName());
                }
            }
            return Component.translatable(this.getDescriptionId(itemStack), "Unknown block");
        }
        return Component.translatable(this.getDescriptionId(itemStack));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (!level.isClientSide) {
            if (!isThroneStructureCompass(itemStack) && isBlockCompass(itemStack)) {
                CompoundTag compoundTag = itemStack.getOrCreateTag();
                if (compoundTag.contains(TAG_TARGET_POS) && compoundTag.contains(TARGET_BLOCK) && compoundTag.contains(TAG_TARGET_DIMENSION)) {

                    Optional<ResourceKey<Level>> optional = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get(HoneyCompass.TAG_TARGET_DIMENSION)).result();
                    if (optional.isPresent() && optional.equals(level.dimension())) {
                        BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound(TAG_TARGET_POS));
                        if (!level.isInWorldBounds(blockPos)) {
                            compoundTag.remove(TAG_TARGET_POS);
                            compoundTag.remove(TAG_TARGET_DIMENSION);
                            compoundTag.remove(TARGET_BLOCK);
                            compoundTag.remove(TAG_TYPE);
                            return;
                        }

                        ChunkAccess chunk = level.getChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4, ChunkStatus.FULL, false);
                        if(chunk != null && !(Registry.BLOCK.getKey(chunk.getBlockState(blockPos).getBlock()).toString().equals(compoundTag.getString(TARGET_BLOCK)))) {
                            compoundTag.remove(TAG_TARGET_POS);
                            compoundTag.remove(TAG_TARGET_DIMENSION);
                            compoundTag.remove(TARGET_BLOCK);
                            compoundTag.remove(TAG_TYPE);
                        }
                    }
                }
                else {
                    compoundTag.remove(TAG_TARGET_POS);
                    compoundTag.remove(TAG_TARGET_DIMENSION);
                    compoundTag.remove(TARGET_BLOCK);
                    compoundTag.remove(TAG_TYPE);
                }
            }

        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)  {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockPos playerPos = player.blockPosition();

        if (itemStack.hasTag() && getLoadingTags(itemStack.getOrCreateTag())) {
            if (ThreadExecutor.isRunningASearch()) {
                return InteractionResultHolder.fail(itemStack);
            }
            else {
                setLoadingTags(itemStack.getOrCreateTag(), false);
            }
        }

        if (!level.isClientSide() && !isThroneStructureCompass(itemStack) && !isStructureCompass(itemStack)) {
            setLoadingTags(itemStack.getOrCreateTag(), true);
            ThreadExecutor.locate((ServerLevel) level, BzTags.HONEY_COMPASS_LOCATING, playerPos, 100, false)
                    .thenOnServerThread(foundPos -> setCompassData((ServerLevel) level, (ServerPlayer) player, interactionHand, itemStack, foundPos));
            return InteractionResultHolder.success(itemStack);
        }

        return super.use(level, player, interactionHand);
    }

    private void setCompassData(ServerLevel serverLevel, ServerPlayer serverPlayer, InteractionHand interactionHand, ItemStack itemStack, BlockPos structurePos) {
        setLoadingTags(itemStack.getOrCreateTag(), false);
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

        if (handCompass.hasTag() && getLoadingTags(handCompass.getOrCreateTag())) {
            if (ThreadExecutor.isRunningASearch()) {
                return InteractionResult.FAIL;
            }
            else {
                setLoadingTags(handCompass.getOrCreateTag(), false);
            }
        }

        if (!isThroneStructureCompass(handCompass) && player != null && isValidBeeHive(targetBlock)) {
            level.playSound(null, blockPos, BzSounds.HONEY_COMPASS_BLOCK_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            if(player instanceof ServerPlayer serverPlayer) {
                BzCriterias.HONEY_COMPASS_USE_TRIGGER.trigger(serverPlayer);
            }

            boolean singleCompass = !player.getAbilities().instabuild && handCompass.getCount() == 1;
            if (singleCompass) {
                this.addBlockTags(level.dimension(), blockPos, handCompass.getOrCreateTag(), targetBlock.getBlock());
            }
            else {
                ItemStack newCompass = new ItemStack(BzItems.HONEY_COMPASS, 1);
                CompoundTag newCompoundTag = handCompass.hasTag() ? handCompass.getTag().copy() : new CompoundTag();
                newCompass.setTag(newCompoundTag);
                if (!player.getAbilities().instabuild) {
                    handCompass.shrink(1);
                }

                this.addBlockTags(level.dimension(), blockPos, newCompoundTag, targetBlock.getBlock());
                if (!player.getInventory().add(newCompass)) {
                    player.drop(newCompass, false);
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

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (isThroneStructureCompass(itemStack)) {
            components.forEach(component -> {
                if (component instanceof MutableComponent mutableComponent) {
                    mutableComponent.withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD);
                }
            });
        }
    }

    public static void setLoadingTags(CompoundTag compoundTag, boolean isLoading) {
        compoundTag.putBoolean(TAG_LOADING, isLoading);
    }

    public static boolean getLoadingTags(CompoundTag compoundTag) {
        if (!compoundTag.contains(TAG_LOADING)) {
            return false;
        }
        return compoundTag.getBoolean(TAG_LOADING);
    }

    public static void addStructureTags(ResourceKey<Level> resourceKey, BlockPos blockPos, CompoundTag compoundTag) {
        compoundTag.put(TAG_TARGET_POS, NbtUtils.writeBlockPos(blockPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, resourceKey).resultOrPartial(Bumblezone.LOGGER::error).ifPresent(tag -> compoundTag.put(TAG_TARGET_DIMENSION, tag));
        compoundTag.putString(TAG_TYPE, "structure");
        compoundTag.remove(TARGET_BLOCK);
    }

    public static void addBlockTags(ResourceKey<Level> resourceKey, BlockPos blockPos, CompoundTag compoundTag, Block block) {
        compoundTag.put(TAG_TARGET_POS, NbtUtils.writeBlockPos(blockPos));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, resourceKey).resultOrPartial(Bumblezone.LOGGER::error).ifPresent(tag -> compoundTag.put(TAG_TARGET_DIMENSION, tag));
        compoundTag.putString(TAG_TYPE, "block");
        compoundTag.putString(TARGET_BLOCK, Registry.BLOCK.getKey(block).toString());
    }

    public static void addThroneTags(CompoundTag compoundTag) {
        compoundTag.putBoolean(IS_THRONE_TYPE, true);
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
            if (tag != null && tag.contains(TARGET_BLOCK)) {
                return tag.getString(TARGET_BLOCK);
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

    public static boolean isThroneStructureCompass(ItemStack compassItem) {
        if(compassItem.hasTag()) {
            CompoundTag tag = compassItem.getTag();
            return tag != null && tag.contains(IS_THRONE_TYPE) && tag.getBoolean(IS_THRONE_TYPE);
        }
        return false;
    }

    public static boolean isLoadingCompass(ItemStack compassItem) {
        if(compassItem.hasTag()) {
            CompoundTag tag = compassItem.getTag();
            return tag != null && tag.contains(TAG_LOADING) && tag.getBoolean(TAG_LOADING);
        }
        return false;
    }
}
