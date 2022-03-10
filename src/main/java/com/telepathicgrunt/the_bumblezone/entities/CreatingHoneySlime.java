package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class CreatingHoneySlime {
    // Spawn honey slime instead of passed in entity
    public static InteractionResult createHoneySlime(Level world, Player playerEntity, InteractionHand hand, Entity target) {
        ItemStack itemstack = playerEntity.getItemInHand(hand);
        if (target.getType().equals(EntityType.SLIME) && itemstack.is(BzTags.TURN_SLIME_TO_HONEY_SLIME)) {

            Slime slimeEntity = (Slime)target;
            int slimeSize = slimeEntity.getSize();
            HoneySlimeEntity honeySlimeMob = BzEntities.HONEY_SLIME.get().create(world);
            if(honeySlimeMob == null || slimeSize > 2)
                return InteractionResult.PASS;

            if(world.isClientSide())
                return InteractionResult.SUCCESS;

            honeySlimeMob.moveTo(
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    target.getYRot(),
                    target.getXRot());

            honeySlimeMob.setBaby(slimeSize == 1);
            honeySlimeMob.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(new BlockPos(honeySlimeMob.position())), MobSpawnType.TRIGGERED, null, null);
            // spawn honey slime
            world.addFreshEntity(honeySlimeMob);

            // remove original slime
            target.discard();

            world.playSound(
                    playerEntity,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    SoundEvents.SLIME_BLOCK_STEP,
                    SoundSource.NEUTRAL,
                    1.0F,
                    1.0F);

            if (!playerEntity.isCreative()) {
                // remove current honey item
                Item item = itemstack.getItem();
                GeneralUtils.givePlayerItem(playerEntity, hand, new ItemStack(item), true, true);
            }

            playerEntity.swing(hand, true);
            if(playerEntity instanceof ServerPlayer) {
                BzCriterias.HONEY_SLIME_CREATION_TRIGGER.trigger((ServerPlayer) playerEntity);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
