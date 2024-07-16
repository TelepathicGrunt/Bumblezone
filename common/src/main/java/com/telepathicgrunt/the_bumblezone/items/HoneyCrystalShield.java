package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.items.datacomponents.HoneyCrystalShieldCurrentLevelData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.HoneyCrystalShieldDefinedLevelsData;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.mixin.items.PlayerDamageShieldInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.TriState;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HoneyCrystalShield extends BzShieldItem implements ItemExtension {

    public HoneyCrystalShield(Properties properties, int initialDurability) {
        super(properties
                .component(BzDataComponents.HONEY_CRYSTAL_SHIELD_CURRENT_LEVEL_DATA.get(), new HoneyCrystalShieldCurrentLevelData())
                .component(BzDataComponents.HONEY_CRYSTAL_SHIELD_DEFINED_LEVELS_DATA.get(), new HoneyCrystalShieldDefinedLevelsData(initialDurability))
                .durability(initialDurability));
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.HONEY_CRYSTAL_SHIELD_CURRENT_LEVEL_DATA.get()) == null) {
            itemStack.set(BzDataComponents.HONEY_CRYSTAL_SHIELD_CURRENT_LEVEL_DATA.get(), new HoneyCrystalShieldCurrentLevelData());
        }
        if (itemStack.get(BzDataComponents.HONEY_CRYSTAL_SHIELD_DEFINED_LEVELS_DATA.get()) == null) {
            itemStack.set(BzDataComponents.HONEY_CRYSTAL_SHIELD_DEFINED_LEVELS_DATA.get(), new HoneyCrystalShieldDefinedLevelsData(itemStack.getMaxDamage()));
        }
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

    // Called on Forge
    @Nullable
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return this.bz$getEquipmentSlot(stack);
    }

    /**
     * Display the shield level (repair cost)
     */
    // CLIENT-SIDED
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        if (itemStack.is(BzItems.HONEY_CRYSTAL_SHIELD.get())) {
            int shieldLevel = itemStack.get(BzDataComponents.HONEY_CRYSTAL_SHIELD_CURRENT_LEVEL_DATA.get()).currentLevel();
            int maxLevel = itemStack.get(BzDataComponents.HONEY_CRYSTAL_SHIELD_DEFINED_LEVELS_DATA.get()).maxLevel();
            shieldLevel = Math.max(Math.min(shieldLevel, maxLevel), 1);
            tooltip.add(Component.translatable("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (shieldLevel)));
        }
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    @Override
    public void bz$setDamage(ItemStack itemStack, int damage) {
        int newDamage = damage;
        int oldDamage = itemStack.getDamageValue();
        int damageCaused = oldDamage - damage;
        int shieldLevel = itemStack.get(BzDataComponents.HONEY_CRYSTAL_SHIELD_CURRENT_LEVEL_DATA.get()).currentLevel();
        HoneyCrystalShieldDefinedLevelsData honeyCrystalShieldDefinedLevelsData = itemStack.get(BzDataComponents.HONEY_CRYSTAL_SHIELD_DEFINED_LEVELS_DATA.get());

        // ignore anvil repairing
        if (damageCaused < 0) {
            int reducedDamage = -1 * Math.min(-1, damageCaused + (shieldLevel / 4));
            newDamage = Math.max(0, itemStack.getDamageValue() + reducedDamage);
        }
        // strengthen on significant repair
        else if (damageCaused > itemStack.getMaxDamage() / 5) {
            itemStack.set(BzDataComponents.HONEY_CRYSTAL_SHIELD_CURRENT_LEVEL_DATA.get(), new HoneyCrystalShieldCurrentLevelData(Math.min(honeyCrystalShieldDefinedLevelsData.maxLevel(), shieldLevel + 1)));
            itemStack.set(DataComponents.MAX_DAMAGE, itemStack.getMaxDamage() + honeyCrystalShieldDefinedLevelsData.getDurabilityForLevel(shieldLevel));
        }

        itemStack.set(DataComponents.DAMAGE, Mth.clamp(newDamage, 0, itemStack.getMaxDamage()));
    }

    // Runs on Neoforge
    public void setDamage(ItemStack stack, int damage) {
        this.bz$setDamage(stack, damage);
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
    public static boolean handledPlayerHurtBehavior(BzEntityAttackedEvent event) {
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
        if (player.getUseItem().getItem() instanceof HoneyCrystalShield &&
            player.isBlocking() &&
            (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_FIRE)))
        {
            if (player instanceof ServerPlayer serverPlayer) {
                BzCriterias.HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER.get().trigger(serverPlayer);
            }

            if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                // damage our shield greatly and do player screen shake
                player.indicateDamage(0, 0);
                ((PlayerDamageShieldInvoker) player).callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 3, 18));
            }
            else if (source.is(DamageTypeTags.IS_FIRE) && !player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                    ((PlayerDamageShieldInvoker) player).callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 6, 3));
                }
                else {
                    ((PlayerDamageShieldInvoker) player).callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 100, 3));
                    return false; //continue the damaging
                }
            }

            if (player instanceof ServerPlayer) {
                player.awardStat(Stats.ITEM_USED.get(player.getUseItem().getItem()));
            }

            return true;
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

    @Override
    public TriState bz$canEnchant(ItemStack itemstack, Holder<Enchantment> enchantment) {
        return enchantment.is(BzTags.ENCHANTABLES_HONEY_CRYSTAL_SHIELD_FORCED_DISALLOWED) ? TriState.DENY : TriState.PASS;
    }
}