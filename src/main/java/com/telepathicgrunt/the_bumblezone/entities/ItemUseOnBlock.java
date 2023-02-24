package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemUseOnBlock {

    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return;
        }

        if(EntityTeleportationHookup.runItemUseOn(event.getEntity(), event.getPos(), event.getLevel().getBlockState(event.getPos()), itemStack)) {
            event.setCanceled(true);
        }
    }

    public static void onEarlyItemUseOnBlock(PlayerInteractEvent.RightClickItem event) {
        if (!event.getItemStack().is(BzTags.DO_ITEM_RIGHT_CLICK_CHECK_EARLIER)) {
            return;
        }

        Player player = event.getEntity();
        HitResult hitResult = player.pick(player.isCreative() ? 5.0f : 4.5f, 1.0f, false);

        if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
            if (EntityTeleportationHookup.runItemUseOn(player, blockHitResult.getBlockPos(), event.getLevel().getBlockState(blockHitResult.getBlockPos()), event.getItemStack())) {
                event.setCanceled(true);
            }
        }
    }
}
