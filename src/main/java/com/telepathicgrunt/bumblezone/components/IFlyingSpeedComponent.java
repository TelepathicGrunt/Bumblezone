package com.telepathicgrunt.bumblezone.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;

public interface IFlyingSpeedComponent extends Component {

    void setOriginalFlyingSpeed(float incomingDim);
    float getOriginalFlyingSpeed();
}