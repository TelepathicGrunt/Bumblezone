package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestMenu;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.apache.commons.lang3.NotImplementedException;

public class LootrCompat implements ModCompat {
  public LootrCompat() {
    // Keep at end so it is only set to true if no exceptions was thrown during setup
    ModChecker.lootrPresent = true;
  }

  @ExpectPlatform
  public static MenuProvider getCocoonMenu(ServerPlayer player, HoneyCocoonBlockEntity blockEntity) {
    throw new NotImplementedException("LootrCompat getCocoonMenu is not implemented!");
  }

  @ExpectPlatform
  public static void unpackLootTable(HoneyCocoonBlockEntity blockEntity, Player player, Container inventory, ResourceLocation table, long seed) {
    throw new NotImplementedException("LootrCompat unpackLootTable is not implemented!");
  }

  public static AbstractContainerMenu menuBuilder(int id, Inventory inventory, Container container, int rows) {
    return new StrictChestMenu(BzMenuTypes.STRICT_9x2.get(), id, inventory, container, rows);
  }
}
