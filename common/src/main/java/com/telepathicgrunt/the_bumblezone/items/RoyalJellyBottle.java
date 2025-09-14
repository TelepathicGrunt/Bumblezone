package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RoyalJellyBottle extends Item {

    public RoyalJellyBottle(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerEntity, LivingEntity entity, InteractionHand playerHand) {
        if(!(entity instanceof Bee) && entity.getType() != BzEntities.BEEHEMOTH.get())
            return InteractionResult.PASS;

        entity.addEffect(new MobEffectInstance(BzEffects.BEENERGIZED.holder(), 24000, 3, true, true, true));
        if (playerEntity instanceof ServerPlayer serverPlayer) {
            BzCriterias.BEENERGIZED_MAXED_TRIGGER.get().trigger(serverPlayer);
        }

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (!playerEntity.isCreative()) {
            itemstack.shrink(1);
        }

        PlayerDataHandler.onBeesFed(playerEntity);
        playerEntity.swing(playerHand, true);
        return InteractionResult.SUCCESS;
    }
}