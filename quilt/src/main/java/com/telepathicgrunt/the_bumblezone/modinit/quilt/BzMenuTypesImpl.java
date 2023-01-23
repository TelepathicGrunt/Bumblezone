package com.telepathicgrunt.the_bumblezone.modinit.quilt;

import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class BzMenuTypesImpl {
    public static <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator) {
        return new MenuType<>(creator::create);
    }
}
