package com.telepathicgrunt.the_bumblezone.mixin.fabricbase;

import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {

    @Inject(method = "isSticky", at = @At(value = "HEAD"), cancellable = true)
    private static void bumblezone$isSticky(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof BlockExtension extension) {
            cir.setReturnValue(extension.bz$isStickyBlock(state));
        }
    }

    @Inject(method = "canStickToEachOther", at = @At("HEAD"), cancellable = true)
    private static void bumblezone$canStickToEachOther(BlockState state1, BlockState state2, CallbackInfoReturnable<Boolean> cir) {
        if (state1.getBlock() instanceof BlockExtension extension) {
            OptionalBoolean result = extension.bz$canStickTo(state1, state2);
            result.ifPresent(cir::setReturnValue);
        } else if (state2.getBlock() instanceof BlockExtension extension) {
            OptionalBoolean result = extension.bz$canStickTo(state2, state1);
            result.ifPresent(cir::setReturnValue);
        }
    }
}
