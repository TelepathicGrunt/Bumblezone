package com.telepathicgrunt.the_bumblezone.loot;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.loot.conditions.BumblezoneDimensionCheck;
import com.telepathicgrunt.the_bumblezone.loot.conditions.DyingBeeEntitiesDrops;
import com.telepathicgrunt.the_bumblezone.loot.conditions.NonBumblezoneDimensionCheck;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableModifier {
    public static void modifyLootTables() {

        // Bee Stinger
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) ->
            tableBuilder.withPool(new LootPool.Builder()
                .when(DyingBeeEntitiesDrops::new)
                .setRolls(ConstantValue.exactly(1))
                .add(LootTableReference.lootTableReference(NewLootInjectorApplier.STINGER_DROP_LOOT_TABLE_RL))
        ));

        // Fishing Loot
        ResourceLocation laterPhase = new ResourceLocation(Bumblezone.MODID, "later_phase");
        LootTableEvents.MODIFY.addPhaseOrdering(Event.DEFAULT_PHASE, laterPhase);
        LootTableEvents.MODIFY.register(laterPhase, (resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(NewLootInjectorApplier.VANILLA_FISHING_LOOT_TABLE_RL)) {
                tableBuilder.modifyPools((builder) -> builder
                    .when(NonBumblezoneDimensionCheck::new));
            }
        });
        ResourceLocation laterPhase2 = new ResourceLocation(Bumblezone.MODID, "later_phase_2");
        LootTableEvents.MODIFY.addPhaseOrdering(laterPhase, laterPhase2);
        LootTableEvents.MODIFY.register(laterPhase2, (resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(NewLootInjectorApplier.VANILLA_FISHING_LOOT_TABLE_RL)) {
                tableBuilder.withPool(new LootPool.Builder()
                    .when(BumblezoneDimensionCheck::new)
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootTableReference.lootTableReference(NewLootInjectorApplier.BZ_DIMENSION_FISHING_LOOT_TABLE_RL))
                );
            }
        });
    }
}
