package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.mixin.items.PlayerDamageShieldInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class HoneyCrystalShield extends BzShieldItem implements ItemExtension {
    private static final int[] shieldDurabilityBoostPerLevel = new int[]{0,20,45,75,110,150,195,245,316,632};
    private static final int maxShieldLevel = shieldDurabilityBoostPerLevel.length - 1;

    public HoneyCrystalShield(Properties properties) {
        //starts off with 40 durability so it is super weak
        super(properties.durability(40));
    }

    /**
     * Specify what item can repair this shield
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS);
    }

    @Override
    public EquipmentSlot bz$getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.OFFHAND;
    }

    /**
     * Display the shield level (repair cost)
     */
    // CLIENT-SIDED
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), maxShieldLevel), 0);
            tooltip.add(Component.translatable("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (shieldLevel + 1)));
        }
    }

    /**
     * Increases the durability of the shield by 10 for every shield level (repair cost)
     */
    @Override
    public int bz$getMaxDamage(ItemStack stack) {
        if(stack.hasTag()) {
            upgradeLegacyShield(stack);

            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), maxShieldLevel), 0);
            if (shieldLevel != 0) {
                return stack.getItem().getMaxDamage() + shieldDurabilityBoostPerLevel[shieldLevel];
            }
        }
        return stack.getItem().getMaxDamage();
    }

    private void upgradeLegacyShield(ItemStack stack) {
        if(stack.hasTag() && !stack.getTag().contains("ShieldLevel")) {
            int repairCost = stack.getOrCreateTag().getInt("RepairCost");
            if (repairCost >= 32) {
                stack.getOrCreateTag().putInt("ShieldLevel", maxShieldLevel);
            }
            else if(repairCost >= 16) {
                stack.getOrCreateTag().putInt("ShieldLevel", maxShieldLevel - 1);
            }
            else if(repairCost >= 5) {
                stack.getOrCreateTag().putInt("ShieldLevel", maxShieldLevel / 2);
            }
        }
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    @Override
    public void bz$setDamage(ItemStack stack, int damage) {
        int newDamage = damage;
        int oldDamage = stack.getDamageValue();
        int damageCaused = oldDamage - damage;
        int shieldLevel = stack.getOrCreateTag().getInt("ShieldLevel");

        // ignore anvil repairing
        if (damageCaused < 0) {
            int reducedDamage = -1 * Math.min(-1, damageCaused + (shieldLevel / 4));
            newDamage = Math.max(0, stack.getDamageValue() + reducedDamage);
        }
        // strengthen on significant repair
        else if (damageCaused > stack.getMaxDamage() / 5) {
            stack.getOrCreateTag().putInt("ShieldLevel", Math.min(maxShieldLevel, shieldLevel + 1));
        }

        stack.getOrCreateTag().putInt("Damage", Math.max(0, newDamage));
    }

    /**
     * blacklisted mending enchantment
     */
    @Override
    public OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment == Enchantments.MENDING) {
            return OptionalBoolean.FALSE;
        }

        return OptionalBoolean.of(enchantment.category.canEnchant(stack.getItem()));
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.round(13.0F - (float)itemStack.getDamageValue() * 13.0F / (float)itemStack.getMaxDamage());
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        float f = Math.max(0.0F, ((float)itemStack.getMaxDamage() - (float)itemStack.getDamageValue()) / (float)itemStack.getMaxDamage());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean bz$canPerformAction(ItemStack stack, String toolAction) {
        return toolAction.equals("shield_block") && stack.is(this);
    }

    //extra effects for honey shield such as slow attackers or melt shield when hit by fire
    public static boolean handledPlayerHurtBehavior(EntityAttackedEvent event) {
        if (event.entity() instanceof Player player) {
            slowPhysicalAttackers(event.source(), player);
            return damageShieldFromExplosionAndFire(event.source(), player);
        }
        return false;
    }

    /**
     * Deals massive damage to shield when blocking explosion or getting fire damage with Honey Crystal Shield
     */
    public static boolean damageShieldFromExplosionAndFire(DamageSource source, Player player) {
        DamageSources damageSources = player.level().damageSources();

        // checks for explosion and player
        if (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_FIRE)) {
            if (player.getUseItem().getItem() instanceof HoneyCrystalShield) {
                if(player instanceof ServerPlayer) {
                    BzCriterias.HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER.trigger((ServerPlayer) player);
                }

                if (source.is(DamageTypeTags.IS_EXPLOSION) && player.isBlocking()) {
                    // damage our shield greatly and 1 damage hit player to show shield weakness
                    player.hurt(damageSources.generic(), 1);
                    ((PlayerDamageShieldInvoker) player).callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 3, 18));
                }
                else if (source.is(DamageTypeTags.IS_FIRE)) {
                    if(source.is(DamageTypeTags.IS_PROJECTILE)){
                        ((PlayerDamageShieldInvoker) player).callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 6, 3));
                    }
                    else{
                        ((PlayerDamageShieldInvoker) player).callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 100, 3));
                        return false; //continue the damaging
                    }
                }

                return true;
            }
        }

        return false;
    }


    /**
     * Applies slowness to physical attackers when blocking with Honey Crystal Shield
     */
    public static void slowPhysicalAttackers(DamageSource source, Player player) {

        // checks for living attacker and player victim
        // and also ignores explosions or magic damage
        if (source.getDirectEntity() instanceof LivingEntity attacker && (!source.is(DamageTypeTags.IS_EXPLOSION) || !source.is(DamageTypeTags.BYPASSES_SHIELD))) {

            // checks to see if player is blocking with our shield
            if (player.getUseItem().getItem() instanceof HoneyCrystalShield && player.isBlocking()) {

                // apply slowness to attacker
                attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 165, 1, true, true, false));
            }
        }
    }

    public static void setShieldCooldown(Player playerEntity, LivingEntity livingEntity){
        float disableChance = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(livingEntity) * 0.05F;
        if (livingEntity.getRandom().nextFloat() < disableChance) {
            playerEntity.getCooldowns().addCooldown(BzItems.HONEY_CRYSTAL_SHIELD.get(), 100);
            livingEntity.level().broadcastEntityEvent(playerEntity, (byte)30);
        }
    }

    public static boolean damageHoneyCrystalShield(Player player, float amount){
        if(player.getUseItem().getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get()){
            if (amount >= 3.0F) {
                int damageToDo = 1 + Mth.floor(amount);
                InteractionHand hand = player.getUsedItemHand();
                player.getUseItem().hurtAndBreak(damageToDo, player, (playerEntity) -> playerEntity.broadcastBreakEvent(hand));
                if (player.getUseItem().isEmpty()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                    else {
                        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    player.stopUsingItem();
                    player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);
                }
            }

            return true;
        }

        return false;
    }
}