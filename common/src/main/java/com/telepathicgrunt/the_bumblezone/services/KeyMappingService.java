package com.telepathicgrunt.the_bumblezone.services;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;

public interface KeyMappingService {

    KeyMappingService INSTANCE = GeneralUtils.loadService(KeyMappingService.class);

    default KeyMapping createBeehemothKey(String display, BeehemothControls.KeyConflict conflict, InputConstants.Key key, String category) {
        throw new NotImplementedException();
    }
}
