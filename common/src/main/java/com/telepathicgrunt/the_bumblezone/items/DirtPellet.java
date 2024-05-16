package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.DirtPelletEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class DirtPellet extends Item {
    public DirtPellet(Properties properties) {
        super(properties);

        DispenserBlock.registerProjectileBehavior(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        ItemStack itemstack = playerEntity.getItemInHand(hand);

        if (!world.isClientSide()) {
            world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), BzSounds.DIRT_PELLET_THROW.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (playerEntity.getRandom().nextFloat() * 0.4F + 0.8F));
                DirtPelletEntity pelletEntity = new DirtPelletEntity(world, playerEntity);
                pelletEntity.setItem(itemstack);
                pelletEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 1.5F, 1.0F);
                world.addFreshEntity(pelletEntity);

            playerEntity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerEntity.isCreative()) {
                itemstack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
    }
}