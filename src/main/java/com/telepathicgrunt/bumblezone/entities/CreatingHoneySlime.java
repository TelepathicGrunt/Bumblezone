package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.tags.BzItemTags;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
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
        if (!world.isClientSide() && target.getType().equals(EntityType.SLIME) && BzItemTags.TURN_SLIME_TO_HONEY_SLIME.contains(itemstack.getItem())) {

            Slime slimeEntity = (Slime)target;
            int slimeSize = slimeEntity.getSize();
            HoneySlimeEntity honeySlimeMob = BzEntities.HONEY_SLIME.create(world);
            if(honeySlimeMob == null || slimeSize > 2)
                return InteractionResult.PASS;

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
                itemstack.shrink(1);
                GeneralUtils.givePlayerItem(playerEntity, hand, new ItemStack(item), true);
            }

            playerEntity.swing(hand, true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
