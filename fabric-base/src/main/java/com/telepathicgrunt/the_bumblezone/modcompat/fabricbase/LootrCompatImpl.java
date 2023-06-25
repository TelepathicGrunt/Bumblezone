package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.modcompat.LootrCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.zestyblaze.lootr.api.LootrAPI;

public class LootrCompatImpl {
  public static MenuProvider getCocoonMenu(ServerPlayer player, HoneyCocoonBlockEntity blockEntity) {
    return LootrAPI.getModdedMenu(blockEntity.getLevel(), blockEntity.getBlockEntityUuid(), blockEntity.getBlockPos(), player, blockEntity, (lootPlayer, inventory, table, seed) -> LootrCompat.unpackLootTable(blockEntity, lootPlayer, inventory, table, seed), blockEntity::getLootTable, blockEntity::getLootSeed, LootrCompat::menuBuilder);
  }

  public static void unpackLootTable(HoneyCocoonBlockEntity blockEntity, Player player, Container inventory, ResourceLocation table, long seed) {
    if (table == null) {
      LootrAPI.LOG.error("Unable to fill loot cocoon in " + blockEntity.getLevel().dimension() + " at " + blockEntity.getBlockPos() + " as the loot table was null.");
      return;
    }
    LootTable loottable = blockEntity.getLevel().getServer().getLootData().getLootTable(table);
    if (loottable == LootTable.EMPTY) {
      LootrAPI.LOG.error("Unable to fill loot cocoon in " + blockEntity.getLevel().dimension() + " at " + blockEntity.getBlockPos() + " as the loot table '" + table + "' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.");
      return;
    }
    LootParams.Builder builder = (new LootParams.Builder((ServerLevel) blockEntity.getLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockEntity.getBlockPos())));
    if (player != null) {
      builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
    }
    loottable.fill(inventory, builder.create(LootContextParamSets.CHEST), LootrAPI.getLootSeed(seed));
  }
}
