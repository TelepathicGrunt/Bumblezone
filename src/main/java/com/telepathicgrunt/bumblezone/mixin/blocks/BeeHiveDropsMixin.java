package com.telepathicgrunt.bumblezone.mixin.blocks;

import com.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public class BeeHiveDropsMixin {

    // makes Comb Cutter increase drops from BeehiveBlocks.
    @Inject(method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BeehiveBlock;dropHoneycomb(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    private void thebumblezone_combDropIncrease(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        CombCutterEnchantment.increasedCombDrops(player, world, pos);
    }
}