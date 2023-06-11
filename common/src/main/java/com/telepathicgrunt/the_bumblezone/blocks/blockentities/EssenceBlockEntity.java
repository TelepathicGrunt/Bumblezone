package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;
import java.util.UUID;

public class EssenceBlockEntity extends BlockEntity {
    private static final String UUID_TAG = "uuid";
    private UUID uuid = null;
    public long testTick = Integer.MAX_VALUE;

    protected EssenceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public EssenceBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.ESSENCE_BLOCK.get(), blockPos, blockState);
    }

    public UUID getUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        return this.uuid;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag != null) {
            this.testTick = compoundTag.getLong("test");
            if (compoundTag.contains(UUID_TAG)) {
                this.uuid = compoundTag.getUUID(UUID_TAG);
            }
            else {
                this.uuid = UUID.randomUUID();
            }
        }

        if (this.level instanceof ClientLevel) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 8);
        }
    }

    public ResourceLocation getSavedNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/saved_area/" +
                this.getBlockPos().getX() + "_" +
                this.getBlockPos().getY() + "_" +
                this.getBlockPos().getZ() + "_" +
                this.getUUID());
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        saveFieldsToTag(compoundTag);
    }

    private void saveFieldsToTag(CompoundTag compoundTag) {
        compoundTag.put(UUID_TAG, NbtUtils.createUUID(this.getUUID()));
        compoundTag.putLong("test", this.testTick);
    }

    @Override
    public void saveToItem(ItemStack stack) {
        CompoundTag compoundTag = new CompoundTag();
        this.saveAdditional(compoundTag);
        BlockItem.setBlockEntityData(stack, this.getType(), compoundTag);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveFieldsToTag(tag);
        return tag;
    }

    public boolean shouldDrawSide(Direction direction) {
        return Block.shouldRenderFace(this.getBlockState(), this.getLevel(), this.getBlockPos(), direction, this.getBlockPos().relative(direction));
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        if (level instanceof ServerLevel serverLevel && level.getGameTime() - essenceBlockEntity.testTick > 100) {
            Optional<StructureTemplate> optionalStructureTemplate = serverLevel.getStructureManager().get(essenceBlockEntity.getSavedNbt());
            optionalStructureTemplate.ifPresentOrElse(structureTemplate -> {
                Vec3i size = structureTemplate.getSize();
                BlockPos halfLengths = new BlockPos(-size.getX() / 2, -size.getY() / 2, -size.getZ() / 2);

                //reset area
                structureTemplate.placeInWorld(
                        serverLevel,
                        blockPos.offset(halfLengths),
                        blockPos.offset(halfLengths),
                        EssenceBlock.PLACEMENT_SETTINGS.getOrFillFromInternal(),
                        serverLevel.getRandom(),
                        Block.UPDATE_CLIENTS
                );
            }, () -> Bumblezone.LOGGER.warn("Bumblezone Essence Block failed to restore area from saved NBT - {} - {}", essenceBlockEntity, blockState));

            essenceBlockEntity.testTick = Integer.MAX_VALUE;
        }
    }
}