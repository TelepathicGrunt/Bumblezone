package com.telepathicgrunt.the_bumblezone.items;


import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

//TODO switch to custom class
public class DispenserAddedSpawnEgg extends ForgeSpawnEggItem {
    public DispenserAddedSpawnEgg(Supplier<? extends EntityType<? extends Mob>> typeIn, int primaryColorIn, int secondaryColorIn, Item.Properties builder) {
        super(typeIn, primaryColorIn, secondaryColorIn, builder);

        // Have to manually add dispenser behavior due to forge item registry event running too late.
        DispenserBlock.registerBehavior(
            this,
            new DefaultDispenseItemBehavior() {
                public ItemStack execute(BlockSource source, ItemStack stack) {
                    Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                    EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                    entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                    stack.shrink(1);
                    return stack;
                }
            });
    }
}