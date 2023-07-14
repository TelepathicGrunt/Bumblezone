package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.StinglessBeeHelmetSightPacket;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Set;

public class StinglessBeeHelmet extends BeeArmor {
    public static int HELMET_EFFECT_COUNTER_CLIENTSIDE = 0;
    public static int BEE_WEARABLES_COUNT = 0;
    public static Set<Entity> BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE = new ObjectArraySet<>();
    public static int PACKET_SEND_COOLDOWN_CLIENTSIDE = 20;

    public StinglessBeeHelmet(ArmorMaterial material, ArmorItem.Type armorType, Properties properties, int variant) {
        super(material, armorType, properties, variant, false);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.BEE_ARMOR_REPAIR_ITEMS);
    }

    @Override
    public void bz$onArmorTick(ItemStack itemstack, Level world, Player player) {
        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        MobEffectInstance nausea = player.getEffect(MobEffects.CONFUSION);
        if (nausea != null) {
            int decreaseSpeed = (int)((beeWearablesCount * 3d) - 2);
            for (int i = 0; i < decreaseSpeed; i++) {
                ((MobEffectInstanceAccessor) nausea).callTickDownDuration();
                if(!world.isClientSide() &&
                    player.getRandom().nextFloat() < 0.001f &&
                    itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
                {
                    itemstack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.HEAD));
                }
            }
        }

        MobEffectInstance poison = player.getEffect(MobEffects.POISON);
        if (poison != null && (beeWearablesCount >= 4 || world.getGameTime() % (beeWearablesCount == 3 ? 2 : 3) == 0)) {
            for (int i = 0; i <= Math.max(beeWearablesCount - 3, 1); i++) {
                if (poison.getDuration() > 0) {
                    ((MobEffectInstanceAccessor) poison).callTickDownDuration();
                }
            }
            if(!world.isClientSide() &&
                player.getRandom().nextFloat() < 0.004f &&
                itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
            {
                itemstack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.HEAD));
            }
        }

        if (world.isClientSide()) {
            PACKET_SEND_COOLDOWN_CLIENTSIDE--;
            if(PACKET_SEND_COOLDOWN_CLIENTSIDE == 0) {
                PACKET_SEND_COOLDOWN_CLIENTSIDE = 20;
                if(BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.size() >= 60) {
                    StinglessBeeHelmetSightPacket.sendToServer(true);
                }
            }
            BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.clear();
            StinglessBeeHelmet.BEE_WEARABLES_COUNT = beeWearablesCount;

            if (player.isShiftKeyDown() && player.onGround()) {
                HELMET_EFFECT_COUNTER_CLIENTSIDE = ((beeWearablesCount - 1) * 65) + 6;

                if(!world.isClientSide() && player.getRandom().nextFloat() < 0.001f) {
                    itemstack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.HEAD));
                }
            }
        }

        CompoundTag tag = itemstack.getOrCreateTag();
        boolean hasBeeRider = tag.getBoolean("hasBeeRider");
        int beeRidingTimer = tag.getInt("beeRidingTimer");
        boolean hasWrath = player.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get());
        if(hasBeeRider || hasWrath) {
            if (hasWrath ||
                player.isUnderWater() ||
                player.isHurt() ||
                player.isShiftKeyDown() ||
                (beeWearablesCount < 4 && beeRidingTimer > beeWearablesCount * 600))
            {
                for (Entity passenger : player.getPassengers()) {
                    if ((passenger instanceof Bee && !passenger.getType().is(BzTags.STINGLESS_BEE_HELMET_DISALLOWED_PASSENGERS)) ||
                        passenger.getType().is(BzTags.STINGLESS_BEE_HELMET_FORCED_ALLOWED_PASSENGERS))
                    {
                        passenger.stopRiding();
                        if (passenger instanceof Mob mob) {
                            mob.setNoAi(false);
                        }
                    }
                }
                if(!world.isClientSide()) {
                    tag.putBoolean("hasBeeRider", false);
                    tag.putInt("beeRidingTimer", 0);
                }
            }
            else if(!world.isClientSide()) {
                tag.putInt("beeRidingTimer", beeRidingTimer + 1);

                if(player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(BzStats.STINGLESS_BEE_HELMET_BEE_RIDER_RL.get());
                }
            }
        }
    }

    public static boolean shouldEntityGlow(Player player, Entity entity) {
        if(entity instanceof Bee || entity instanceof BeehemothEntity || entity instanceof BeeQueenEntity) {
            return entity.blockPosition().closerThan(player.blockPosition(), (BEE_WEARABLES_COUNT * 16.5d) + 14);
        }
        return false;
    }

    public static InteractionResult addBeePassenger(Level world, Player playerEntity, InteractionHand hand, Entity entity) {
        ItemStack beeHelmet = StinglessBeeHelmet.getEntityBeeHelmet(playerEntity);
        if (!beeHelmet.isEmpty() &&
            !playerEntity.isShiftKeyDown() &&
            playerEntity.getItemInHand(playerEntity.getUsedItemHand()).isEmpty() &&
            playerEntity.getPassengers().isEmpty())
        {
            if ((entity instanceof Bee && !entity.getType().is(BzTags.STINGLESS_BEE_HELMET_DISALLOWED_PASSENGERS)) ||
                entity.getType().is(BzTags.STINGLESS_BEE_HELMET_FORCED_ALLOWED_PASSENGERS))
            {
                entity.startRiding(playerEntity);

                if(!world.isClientSide()) {
                    CompoundTag tag = beeHelmet.getOrCreateTag();
                    tag.putBoolean("hasBeeRider", true);
                }
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.PASS;
    }

    public static double beeRidingOffset(double yOffset, Entity vehicle, Entity rider) {
        if(vehicle instanceof Player && rider instanceof Bee) {
            return yOffset + 0.3f;
        }
        return yOffset;
    }

    public static void decrementHighlightingCounter(Player player) {
        if(player != null && HELMET_EFFECT_COUNTER_CLIENTSIDE > 0) {
            HELMET_EFFECT_COUNTER_CLIENTSIDE--;

            if (getEntityBeeHelmet(player).isEmpty()) {
                HELMET_EFFECT_COUNTER_CLIENTSIDE = 0;
            }
        }
    }

    public static ItemStack getEntityBeeHelmet(Entity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof StinglessBeeHelmet) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }
}