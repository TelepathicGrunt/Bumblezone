package com.telepathicgrunt.bumblezone.entities;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public interface IEntityComponent extends Component {
    boolean getIsTeleporting();
    void setIsTeleporting(boolean isTeleporting);

    Identifier getNonBZDimension();
    void setNonBZDimension(Identifier nonBZDimension);

    void setNonBZPos(Vec3d incomingPos);
    Vec3d getNonBZPos();
}