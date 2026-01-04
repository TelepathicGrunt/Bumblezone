package com.telepathicgrunt.the_bumblezone.loot;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.loot.LootContextAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.Optional;

public final class NewLootInjectorApplier {
    private NewLootInjectorApplier() {}

    public static final ResourceKey<LootTable> VANILLA_FISHING_LOOT_TABLE_RK = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("minecraft", "gameplay/fishing"));
    public static final Identifier BZ_DIMENSION_FISHING_LOOT_TABLE_RL = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "gameplay/fishing");
    public static final Identifier STINGER_DROP_LOOT_TABLE_RL = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "entities/bee_stinger_drops");

    public static boolean checkIfInjectBeeStingerLoot(LootContext context) {
        if (BzGeneralConfigs.beeLootInjection || BzGeneralConfigs.moddedBeeLootInjection) {
            if(context.hasParameter(LootContextParams.THIS_ENTITY)) {
                if (context.getParameter(LootContextParams.THIS_ENTITY) instanceof Bee bee) {
                    if (!((LootParamsBzVisitedLootInterface)((LootContextAccessor)context).bumblezone$getParams()).getVisitedBzVisitedLootRL().contains(STINGER_DROP_LOOT_TABLE_RL) &&
                        !((EntityLootDropInterface)bee).thebumblezone_hasPerformedEntityDrops())
                    {
                        Identifier beeRL = BuiltInRegistries.ENTITY_TYPE.getKey(bee.getType());
                        return (BzGeneralConfigs.beeLootInjection && beeRL.getNamespace().equals("minecraft")) ||
                                (BzGeneralConfigs.moddedBeeLootInjection && !beeRL.getNamespace().equals("minecraft"));
                    }
                }
            }
        }

        return false;
    }

    public static boolean checkIfValidForDimensionFishingLoot(LootContext context) {
        if (!((LootParamsBzVisitedLootInterface)((LootContextAccessor)context).bumblezone$getParams()).getVisitedBzVisitedLootRL().contains(NewLootInjectorApplier.BZ_DIMENSION_FISHING_LOOT_TABLE_RL)) {
            return context.getLevel().dimension().equals(BzDimension.BZ_WORLD_KEY);
        }

        return false;
    }

    public static void injectLoot(LootContext context, List<ItemStack> originalLoot, Identifier lootTableToPullFrom) {
        Optional<Holder.Reference<LootTable>> optionalLootTableReference = context.getResolver().getOrThrow(Registries.LOOT_TABLE).value().get(ResourceKey.create(Registries.LOOT_TABLE, lootTableToPullFrom));
        if (optionalLootTableReference.isEmpty()) {
            return;
        }

        LootTable stingerLootTable = optionalLootTableReference.get().value();
        ((LootParamsBzVisitedLootInterface)((LootContextAccessor)context).bumblezone$getParams()).addVisitedBzVisitedLootRL(lootTableToPullFrom);
        ObjectArrayList<ItemStack> newItems = new ObjectArrayList<>();
        stingerLootTable.getRandomItems(((LootContextAccessor)context).bumblezone$getParams(), newItems::add);
        originalLoot.addAll(newItems);

        if (context.hasParameter(LootContextParams.THIS_ENTITY)) {
            Entity entity = context.getParameter(LootContextParams.THIS_ENTITY);
            if (entity != null) {
                ((EntityLootDropInterface)entity).thebumblezone_performedEntityDrops();
            }
        }
    }
}
