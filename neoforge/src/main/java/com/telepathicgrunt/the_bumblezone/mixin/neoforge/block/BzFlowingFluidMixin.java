package com.telepathicgrunt.the_bumblezone.mixin.neoforge.block;

import com.telepathicgrunt.the_bumblezone.fluids.base.BzFlowingFluid;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.ForgeFluidInfo;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzFlowingFluid.class)
public abstract class BzFlowingFluidMixin extends FlowingFluid {

    @Shadow
    public abstract FluidInfo info();

    @NotNull
    @Override
    public FluidType getFluidType() {
        if (info() instanceof ForgeFluidInfo forgeInfo) {
            return forgeInfo.type();
        }
        return super.getFluidType();
    }
}
