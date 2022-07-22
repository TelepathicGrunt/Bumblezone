package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Animal.class)
public class AnimalMixin {

    @Inject(method = "spawnChildFromBreeding(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/Animal;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void thebumblezone_animalBred(ServerLevel serverLevel,
                                           Animal animal,
                                           CallbackInfo ci,
                                           AgeableMob ageableMob,
                                           ServerPlayer serverPlayer)
    {
        MiscComponent.onBeeBreed(ageableMob, serverPlayer);
        MiscComponent.onHoneySlimeBred(ageableMob, serverPlayer);
    }
}
