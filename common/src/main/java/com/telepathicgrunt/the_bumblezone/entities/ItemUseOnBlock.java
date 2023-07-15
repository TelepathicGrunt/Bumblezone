package com.telepathicgrunt.the_bumblezone.entities;


import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemUseOnBlock {

    public static InteractionResult onItemUseOnBlock(PlayerItemUseOnBlockEvent event) {
        if (event.usingStack().is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return InteractionResult.PASS;
        }

        boolean success = EntityTeleportationHookup.runItemUseOn(event.user(), event.hitResult().getBlockPos(), event.usingStack());

        return success ? InteractionResult.SUCCESS : null;
    }

    public static boolean onEarlyItemUseOnBlock(PlayerItemUseEvent event) {
        if (!event.usingStack().is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return false;
        }

        Player player = event.user();
        HitResult hitResult = player.pick(player.isCreative() ? 5.0f : 4.5f, 1.0f, false);

        if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
            return EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), event.usingStack());
        }

        return false;
    }
}
