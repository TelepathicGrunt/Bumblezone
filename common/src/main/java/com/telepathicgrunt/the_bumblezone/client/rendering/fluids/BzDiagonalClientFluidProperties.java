package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.teamresourceful.resourcefullib.client.fluid.data.ClientFluidProperties;
import net.minecraft.client.resources.model.sprite.Material;

public interface BzDiagonalClientFluidProperties extends ClientFluidProperties {
    default Material flowingDiagonal() {
        return null;
    }
}