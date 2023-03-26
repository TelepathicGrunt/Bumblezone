package com.telepathicgrunt.the_bumblezone.mixin.blocks;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {
    "net.minecraft.world.item.BlockItem",
    "net.minecraft.world.item.StandingAndWallBlockItem"
})
public class WaterlogForPlacingBlockMixin {

    @ModifyReturnValue(method = "getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At(value = "RETURN"),
            require = 0)
    private BlockState thebumblezone_waterlogWhenPlacedIntoSugarWater(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        if(blockState != null && blockState.hasProperty(BlockStateProperties.WATERLOGGED) && !blockState.getValue(BlockStateProperties.WATERLOGGED) && blockState.is(BzTags.WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID)) {
            if (!(blockState.hasProperty(SlabBlock.TYPE) && blockState.getValue(SlabBlock.TYPE) == SlabType.DOUBLE)){
                FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
                if (fluidState.getType() == BzFluids.SUGAR_WATER_FLUID.get()) {
                    BlockState newState = blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                    if (blockState.is(BlockTags.CAMPFIRES) && blockState.hasProperty(CampfireBlock.LIT) && blockState.getValue(CampfireBlock.LIT)) {
                        newState = newState.setValue(CampfireBlock.LIT, false);
                    }
                    return newState;
                }
            }
        }
        return blockState;
    }
}
