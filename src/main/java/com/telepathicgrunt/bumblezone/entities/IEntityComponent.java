package com.telepathicgrunt.bumblezone.entities;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public interface IEntityComponent extends Component {
    boolean getIsTeleporting();
    void setIsTeleporting(boolean isTeleporting);

    ResourceLocation getNonBZDimension();
    void setNonBZDimension(ResourceLocation nonBZDimension);

    void setNonBZPos(Vec3 incomingPos);
    Vec3 getNonBZPos();
}