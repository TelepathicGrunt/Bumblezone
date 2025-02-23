package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.menus.StrictChestMenu;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import noobanidus.mods.lootr.common.api.data.LootFiller;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class LootrCompat implements ModCompat {
    public LootrCompat() {
        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.lootrPresent = true;
    }

    public static MenuProvider getCocoonMenu(ServerPlayer player, HoneyCocoonBlockEntity blockEntity) {
        ILootrInfoProvider iLootrInfoProvider = ILootrInfoProvider.of(blockEntity, blockEntity.getBlockEntityUuid());
        return LootrAPI.getInventory(iLootrInfoProvider, player, new HoneyCocoonLootFiller(), LootrCompat::menuBuilder);
    }

    public static class HoneyCocoonLootFiller implements LootFiller {
        @Override
        public void unpackLootTable(@NotNull ILootrInfoProvider lootrInfoProvider, @NotNull Player player, Container container) {
            if (lootrInfoProvider.getInfoContainer() instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
                if (lootrInfoProvider.getInfoLootTable() == null) {
                    LootrAPI.LOG.error("Unable to fill loot cocoon in {} at {} as the loot table was null.", lootrInfoProvider.getInfoLevel().dimension(), honeyCocoonBlockEntity.getBlockPos());
                    return;
                }

                if (lootrInfoProvider.getInfoLevel() instanceof ServerLevel serverLevel) {
                    LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootrInfoProvider.getInfoLootTable());
                    if (lootTable == LootTable.EMPTY) {
                        LootrAPI.LOG.error("Unable to fill loot cocoon in {} at {} as the loot table '{}' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.", serverLevel.dimension(), honeyCocoonBlockEntity.getBlockPos(), lootrInfoProvider.getInfoLootTable());
                        return;
                    }

                    if (lootrInfoProvider.getInfoContainer() != null) {
                        LootParams.Builder builder = (new LootParams.Builder((ServerLevel) lootrInfoProvider.getInfoLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(honeyCocoonBlockEntity.getBlockPos())));
                        builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
                        honeyCocoonBlockEntity.setLootrInitialInventorySetting(true);
                        lootTable.fill(honeyCocoonBlockEntity, builder.create(LootContextParamSets.CHEST), LootrAPI.getLootSeed(lootrInfoProvider.getInfoLootSeed()));
                        honeyCocoonBlockEntity.setLootrInitialInventorySetting(false);
                    }
                }
            }
        }
    }

    public static AbstractContainerMenu menuBuilder(int id, Inventory inventory, Container container, int rows) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x2.get(), id, inventory, container, rows);
    }
}
