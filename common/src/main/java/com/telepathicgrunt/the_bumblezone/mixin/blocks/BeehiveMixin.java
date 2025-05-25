package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BeehiveBlock.class)
public class BeehiveMixin {

    // makes Comb Cutter increase drops from BeehiveBlocks.
    @Inject(method = "use(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BeehiveBlock;dropHoneycomb(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void bumblezone$combDropIncrease(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        CombCutterEnchantment.increasedCombDrops(player, world, pos);
    }

    @PlatformOnly("forge")
    @Inject(method = "angerNearbyBees(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1, remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void bumblezone$essenceBeehivePreventAnger2_forge(Level level, BlockPos blockPos, CallbackInfo ci, List<Bee> beeList, List<Player> playerList) {
        BeeAggression.preventAngerOnEssencedPlayers(beeList, playerList);
    }

    @PlatformOnly({"fabric"})
    @Inject(method = "angerNearbyBees(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void bumblezone$essenceBeehivePreventAnger2_fabric(Level level, BlockPos blockPos, CallbackInfo ci, List<Bee> beeList, List<Player> playerList) {
        BeeAggression.preventAngerOnEssencedPlayers(beeList, playerList);
        if (playerList.isEmpty()) {
            ci.cancel(); // Prevent crash if no players are around
        }
    }
}