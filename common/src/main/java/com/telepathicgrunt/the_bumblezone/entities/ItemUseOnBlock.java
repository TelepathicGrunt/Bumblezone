package com.telepathicgrunt.the_bumblezone.entities;


import com.telepathicgrunt.the_bumblezone.events.ItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.ItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemUseOnBlock {

    public static boolean onItemUseOnBlock(ItemUseOnBlockEvent event) {
        if (event.usingStack().is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return false;
        }

        return EntityTeleportationHookup.runItemUseOn(event.user(), event.clickedPos(), event.blockstate(), event.usingStack());
    }

    public static boolean onEarlyItemUseOnBlock(ItemUseEvent event) {
        if (!event.usingStack().is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return false;
        }

        Player player = event.user();
        HitResult hitResult = player.pick(player.isCreative() ? 5.0f : 4.5f, 1.0f, false);

        if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
            return EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), event.level().getBlockState(blockHitResult.getBlockPos()), event.usingStack());
        }

        return false;
    }
}
