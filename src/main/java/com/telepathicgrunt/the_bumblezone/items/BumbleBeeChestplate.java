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
    public void onArmorTick(ItemStack itemstack, Level world, Player player) {
        CompoundTag tag = itemstack.getOrCreateTag();
        boolean isFlying = tag.getBoolean("isFlying");
        int flyCounter = tag.getInt("flyCounter");
        if(world.isClientSide()) {
            if (flyCounter > 0 && !player.isOnGround() && !player.isInWater() && ((LivingEntityAccessor)player).isJumping() && !player.getAbilities().flying && !player.isPassenger() && !player.onClimbable()) {
                if(!isFlying) {
                    LivingEntityFlyingSoundInstance.playSound(player, BzSounds.BUMBLE_BEE_CHESTPLATE_FLYING.get());
                    BumbleBeeChestplateFlyingPacket.sendToServer(true);
                    tag.putBoolean("isFlying", true);
                }
            }
            else if(isFlying) {
                LivingEntityFlyingSoundInstance.stopSound(player, BzSounds.BUMBLE_BEE_CHESTPLATE_FLYING.get());
                BumbleBeeChestplateFlyingPacket.sendToServer(false);
                tag.putBoolean("isFlying", false);
            }
        }

        int beeGearCount = BeeArmor.getBeeThemedGearCount(player);
        MobEffectInstance beenergized = player.getEffect(BzEffects.BEENERGIZED.get());
        boolean isBeenergized = beenergized != null;

        isFlying = tag.getBoolean("isFlying");
        if(isFlying) {
            if(flyCounter > 0) {
                Vec3 velocity = player.getDeltaMovement();
                double additiveSpeed = velocity.y() > 0 ? velocity.y() > 0.1D ? 0.06D : 0.080D : 0.13D;
                if(isBeenergized) {
                    additiveSpeed += (beenergized.getAmplifier() + 1) * 0.0125D;
                }

                double newYSpeed = velocity.y() + additiveSpeed;
                player.setDeltaMovement(
                        velocity.x(),
                        newYSpeed,
                        velocity.z()
                );

                if(newYSpeed > -0.3) {
                    player.fallDistance = 0;
                }
                else if (newYSpeed <= -0.3) {
                    player.fallDistance = ((float) Math.abs(newYSpeed) / 0.3f) + 1.75f;
                }

                tag.putInt("flyCounter", flyCounter - 1);
                if(!world.isClientSide() && player.getRandom().nextFloat() < 0.0025f) {
                    itemstack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(EquipmentSlot.CHEST));
                }

                if(player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(BzStats.BUMBLE_BEE_CHESTPLATE_FLY_TIME_RL.get());
                }
            }
            else {
                tag.putBoolean("isFlying", false);
                if(beeGearCount >= 4 && player instanceof ServerPlayer) {
                    BzCriterias.BUMBLE_BEE_CHESTPLATE_MAX_FLIGHT_TRIGGER.trigger((ServerPlayer) player);
                }
            }
        }

        if(player.isOnGround()) {
            if (tag.contains("forcedMaxFlyingTickTime")) {
                if (!tag.contains("requiredGearCountForForcedFlyingTime") || tag.getInt("requiredGearCountForForcedFlyingTime") >= beeGearCount) {
                    tag.putInt("flyCounter", tag.getInt("forcedMaxFlyingTickTime"));
                }
                else {
                    tag.putInt("flyCounter", (int) (20 * (isBeenergized ? 1.5F : 1) * (((beeGearCount - 1) * 0.5F) + 1)));
                }
            }
            else {
                tag.putInt("flyCounter", (int) (20 * (isBeenergized ? 1.5F : 1) * (((beeGearCount - 1) * 0.5F) + 1)));
            }
        }

        super.onArmorTick(itemstack, world, player);
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