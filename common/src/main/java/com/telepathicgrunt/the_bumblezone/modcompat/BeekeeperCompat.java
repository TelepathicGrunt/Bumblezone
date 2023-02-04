package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.RegisterVillagerTradesEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class BeekeeperCompat implements ModCompat {
    private static Optional<VillagerProfession> BEEKEEPER;

    public BeekeeperCompat() {
        if(BzModCompatibilityConfigs.allowBeekeeperTradesCompat) {
            BEEKEEPER = BuiltInRegistries.VILLAGER_PROFESSION.getOptional(new ResourceLocation("bk", "beekeeper"));
            RegisterVillagerTradesEvent.EVENT.addListener(BeekeeperCompat::setupBeekeeperTrades);
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.beekeeperPresent = true;
    }

    public static void setupBeekeeperTrades(RegisterVillagerTradesEvent event) {
        if(BEEKEEPER.isPresent() && event.type() == BEEKEEPER.get()) {
            event.addTrade(2,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.STICKY_HONEY_RESIDUE.get(), 1, 2, 10, 8, 0.05F));

            event.addTrade(2,
                    new GeneralUtils.BasicItemTrade(BzItems.POLLEN_PUFF.get(), Items.EMERALD, 2, 1, 12, 7, 0.05F));

            event.addTrade(2,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.POLLEN_PUFF.get(), 1, 1, 14, 5, 0.05F));

            event.addTrade(3,
                    new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS.get(), Items.EMERALD, 3, 1, 25, 12, 0.1F));

            event.addTrade(3,
                    new GeneralUtils.BasicItemTrade(BzItems.BEE_BREAD.get(), Items.EMERALD, 3, 6, 12, 17, 0.1F));

            event.addTrade(4,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_CRYSTAL.get(), 2, 1, 10, 22, 0.2F));

            event.addTrade(4,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.BEE_BREAD.get(), 10, 3, 10, 22, 0.2F));

            event.addTrade(5,
                    new GeneralUtils.BasicItemTrade(BzItems.HONEY_BUCKET.get(), Items.EMERALD, 1, 6, 3, 25, 0.2F));

            event.addTrade(5,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEYCOMB_BROOD.get(), 25, 1, 2, 30, 0.2F));

            event.addTrade(5,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_COCOON.get(), 15, 1, 3, 30, 0.2F));

            event.addTrade(5,
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_BUCKET.get(), 20, 1, 2, 30, 0.2F));
        }
    }
}
