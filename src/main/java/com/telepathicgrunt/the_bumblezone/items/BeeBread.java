package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BeeBread extends Item {
    public BeeBread(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerEntity, LivingEntity entity, InteractionHand playerHand) {
        if(!(entity instanceof Bee) && entity.getType() != BzEntities.BEEHEMOTH)
            return InteractionResult.PASS;

        int currentEffectAmplifier = 0;
        if(entity.hasEffect(BzEffects.BEENERGIZED)) {
            currentEffectAmplifier = Math.min(entity.getEffect(BzEffects.BEENERGIZED).getAmplifier() + 1, 2);
            if(currentEffectAmplifier == 2 && playerEntity instanceof ServerPlayer) {
                BzCriterias.BEENERGIZED_MAXED_TRIGGER.trigger((ServerPlayer) playerEntity);
            }
        }
        entity.addEffect(new MobEffectInstance(BzEffects.BEENERGIZED, 6000, currentEffectAmplifier, true, true, true));

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (!playerEntity.isCreative()) {
            itemstack.shrink(1);
        }

        MiscComponent.onBeesFed(playerEntity);
        playerEntity.swing(playerHand, true);
        return InteractionResult.SUCCESS;
    }
}