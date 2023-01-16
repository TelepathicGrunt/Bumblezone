package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import vazkii.quark.content.client.tooltip.EnchantedBookTooltips;

public class QuarkCompat implements ModCompat {

    public QuarkCompat() {
        if(BzModCompatibilityConfigs.injectBzItemsIntoQuarkEnchantmentTooltipsCompat) {
            setupEnchantmentTooltipItemsSymbols();
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.quarkPresent = true;
    }

    public static void setupEnchantmentTooltipItemsSymbols() {
        EnchantedBookTooltips.getTestItems().add(BzItems.STINGER_SPEAR.get().getDefaultInstance());
        EnchantedBookTooltips.getTestItems().add(BzItems.CRYSTAL_CANNON.get().getDefaultInstance());
        EnchantedBookTooltips.getTestItems().add(BzItems.HONEY_CRYSTAL_SHIELD.get().getDefaultInstance());
        EnchantedBookTooltips.getTestItems().add(BzItems.STINGLESS_BEE_HELMET_2.get().getDefaultInstance());
        EnchantedBookTooltips.getTestItems().add(BzItems.BUMBLE_BEE_CHESTPLATE_2.get().getDefaultInstance());
        EnchantedBookTooltips.getTestItems().add(BzItems.HONEY_BEE_LEGGINGS_2.get().getDefaultInstance());
        EnchantedBookTooltips.getTestItems().add(BzItems.CARPENTER_BEE_BOOTS_2.get().getDefaultInstance());
    }
}
