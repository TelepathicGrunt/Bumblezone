package com.telepathicgrunt.the_bumblezone.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.EntityMiscHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class StingerSpearItem extends TridentItem {
    public static final float BASE_DAMAGE = 1F;
    public static final float BASE_THROWN_DAMAGE = 1.5F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public StingerSpearItem(Properties properties) {
        super(properties.durability(220));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BASE_DAMAGE, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -1F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 50000;
    }

    /**
     * Specify what item can repair this weapon
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.STINGER_SPEAR_REPAIR_ITEMS);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int currentDuration) {
        if (livingEntity instanceof Player player) {
            int remainingDuration = this.getUseDuration(itemStack) - currentDuration;
            if (remainingDuration >= 10) {
                if (!level.isClientSide) {
                    itemStack.hurtAndBreak(1, player, playerx -> playerx.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                    ThrownStingerSpearEntity thrownStingerSpear = new ThrownStingerSpearEntity(level, player, itemStack);
                    thrownStingerSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
                    if (player.getAbilities().instabuild) {
                        thrownStingerSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    level.addFreshEntity(thrownStingerSpear);
                    level.playSound(null, thrownStingerSpear, BzSounds.STINGER_SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(itemStack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemStack);
        } else {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity enemy, LivingEntity user) {
        int potentPoisonLevel = itemStack.getEnchantmentLevel(BzEnchantments.POTENT_POISON.get());
        if (enemy.getMobType() != MobType.UNDEAD) {
            enemy.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    100 + 100 * (potentPoisonLevel - ((potentPoisonLevel - 1) / 2)),
                    potentPoisonLevel, // 0, 1, 2, 3
                    true,
                    true,
                    true));

            if (user instanceof ServerPlayer serverPlayer) {
                BzCriterias.STINGER_SPEAR_POISONING_TRIGGER.trigger(serverPlayer);
            }
        }

        int durabilityDecrease = 1;
        if (user instanceof Player) {
            int neuroToxinLevel = itemStack.getEnchantmentLevel(BzEnchantments.NEUROTOXINS.get());
            if (neuroToxinLevel > 0) {
                durabilityDecrease = 5;
            }
        }

        itemStack.hurtAndBreak(durabilityDecrease, user, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

        if (user instanceof ServerPlayer serverPlayer &&
                enemy.getType() == EntityType.WITHER &&
                enemy.isDeadOrDying() &&
                EntityMiscHandler.rootAdvancementDone(serverPlayer)) {
            BzCriterias.STINGER_SPEAR_KILLED_WITH_WITHER_TRIGGER.trigger(serverPlayer);
        }

        return true;
    }

    /**
     * blacklisted riptide and channeling enchantment
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.CHANNELING || enchantment == Enchantments.RIPTIDE) {
            return false;
        }

        if (enchantment == Enchantments.SMITE) {
            return true;
        }

        return enchantment.category.canEnchant(stack.getItem());
    }
}
