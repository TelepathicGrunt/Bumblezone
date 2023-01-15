package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.events.AddWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.item.Items;

public class WanderingTrades {

    public static void addWanderingTrades(AddWanderingTradesEvent event) {
        event.rare().add(new GeneralUtils.BasicItemTrade(
                Items.EMERALD,
                BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.get(),
                20,
                1,
                1,
                10,
                0.5F
        ));

        event.rare().add(new GeneralUtils.BasicItemTrade(
                Items.EMERALD,
                BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY.get(),
                20,
                1,
                1,
                10,
                0.5F
        ));

        event.rare().add(new GeneralUtils.BasicItemTrade(
                Items.EMERALD,
                BzItems.MUSIC_DISC_LA_BEE_DA_LOCA.get(),
                20,
                1,
                1,
                10,
                0.5F
        ));

        event.rare().add(new GeneralUtils.BasicItemTrade(
                Items.EMERALD,
                BzItems.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES.get(),
                20,
                1,
                1,
                10,
                0.5F
        ));
    }
}