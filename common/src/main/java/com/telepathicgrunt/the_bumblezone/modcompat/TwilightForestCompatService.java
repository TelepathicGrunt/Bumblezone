package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.entity.Entity;

public interface TwilightForestCompatService {
    TwilightForestCompatService INSTANCE = GeneralUtils.loadService(TwilightForestCompatService.class);

    boolean getIsEnderArrow(Entity entity);
}
