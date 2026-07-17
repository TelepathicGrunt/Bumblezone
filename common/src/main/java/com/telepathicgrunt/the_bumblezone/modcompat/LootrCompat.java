package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.menus.StrictChestMenu;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class LootrCompat implements ModCompat {
  public LootrCompat() {
    // Keep at end so it is only set to true if no exceptions was thrown during setup
    ModChecker.lootrPresent = true;
  }

  public static AbstractContainerMenu menuBuilder(int id, Inventory inventory, Container container, int rows) {
    return new StrictChestMenu(BzMenuTypes.STRICT_9x2.get(), id, inventory, container, rows);
  }
}
