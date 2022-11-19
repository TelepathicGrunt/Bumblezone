package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {

    @Inject(method = "emptyAllLivingFromHive(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity;releaseAllOccupants(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$BeeReleaseStatus;)Ljava/util/List;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void thebumblezone_essenceBeehivePreventAnger1(Player player,
                                               BlockState blockState,
                                               BeehiveBlockEntity.BeeReleaseStatus beeReleaseStatus,
                                               CallbackInfo ci,
                                               List<Entity> entities)
    {
        BeeAggression.preventAngerOnEssencedPlayers(player, entities);
    }
}