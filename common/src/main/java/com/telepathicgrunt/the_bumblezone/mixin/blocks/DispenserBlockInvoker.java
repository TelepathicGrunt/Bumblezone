package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DispenserBlock.class)
public interface DispenserBlockInvoker {

    @Accessor("DISPENSER_REGISTRY")
    @Contract(pure = true)
    static Map<Item, DispenseItemBehavior> getDISPENSER_REGISTRY() {
        throw new UnsupportedOperationException();
    }
}
