package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;

public interface LootrCompatService {
    LootrCompatService INSTANCE = GeneralUtils.loadService(LootrCompatService.class);

    MenuProvider getCocoonMenu(ServerPlayer player, HoneyCocoonBlockEntity blockEntity);

    void unpackLootTable(HoneyCocoonBlockEntity blockEntity, Player player, Container inventory, ResourceLocation table, long seed);
}
