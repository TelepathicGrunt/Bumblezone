package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.StinglessBeeHelmetSightPacket;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Set;


public class StinglessBeeHelmet extends BeeArmor {
    public static int HELMET_EFFECT_COUNTER_CLIENTSIDE = 0;
    public static boolean ALL_BEE_ARMOR_ON_CLIENTSIDE = false;
    public static Set<Entity> BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE = new ObjectArraySet<>();
    public static int PACKET_SEND_COOLDOWN_CLIENTSIDE = 20;

    public StinglessBeeHelmet(ArmorMaterial material, EquipmentSlot slot, Properties properties, int variant) {
        super(material, slot, properties, variant, false);
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
        boolean isAllBeeArmorOn = StinglessBeeHelmet.isAllBeeArmorOn(entity);

        MobEffectInstance nausea = entity.getEffect(MobEffects.CONFUSION);
        if (nausea != null) {
            int decreaseSpeed = isAllBeeArmorOn ? 10 : 1;
            for (int i = 0; i < decreaseSpeed; i++) {
                ((MobEffectInstanceAccessor) nausea).callTickDownDuration();
                if(!world.isClientSide() &&
                    world.random.nextFloat() < 0.001f &&
                    itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
                {
                    itemstack.hurtAndBreak(1, entity, (playerEntity) -> {});
                }
            }
        }

        if(isAllBeeArmorOn) {
            MobEffectInstance poison = entity.getEffect(MobEffects.POISON);
            if (poison != null) {
                ((MobEffectInstanceAccessor) poison).callTickDownDuration();
                if(!world.isClientSide() &&
                    world.random.nextFloat() < 0.004f &&
                    itemstack.getMaxDamage() - itemstack.getDamageValue() > 1)
                {
                    itemstack.hurtAndBreak(1, entity, (playerEntity) -> {});
                }
            }
        }

        if (world.isClientSide()) {
            PACKET_SEND_COOLDOWN_CLIENTSIDE--;
            if(PACKET_SEND_COOLDOWN_CLIENTSIDE == 0) {
                PACKET_SEND_COOLDOWN_CLIENTSIDE = 20;
                if(BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.size() >= 100) {
                    FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
                    passedData.writeByte(1);
                    ClientPlayNetworking.send(StinglessBeeHelmetSightPacket.PACKET_ID, passedData);
                }
            }
            BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.clear();
            ALL_BEE_ARMOR_ON_CLIENTSIDE = isAllBeeArmorOn;
            decrementHighlightingCounter();

            if (entity.isCrouching()) {
                HELMET_EFFECT_COUNTER_CLIENTSIDE = isAllBeeArmorOn ? 200 : 6;

                if(!world.isClientSide() && world.random.nextFloat() < 0.001f) {
                    itemstack.hurtAndBreak(1, entity, (playerEntity) -> {});
                }
            }
        }
    }

    public static boolean shouldEntityGlow(Player player, Entity entity) {
        if(entity instanceof Bee || entity instanceof BeehemothEntity) {
            return entity.blockPosition().closerThan(player.blockPosition(), ALL_BEE_ARMOR_ON_CLIENTSIDE ? 80 : 30);
        }
        return false;
    }


    public static void decrementHighlightingCounter() {
        if(HELMET_EFFECT_COUNTER_CLIENTSIDE > 0) {
            HELMET_EFFECT_COUNTER_CLIENTSIDE--;
        }
    }

    public static boolean isAllBeeArmorOn(Entity entity) {
        return !StinglessBeeHelmet.getEntityBeeHelmet(entity).isEmpty() &&
                !BumbleBeeChestplate.getEntityBeeChestplate(entity).isEmpty() &&
                !HoneyBeeLeggings.getEntityBeeLegging(entity).isEmpty() &&
                !CarpenterBeeBoots.getEntityBeeBoots(entity).isEmpty();
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