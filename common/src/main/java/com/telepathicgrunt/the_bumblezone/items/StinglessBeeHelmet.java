package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.datacomponents.StinglessBeeHelmetData;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.StinglessBeeHelmetSightPacket;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class StinglessBeeHelmet extends BeeArmor {
    public static final Set<Entity> BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE = new ObjectArraySet<>();
    public static int HELMET_EFFECT_COUNTER_CLIENTSIDE = 0;
    public static int BEE_WEARABLES_COUNT = 0;
    public static int PACKET_SEND_COOLDOWN_CLIENTSIDE = 20;

    public StinglessBeeHelmet(Holder<ArmorMaterial> material, ArmorItem.Type armorType, Properties properties, int variant) {
        super(material, armorType, properties.component(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get(), new StinglessBeeHelmetData()), variant, false);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get()) == null) {
            itemStack.set(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get(), new StinglessBeeHelmetData());
        }
    }

    // Runs on Forge
    public void onArmorTick(ItemStack itemstack, Level world, Player player) {
        this.bz$onArmorTick(itemstack, world, player);
    }

    @Override
    public void bz$onArmorTick(ItemStack itemStack, Level level, Player player) {
        StinglessBeeHelmetData beeHelmetData = itemStack.get(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get());
        boolean hasBeeRider = beeHelmetData.hasBeeRider();
        long beeRiderStartTime = beeHelmetData.beeRiderStartTime();

        if (player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            if (hasBeeRider) {
                ejectAllBeeRiders(player);

                if (!level.isClientSide()) {
                    hasBeeRider = false;
                    beeRiderStartTime = -1;

                    if (beeHelmetData.isDifferent(hasBeeRider, beeRiderStartTime)) {
                        itemStack.set(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get(), new StinglessBeeHelmetData(hasBeeRider, beeRiderStartTime));
                    }
                }
            }
            return;
        }

        int beeWearablesCount = BeeArmor.getBeeThemedWearablesCount(player);

        MobEffectInstance nausea = player.getEffect(MobEffects.CONFUSION);
        if (nausea != null) {
            int decreaseSpeed = (int)((beeWearablesCount * 3d) - 2);
            for (int i = 0; i < decreaseSpeed; i++) {
                ((MobEffectInstanceAccessor) nausea).callTickDownDuration();
                if (!level.isClientSide() &&
                    player.getRandom().nextFloat() < 0.001f &&
                    itemStack.getMaxDamage() - itemStack.getDamageValue() > 1)
                {
                    itemStack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
                }
            }
        }

        MobEffectInstance poison = player.getEffect(MobEffects.POISON);
        if (poison != null && (beeWearablesCount >= 4 || level.getGameTime() % (beeWearablesCount == 3 ? 2 : 3) == 0)) {
            for (int i = 0; i <= Math.max(beeWearablesCount - 3, 1); i++) {
                if (poison.getDuration() > 0) {
                    ((MobEffectInstanceAccessor) poison).callTickDownDuration();
                }
            }
            if(!level.isClientSide() &&
                player.getRandom().nextFloat() < 0.004f &&
                itemStack.getMaxDamage() - itemStack.getDamageValue() > 1)
            {
                itemStack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
            }
        }

        if (level.isClientSide()) {
            PACKET_SEND_COOLDOWN_CLIENTSIDE--;
            if (PACKET_SEND_COOLDOWN_CLIENTSIDE == 0) {
                PACKET_SEND_COOLDOWN_CLIENTSIDE = 20;
                if (BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.size() >= 50) {
                    StinglessBeeHelmetSightPacket.sendToServer(true);
                }
            }
            BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.clear();
            StinglessBeeHelmet.BEE_WEARABLES_COUNT = beeWearablesCount;

            if (player.isShiftKeyDown() && player.onGround()) {
                HELMET_EFFECT_COUNTER_CLIENTSIDE = ((beeWearablesCount - 1) * 65) + 6;
            }
        }

        boolean hasWrath = player.hasEffect(BzEffects.WRATH_OF_THE_HIVE.holder());
        if (hasBeeRider || hasWrath) {
            if (hasWrath ||
                player.isUnderWater() ||
                player.isShiftKeyDown() ||
                (beeWearablesCount < 4 && ((beeWearablesCount * 600L) + beeRiderStartTime) < level.getGameTime()))
            {
                ejectAllBeeRiders(player);

                if (!level.isClientSide()) {
                    hasBeeRider = false;
                    beeRiderStartTime = -1;
                }
            }
            else if (!level.isClientSide()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(BzStats.STINGLESS_BEE_HELMET_BEE_RIDER_RL.get());
                }
            }
        }

        if (beeHelmetData.isDifferent(hasBeeRider, beeRiderStartTime)) {
            itemStack.set(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get(), new StinglessBeeHelmetData(hasBeeRider, beeRiderStartTime));
        }
    }

    private void ejectAllBeeRiders(Player player) {
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
    }

    public static boolean shouldEntityGlow(Player player, Entity entity) {
        if (entity instanceof Bee || entity instanceof BeehemothEntity || entity instanceof BeeQueenEntity) {
            if (player.getCooldowns().isOnCooldown(BzItems.STINGLESS_BEE_HELMET_1.get()) ||
                player.getCooldowns().isOnCooldown(BzItems.STINGLESS_BEE_HELMET_2.get()))
            {
                return false;
            }

            return entity.blockPosition().closerThan(player.blockPosition(), (BEE_WEARABLES_COUNT * 16.5d) + 14);
        }
        return false;
    }

    public static InteractionResult addBeePassenger(Level level, Player playerEntity, InteractionHand hand, Entity entity) {
        ItemStack itemStack = StinglessBeeHelmet.getEntityBeeHelmet(playerEntity);
        if (!itemStack.isEmpty() &&
            !playerEntity.isShiftKeyDown() &&
            playerEntity.getItemInHand(playerEntity.getUsedItemHand()).isEmpty() &&
            playerEntity.getPassengers().isEmpty() &&
            !playerEntity.getCooldowns().isOnCooldown(itemStack.getItem()))
        {
            if ((entity instanceof Bee && !entity.getType().is(BzTags.STINGLESS_BEE_HELMET_DISALLOWED_PASSENGERS)) ||
                entity.getType().is(BzTags.STINGLESS_BEE_HELMET_FORCED_ALLOWED_PASSENGERS))
            {
                entity.startRiding(playerEntity);
                if (playerEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetPassengersPacket(serverPlayer));
                }

                if (!level.isClientSide()) {
                    itemStack.set(BzDataComponents.STINGLESS_BEE_HELMET_DATA.get(), new StinglessBeeHelmetData(true, level.getGameTime()));
                }
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.PASS;
    }

    public static Vec3 beeRidingOffset(Vec3 offset, Entity vehicle, Entity rider) {
        if (vehicle instanceof Player && rider instanceof Bee) {
            return offset.add(0, 0, 0);
        }
        return offset;
    }

    public static void decrementHighlightingCounter(Player player) {
        if (player != null && HELMET_EFFECT_COUNTER_CLIENTSIDE > 0) {
            HELMET_EFFECT_COUNTER_CLIENTSIDE--;

            if (getEntityBeeHelmet(player).isEmpty()) {
                HELMET_EFFECT_COUNTER_CLIENTSIDE = 0;
            }
        }
    }

    public static ItemStack getEntityBeeHelmet(LivingEntity entity) {
        for (ItemStack armor : entity.getArmorSlots()) {
            if (armor.getItem() instanceof StinglessBeeHelmet) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }
}