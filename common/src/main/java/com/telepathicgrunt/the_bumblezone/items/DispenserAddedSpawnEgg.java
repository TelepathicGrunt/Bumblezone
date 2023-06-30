package com.telepathicgrunt.the_bumblezone.items;


import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.SetupEvent;
import com.telepathicgrunt.the_bumblezone.mixin.items.SpawnEggItemAccessor;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DispenserAddedSpawnEgg extends SpawnEggItem {

    private static final List<Pair<Supplier<? extends EntityType<? extends Mob>>, SpawnEggItem>> SPAWN_EGGS = new ArrayList<>();
    private final Supplier<? extends EntityType<? extends Mob>> entityType;

    public DispenserAddedSpawnEgg(Supplier<? extends EntityType<? extends Mob>> typeIn, int primaryColorIn, int secondaryColorIn, Item.Properties builder) {
        super(null, primaryColorIn, secondaryColorIn, builder);
        this.entityType = typeIn;

        setupDispenserBehavior();
        SPAWN_EGGS.add(new Pair<>(typeIn, this));
    }

    protected void setupDispenserBehavior() {
        // Have to manually add dispenser behavior due to forge item registry event running too late.
        DispenserBlock.registerBehavior(
                this,
                new DefaultDispenseItemBehavior() {
                    public ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                        EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                        entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                        stack.shrink(1);
                        return stack;
                    }
                });
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundTag compoundTag) {
        if (compoundTag != null && compoundTag.contains("EntityTag", 10)) {
            CompoundTag compoundTag2 = compoundTag.getCompound("EntityTag");
            if (compoundTag2.contains("id", 8)) {
                return EntityType.byString(compoundTag2.getString("id")).orElseGet(this.entityType);
            }
        }

        return this.entityType.get();
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return getType(null).requiredFeatures();
    }

    public static void onSetup(SetupEvent event) {
        var spawnEggMap = SpawnEggItemAccessor.bz$getIdMap();
        for (var entry : DispenserAddedSpawnEgg.SPAWN_EGGS) {
            spawnEggMap.put(entry.getFirst().get(), entry.getSecond());
        }
    }
}