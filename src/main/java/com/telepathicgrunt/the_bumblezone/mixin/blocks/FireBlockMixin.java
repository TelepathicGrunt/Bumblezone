package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyCocoon;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    // Make Honey Cocoon drop items when fire broken
    @Inject(method = "tryCatchFire(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILnet/minecraft/util/RandomSource;ILnet/minecraft/core/Direction;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"),
            require = 0)
    private void thebumblezone_fireHoneyCocoonLootDrop(Level level,
                                                       BlockPos blockPos,
                                                       int fireChance,
                                                       RandomSource randomSource,
                                                       int fireAge,
                                                       Direction face,
                                                       CallbackInfo ci)
    {
        BlockState fireBrokenBlock = level.getBlockState(blockPos);

        if (fireBrokenBlock.getBlock() instanceof HoneyCocoon && level instanceof ServerLevel serverLevel) {
            BlockEntity blockentity = fireBrokenBlock.hasBlockEntity() ? serverLevel.getBlockEntity(blockPos) : null;

            Block.getDrops(fireBrokenBlock, serverLevel, blockPos, blockentity, null, ItemStack.EMPTY).forEach((itemStack) -> {
                if (!itemStack.is(BzItems.HONEY_COCOON.get())) {
                    Block.popResource(serverLevel, blockPos, itemStack);
                }
            });

            fireBrokenBlock.spawnAfterBreak(serverLevel, blockPos, ItemStack.EMPTY, true);

            serverLevel.levelEvent(2001, blockPos, Block.getId(fireBrokenBlock));
            serverLevel.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(null, fireBrokenBlock));
        }
    }
}