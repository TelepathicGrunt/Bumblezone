package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerEntity, LivingEntity entity, InteractionHand playerHand) {
        if (!(entity instanceof Bee) && entity.getType() != BzEntities.BEEHEMOTH.get()) {
            return InteractionResult.PASS;
        }

        grantStackingBeenergized(playerEntity, entity);

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (!playerEntity.isCreative()) {
            itemstack.shrink(1);
        }

        PlayerDataHandler.onBeesFed(playerEntity);
        playerEntity.swing(playerHand, true);
        return InteractionResult.SUCCESS;
    }

    public static void grantStackingBeenergized(Player playerEntity, LivingEntity fedEntity) {
        int currentEffectAmplifier = 0;
        if (fedEntity.hasEffect(BzEffects.BEENERGIZED.holder())) {
            currentEffectAmplifier = Math.min(fedEntity.getEffect(BzEffects.BEENERGIZED.holder()).getAmplifier() + 1, 2);
            if (currentEffectAmplifier == 2 && playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.BEENERGIZED_MAXED_TRIGGER.get().trigger(serverPlayer);
            }
        }
        fedEntity.addEffect(new MobEffectInstance(BzEffects.BEENERGIZED.holder(), 6000, currentEffectAmplifier, true, true, true));
    }
}