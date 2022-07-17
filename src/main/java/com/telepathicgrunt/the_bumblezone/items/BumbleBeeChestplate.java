package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.client.LivingEntityFlyingSoundInstance;
import com.telepathicgrunt.the_bumblezone.mixin.entities.LivingEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.BumbleBeeChestplateFlyingPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BumbleBeeChestplate extends BeeArmor {

    public BumbleBeeChestplate(ArmorMaterial material, EquipmentSlot slot, Properties properties, boolean transTexture, int variant) {
        super(material, slot, properties, variant, transTexture);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.BEE_ARMOR_REPAIR_ITEMS);
    }

    @Override
    public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
        CompoundTag tag = itemstack.getOrCreateTag();
        boolean isFlying = tag.getBoolean("isFlying");
        int flyCounter = tag.getInt("flyCounter");
        if(world.isClientSide()) {
            if (flyCounter > 0 && !entity.isOnGround() && !entity.isInWater() && ((LivingEntityAccessor)entity).isJumping() && !entity.getAbilities().flying && !entity.isPassenger() && !entity.onClimbable()) {
                if(!isFlying) {
                    LivingEntityFlyingSoundInstance.playSound(entity, BzSounds.BUMBLE_BEE_CHESTPLATE_FLYING.get());
                    BumbleBeeChestplateFlyingPacket.sendToServer(true);
                    tag.putBoolean("isFlying", true);
                }
            }
            else if(isFlying) {
                LivingEntityFlyingSoundInstance.stopSound(entity, BzSounds.BUMBLE_BEE_CHESTPLATE_FLYING.get());
                BumbleBeeChestplateFlyingPacket.sendToServer(false);
                tag.putBoolean("isFlying", false);
            }
        }

        boolean isAllBeeArmorOn = StinglessBeeHelmet.isAllBeeArmorOn(entity);
        MobEffectInstance beenergized = entity.getEffect(BzEffects.BEENERGIZED.get());
        boolean isBeenergized = beenergized != null;

        isFlying = tag.getBoolean("isFlying");
        if(isFlying) {
            if(flyCounter > 0) {
                Vec3 velocity = entity.getDeltaMovement();
                double additiveSpeed = velocity.y() > 0 ? velocity.y() > 0.1D ? 0.06D : 0.080D : 0.13D;
                if(isBeenergized) {
                    additiveSpeed += (beenergized.getAmplifier() + 1) * 0.0125D;
                }

                double newYSpeed = velocity.y() + additiveSpeed;
                entity.setDeltaMovement(
                        velocity.x(),
                        newYSpeed,
                        velocity.z()
                );

                if(newYSpeed > -0.3) {
                    entity.fallDistance = 0;
                }
                else if (newYSpeed <= -0.3) {
                    entity.fallDistance = ((float) Math.abs(newYSpeed) / 0.3f) + 1.75f;
                }

                tag.putInt("flyCounter", flyCounter - 1);
                if(!world.isClientSide() && world.random.nextFloat() < 0.0025f) {
                    itemstack.hurtAndBreak(1, entity, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.CHEST));
                }

                if(entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(BzStats.BUMBLE_BEE_CHESTPLATE_FLY_TIME_RL.get());
                }
            }
            else {
                tag.putBoolean("isFlying", false);
                if(isBeenergized && isAllBeeArmorOn && entity instanceof ServerPlayer) {
                    BzCriterias.BUMBLE_BEE_CHESTPLATE_MAX_FLIGHT_TRIGGER.trigger((ServerPlayer) entity);
                }
            }
        }

        if(entity.isOnGround()) {
            tag.putInt("flyCounter", (int) (20 * (isBeenergized ? 1.5f : 1) * (isAllBeeArmorOn ? 2 : 1)));
        }

        super.onArmorTick(itemstack, world, entity);
    }

    public static ItemStack getEntityBeeChestplate(Entity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof BumbleBeeChestplate) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }
}