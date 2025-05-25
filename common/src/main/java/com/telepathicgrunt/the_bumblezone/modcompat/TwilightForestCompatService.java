package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.NotImplementedException;

public interface TwilightForestCompatService {

    TwilightForestCompatService INSTANCE = GeneralUtils.loadService(TwilightForestCompatService.class);

    default CompoundTag getPersistentData(Entity entity) {
        throw new NotImplementedException("TwilightForestCompat getPesistentData is not implemented!");
    }
}
