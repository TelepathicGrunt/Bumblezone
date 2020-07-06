package net.telepathicgrunt.bumblezone.entities;

import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public interface IPlayerComponent extends Component {
    boolean getIsTeleporting();
    void setIsTeleporting(boolean isTeleporting);

    Identifier getNonBZDimension();
    void setNonBZDimension(Identifier nonBZDimension);

    void setNonBZPos(Vec3d incomingPos);
    Vec3d getNonBZPos();
}