package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class BeeBread extends Item {
    public BeeBread(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerEntity, LivingEntity entity, Hand playerHand) {
        if(!(entity instanceof BeeEntity) && entity.getType() != BzEntities.BEEHEMOTH.get())
            return ActionResultType.PASS;

        int currentEffectAmplifier = 0;
        if(entity.hasEffect(BzEffects.BEENERGIZED.get())) {
            currentEffectAmplifier = Math.min(entity.getEffect(BzEffects.BEENERGIZED.get()).getAmplifier() + 1, 2);
            if(currentEffectAmplifier == 2 && playerEntity instanceof ServerPlayerEntity) {
                BzCriterias.BEENERGIZED_MAXED_TRIGGER.trigger((ServerPlayerEntity) playerEntity);
            }
        }
        entity.addEffect(new EffectInstance(BzEffects.BEENERGIZED.get(), 6000, currentEffectAmplifier, true, true, true));

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (!playerEntity.isCreative()) {
            itemstack.shrink(1);
        }

        playerEntity.swing(playerHand, true);
        return ActionResultType.SUCCESS;
    }
}