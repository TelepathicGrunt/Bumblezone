package net.telepathicgrunt.bumblezone.entities;

import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public interface IPlayerComponent extends Component {
    boolean getIsTeleporting();
    void setIsTeleporting(boolean isTeleporting);

    ResourceLocation getNonBZDimension();
    void setNonBZDimension(ResourceLocation nonBZDimension);

    void setNonBZPos(Vec3d incomingPos);
    Vec3d getNonBZPos();
}