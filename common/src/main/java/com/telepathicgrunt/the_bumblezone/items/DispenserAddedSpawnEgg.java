package com.telepathicgrunt.the_bumblezone.items;


import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.SetupEvent;
import com.telepathicgrunt.the_bumblezone.mixin.items.SpawnEggItemAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DispenserAddedSpawnEgg extends SpawnEggItem {
    private static final MapCodec<EntityType<?>> ENTITY_TYPE_FIELD_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id");

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
                        Direction direction = source.state().getValue(DispenserBlock.FACING);
                        EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack);
                        entitytype.spawn(source.level(), stack, null, source.pos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                        stack.shrink(1);
                        return stack;
                    }
                });
    }

    @Override
    public EntityType<?> getType(ItemStack itemStack) {
        CustomData customData = itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        return !customData.isEmpty() ? customData.read(ENTITY_TYPE_FIELD_CODEC).result().orElse(this.entityType.get()) : this.entityType.get();
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return getType(ItemStack.EMPTY).requiredFeatures();
    }

    public static void onSetup(SetupEvent event) {
        var spawnEggMap = SpawnEggItemAccessor.bz$getIdMap();
        for (var entry : DispenserAddedSpawnEgg.SPAWN_EGGS) {
            spawnEggMap.put(entry.getFirst().get(), entry.getSecond());
        }
    }
}