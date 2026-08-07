package com.telepathicgrunt.the_bumblezone.items;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.enchantments.PotentPoisonEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.ParalyzeMarker;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StingerSpearItem extends TridentItem implements ItemExtension {
    public static final float BASE_DAMAGE = 1F;
    public static final float BASE_THROWN_DAMAGE = 1.5F;

    public StingerSpearItem(Properties properties) {
        super(properties
                .stacksTo(1)
                .durability(220)
                .repairable(BzTags.STINGER_SPEAR_REPAIR_ITEMS)
                .enchantable(1)
                .component(DataComponents.TOOL, createToolProperties())
                .component(DataComponents.WEAPON, new Weapon(1))
                .attributes(StingerSpearItem.createAttributes())
                .rarity(Rarity.UNCOMMON));
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2, false);
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_ID, BASE_DAMAGE, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_SPEED_ID, -1F, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 50000;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int currentDuration) {
        if (livingEntity instanceof Player player) {
            int remainingDuration = this.getUseDuration(itemStack, player) - currentDuration;
            if (remainingDuration >= 10) {
                if (level instanceof ServerLevel serverLevel) {
                    itemStack.hurtWithoutBreaking(1, player);
                    ThrownStingerSpearEntity thrownStingerSpear = Projectile.spawnProjectileFromRotation(ThrownStingerSpearEntity::new, serverLevel, itemStack, player, 0.0F, 3.0F, 1.0F);
                    if (player.getAbilities().instabuild) {
                        thrownStingerSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    level.playSound(null, thrownStingerSpear, BzSounds.STINGER_SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(itemStack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                return true;
            }
        }

        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
            return InteractionResult.FAIL;
        }
        else {
            player.startUsingItem(interactionHand);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity victim, LivingEntity user) {
        int durabilityDecrease = 1;

        if (!victim.is(EntityTypeTags.UNDEAD)) {
            boolean potentPoisonApplied = PotentPoisonEnchantmentApplication.doPostAttackBoostedPoison(itemStack, victim);
            if (!potentPoisonApplied) {
                victim.addEffect(new MobEffectInstance(
                        MobEffects.POISON,
                        100,
                        0,
                        false,
                        true,
                        true));
            }

            if (user instanceof ServerPlayer serverPlayer) {
                BzCriterias.STINGER_SPEAR_POISONING_TRIGGER.get().trigger(serverPlayer);
            }

            if (!victim.is(BzTags.PARALYZED_IMMUNE)) {
                Pair<ParalyzeMarker, Integer> neurotoxin = NeurotoxinsEnchantmentApplication.getNeurotoxinEnchantLevel(itemStack);
                if (neurotoxin != null && neurotoxin.getSecond() > 0) {
                    durabilityDecrease = neurotoxin.getFirst().durabilityDrainOnValidTargetHit();
                }
            }
        }

        itemStack.hurtAndBreak(durabilityDecrease, user, EquipmentSlot.MAINHAND);

        if (user instanceof ServerPlayer serverPlayer &&
            victim.getType() == EntityType.WITHER &&
            victim.isDeadOrDying() &&
            PlayerDataHandler.rootAdvancementDone(serverPlayer))
        {
            BzCriterias.STINGER_SPEAR_KILLED_WITH_WITHER_TRIGGER.get().trigger(serverPlayer);
        }
    }

    @Override
    public TriState bz$canEnchant(ItemStack itemstack, Holder<Enchantment> enchantment) {
        if (enchantment.is(BzTags.ENCHANTABLES_STINGER_SPEAR_FORCED_DISALLOWED)) {
            return TriState.DENY;
        }

        return enchantment.is(BzTags.ENCHANTABLES_STINGER_SPEAR_EXTRA_ALLOWED) ? TriState.ALLOW : TriState.PASS;
    }
}
