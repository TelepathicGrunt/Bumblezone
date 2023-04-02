package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.loot;

import com.telepathicgrunt.the_bumblezone.loot.NewLootInjectorApplier;
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

@Mixin(LootTable.class)
public class LootTableMixin {

    /**
     * Allow us to add bee stinger drops to any entity that extends Bee
     * @author TelepathicGrunt
     */
    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
            at = @At(value = "TAIL"))
    private void bumblezone_injectStingerLootForBees(LootContext lootContext, Consumer<ItemStack> consumer, CallbackInfo ci) {
        if (NewLootInjectorApplier.checkIfInjectLoot(lootContext)) {
            List<ItemStack> itemStacks = new ObjectArrayList<>();
            NewLootInjectorApplier.injectLoot(lootContext, itemStacks);
            if (!itemStacks.isEmpty()) {
                itemStacks.forEach(consumer);
            }
        }
    }
}
