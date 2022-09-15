package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.components.EssenceComponent;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EssenceOfTheBees extends Item {

    public EssenceOfTheBees(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (hasEssence(serverPlayer)) {
                Component message = Component.translatable("item.the_bumblezone.essence_of_the_bees.already_essenced").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD);
                serverPlayer.displayClientMessage(message, true);
                return itemStack;
            }

            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);

            setEssence(serverPlayer, true);
            if (!serverPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            spawnParticles((ServerLevel) level, serverPlayer.position(), serverPlayer.getRandom());
            level.playSound(
                    null,
                    serverPlayer.blockPosition(),
                    BzSounds.BEE_ESSENCE_CONSUMED,
                    SoundSource.PLAYERS,
                    2F,
                    (serverPlayer.getRandom().nextFloat() * 0.2F) + 0.6F);
        }
        return itemStack;
    }

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random) {
        world.sendParticles(
                ParticleTypes.FIREWORK,
                location.x(),
                location.y() + 1,
                location.z(),
                100,
                random.nextGaussian() * 0.1D,
                (random.nextGaussian() * 0.1D) + 0.1,
                random.nextGaussian() * 0.1D,
                random.nextFloat() * 0.4 + 0.2f);
        world.sendParticles(
                ParticleTypes.ENCHANT,
                location.x(),
                location.y() + 1,
                location.z(),
                400,
                1,
                1,
                1,
                random.nextFloat() * 0.5 + 1.2f);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 150;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return BzSounds.BEE_ESSENCE_CONSUMING;
    }

    @Override
    public SoundEvent getEatingSound() {
        return BzSounds.BEE_ESSENCE_CONSUMING;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    public static void setEssence(ServerPlayer serverPlayer, boolean newValue) {
        EssenceComponent capability = Bumblezone.ESSENCE_COMPONENT.get(serverPlayer);
        capability.isBeeEssenced = newValue;
    }

    public static boolean hasEssence(ServerPlayer serverPlayer) {
        EssenceComponent capability = Bumblezone.ESSENCE_COMPONENT.get(serverPlayer);
        return capability.isBeeEssenced;
    }
}