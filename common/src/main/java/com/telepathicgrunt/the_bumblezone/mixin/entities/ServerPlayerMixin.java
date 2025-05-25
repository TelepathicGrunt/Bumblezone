package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import com.telepathicgrunt.the_bumblezone.worldgen.structures.SempiternalSanctumBehavior;
import com.telepathicgrunt.the_bumblezone.worldgen.structures.ThronePillarBehavior;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "doTick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/PlayerTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void bumblezone$checkIfInSpecialStructures(CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer)(Object)this;
        BeeAggression.applyAngerIfInTaggedStructures(serverPlayer);
        ThronePillarBehavior.applyFatigueAndSpawningBeeQueen(serverPlayer);
        SempiternalSanctumBehavior.runStructureMessagesAndFatigue(serverPlayer);
    }


    @Inject(method = "triggerRecipeCrafted(Lnet/minecraft/world/item/crafting/Recipe;Ljava/util/List;)V",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void bumblezone$hookToRecipeCrafting(Recipe<?> recipe, List<ItemStack> list, CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer)(Object)this;
        ItemStack createdItem = recipe.getResultItem(serverPlayer.level().registryAccess());

        if (createdItem.getItem() instanceof BlockItem blockItem &&
            blockItem.getBlock() instanceof BeehiveBlock &&
            PlayerDataHandler.rootAdvancementDone(serverPlayer))
        {
            ModuleHelper.getModule(serverPlayer, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.craftedBeehives++;
                BzCriterias.BEEHIVE_CRAFTED_TRIGGER.trigger(serverPlayer, module.craftedBeehives);
            });
        }
    }
}