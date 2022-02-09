package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.entities.LivingEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.packets.BumbleBeeChestplateFlyingPacket;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class BumbleBeeChestplate extends TickingArmorItem {

    public BumbleBeeChestplate(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return BzItemTags.BEE_ARMOR_REPAIR_ITEMS.contains(repair.getItem());
    }

    @Override
    public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
        CompoundTag tag = itemstack.getOrCreateTag();
        boolean isFlying = tag.getBoolean("isFlying");
        if(world.isClientSide()) {
            if (!entity.isOnGround() && ((LivingEntityAccessor)entity).isJumping() && !entity.getAbilities().flying && !entity.isPassenger() && !entity.onClimbable()) {
                if(!isFlying) {
                    FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
                    passedData.writeByte(1);
                    ClientPlayNetworking.send(BumbleBeeChestplateFlyingPacket.PACKET_ID, passedData);
                    tag.putBoolean("isFlying", true);
                }
            }
            else if(isFlying) {
                FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
                passedData.writeByte(0);
                ClientPlayNetworking.send(BumbleBeeChestplateFlyingPacket.PACKET_ID, passedData);
                tag.putBoolean("isFlying", false);
            }
        }

        boolean isAllBeeArmorOn = StinglessBeeHelmet.isAllBeeArmorOn(entity);
        MobEffectInstance beenergized = entity.getEffect(BzEffects.BEENERGIZED);
        boolean isBeenergized = beenergized != null;
        if(entity.isOnGround()) {
            tag.putInt("flyCounter", (int) (20 * (isBeenergized ? 1.5f : 1) * (isAllBeeArmorOn ? 2 : 1)));
        }

        int flyCounter = tag.getInt("flyCounter");
        isFlying = tag.getBoolean("isFlying");
        if(isFlying) {
            if(flyCounter > 0) {
                Vec3 velocity = entity.getDeltaMovement();
                double additiveSpeed = velocity.y() > 0 ? velocity.y() > 0.1D ? 0.06D : 0.080D : 0.13D;
                if(isBeenergized) {
                    additiveSpeed += (beenergized.getAmplifier() + 1) * 0.012D;
                }

                double newYSpeed = velocity.y() + additiveSpeed;
                entity.setDeltaMovement(
                        velocity.x(),
                        newYSpeed,
                        velocity.z()
                );

                if(newYSpeed > -0.3) {
                    entity.resetFallDistance();
                }

                tag.putInt("flyCounter", flyCounter - 1);
                if(!world.isClientSide() && world.random.nextFloat() < 0.0025f) {
                    itemstack.hurtAndBreak(1, entity, (playerEntity) -> {});
                }
            }
            else {
                tag.putBoolean("isFlying", false);
                if(isBeenergized && isAllBeeArmorOn && entity instanceof ServerPlayer) {
                    BzCriterias.BUMBLE_BEE_CHESTPLATE_MAX_FLIGHT_TRIGGER.trigger((ServerPlayer) entity);
                }
            }
        }
    }

    public static ItemStack getEntityBeeChestplate(Entity entity) {
        for(ItemStack armor : entity.getArmorSlots()) {
            if(armor.getItem() instanceof BumbleBeeChestplate) {
                return armor;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public float getToughness() {
        return 0.5f;
    }

    @Override
    public int getDefense() {
        return 4;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}