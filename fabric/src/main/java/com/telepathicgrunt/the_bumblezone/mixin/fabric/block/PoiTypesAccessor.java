package com.telepathicgrunt.the_bumblezone.mixin.fabric.block;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PoiTypes.class)
public interface PoiTypesAccessor {
    @Invoker("registerBlockStates")
    static void callRegisterBlockStates(Holder<PoiType> holder, Set<BlockState> set) {
        throw new UnsupportedOperationException();
    }
}
