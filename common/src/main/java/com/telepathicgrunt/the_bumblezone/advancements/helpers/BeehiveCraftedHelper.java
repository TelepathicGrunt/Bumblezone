package com.telepathicgrunt.the_bumblezone.advancements.helpers;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;

public class BeehiveCraftedHelper {

    public static void checkAndIncrementBeehiveCraftedCount(Player player, ItemStack createdItem) {
        if (createdItem.getItem() instanceof BlockItem blockItem &&
            blockItem.getBlock() instanceof BeehiveBlock &&
            player instanceof ServerPlayer serverPlayer &&
            PlayerDataHandler.rootAdvancementDone(serverPlayer))
        {
            PlatformService.INSTANCE.getModule(serverPlayer, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.craftedBeehives++;
                BzCriterias.BEEHIVE_CRAFTED_TRIGGER.get().trigger(serverPlayer, module.craftedBeehives);
            });
        }
    }
}
