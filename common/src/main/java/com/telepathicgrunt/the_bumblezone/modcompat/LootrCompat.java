package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;

public class LootrCompat implements ModCompat {
  public LootrCompat() {
    // Keep at end so it is only set to true if no exceptions was thrown during setup
    ModChecker.lootrPresent = true;
  }

  @ExpectPlatform
  public static MenuProvider getCocoonMenu(ServerPlayer player, BlockState state, Level level, BlockPos position) {
    throw new NotImplementedException("LootrCompat getCocoonMenu is not implemented!");
  }

  @ExpectPlatform
  public static void unpackLootTable(HoneyCocoonBlockEntity blockEntity, Player player, Container inventory, ResourceLocation table, long seed) {
    throw new NotImplementedException("LootrCompat unpackLootTable is not implemented!");
  }
}
