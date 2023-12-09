package com.telepathicgrunt.the_bumblezone.mixin.fabric.loot;


import com.telepathicgrunt.the_bumblezone.loot.NewLootInjectorApplier;
import com.telepathicgrunt.the_bumblezone.loots.LootUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = LootTable.class, priority = 900)
public class LootTableMixin {

    /**
     * Allow us to add bee stinger drops to any entity that extends Bee
     * @author TelepathicGrunt
     */
    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
            at = @At(value = "TAIL"))
    private void bumblezone_injectStingerLootForBees(LootContext lootContext, Consumer<ItemStack> consumer, CallbackInfo ci) {
        if (NewLootInjectorApplier.checkIfInjectBeeStingerLoot(lootContext)) {

            if (LootUtils.isEntityLootTable(lootContext.getLevel().getServer(), ((LootTable)(Object)this))) {
                List<ItemStack> itemStacks = new ObjectArrayList<>();
                NewLootInjectorApplier.injectLoot(lootContext, itemStacks, NewLootInjectorApplier.STINGER_DROP_LOOT_TABLE_RL);
                if (!itemStacks.isEmpty()) {
                    itemStacks.forEach(consumer);
                }
            }
        }
    }

    /**
     * Allow us to do special fishing loot for Bumblezone dimension only
     * @author TelepathicGrunt
     */
    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone_switchToDimensionFishingLoot(LootContext lootContext, Consumer<ItemStack> consumer, CallbackInfo ci) {
        if (NewLootInjectorApplier.checkIfValidForDimensionFishingLoot(lootContext) &&
            lootContext.getLevel().getServer().getLootData().getLootTable(NewLootInjectorApplier.VANILLA_FISHING_LOOT_TABLE_RL) == ((LootTable)(Object)this))
        {
            ObjectArrayList<ItemStack> newItems = new ObjectArrayList<>();
            NewLootInjectorApplier.injectLoot(lootContext, newItems, NewLootInjectorApplier.BZ_DIMENSION_FISHING_LOOT_TABLE_RL);
            if (!newItems.isEmpty()) {
                newItems.forEach(consumer);
                ci.cancel();
            }
        }
    }
}