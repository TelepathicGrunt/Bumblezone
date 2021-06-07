package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;

public class HoneySlimeSpawnEgg extends SpawnEggItem {
    public HoneySlimeSpawnEgg(EntityType<?> typeIn, int primaryColorIn, int secondaryColorIn, Properties builder) {
        super(typeIn, primaryColorIn, secondaryColorIn, builder);

        // Have to manually add dispenser behavior due to forge item registry event running too late.
        DispenserBlock.registerBehavior(
            this,
            new DefaultDispenseItemBehavior() {
                public ItemStack execute(IBlockSource source, ItemStack stack) {
                    Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                    EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                    entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                    stack.shrink(1);
                    return stack;
                }
            });
    }
}