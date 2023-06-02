package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.StateReturningBrushableBlock;
import com.telepathicgrunt.the_bumblezone.mixin.BlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StateFocusedBrushableBlockEntity extends BrushableBlockEntity {
    protected StateFocusedBrushableBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
        ((BlockEntityAccessor)this).setType(blockEntityType);
    }

    public StateFocusedBrushableBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.STATE_FOCUSED_BRUSHABLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void brushingCompleted(Player player) {
        if (this.level == null || this.level.getServer() == null) {
            return;
        }

        this.dropContent(player);

        BlockState blockState = this.getBlockState();
        this.level.levelEvent(3008, this.getBlockPos(), Block.getId(blockState));

        Block block = this.getBlockState().getBlock();
        BlockState finalState;
        if (block instanceof StateReturningBrushableBlock stateReturningBrushableBlock) {
            finalState = stateReturningBrushableBlock.getTurnsIntoState();
        }
        else if (block instanceof BrushableBlock brushableBlock) {
            finalState = brushableBlock.getTurnsInto().defaultBlockState();
        }
        else {
            finalState = Blocks.AIR.defaultBlockState();
        }
        this.level.setBlock(this.worldPosition, finalState, 3);
    }
}
