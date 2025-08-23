package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.menus.StrictChestMenu;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.DefaultLootFiller;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;

public class LootrCompat implements ModCompat {
    public LootrCompat() {
        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.lootrPresent = true;
    }

    public static MenuProvider getCocoonMenu(ServerPlayer player, HoneyCocoonBlockEntity blockEntity) {
        ILootrInfoProvider iLootrInfoProvider = ILootrInfoProvider.of(blockEntity, blockEntity.getBlockEntityUuid());
        return LootrAPI.getInventory(iLootrInfoProvider, player, DefaultLootFiller.getInstance(), LootrCompat::menuBuilder);
    }

    public static AbstractContainerMenu menuBuilder(int id, Inventory inventory, Container container, int rows) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x2.get(), id, inventory, container, rows);
    }
}
