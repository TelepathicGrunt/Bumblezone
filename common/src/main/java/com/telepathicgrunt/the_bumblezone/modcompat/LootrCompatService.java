package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.NotImplementedException;

public interface LootrCompatService {

    LootrCompatService INSTANCE = GeneralUtils.loadService(LootrCompatService.class);

    default MenuProvider getCocoonMenu(ServerPlayer player, HoneyCocoonBlockEntity blockEntity) {
        throw new NotImplementedException("LootrCompat getCocoonMenu is not implemented!");
    }

    default void unpackLootTable(HoneyCocoonBlockEntity blockEntity, Player player, Container inventory, ResourceLocation table, long seed) {
        throw new NotImplementedException("LootrCompat unpackLootTable is not implemented!");
    }
}
