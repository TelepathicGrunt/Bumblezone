package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.datacomponents.AbilityEssenceAbilityData;
import com.telepathicgrunt.the_bumblezone.datacomponents.AbilityEssenceActivityData;
import com.telepathicgrunt.the_bumblezone.datacomponents.AbilityEssenceCooldownData;
import com.telepathicgrunt.the_bumblezone.datacomponents.AbilityEssenceLastChargeData;
import com.telepathicgrunt.the_bumblezone.mixin.gameplay.CooldownInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.gameplay.ItemCooldownsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public abstract class AbilityEssenceItem extends Item implements ItemExtension {

    private final Supplier<Integer> cooldownTickLength;
    private final Supplier<Integer> abilityUseAmount;

    public AbilityEssenceItem(Properties properties, Supplier<Integer> cooldownTickLength, Supplier<Integer> abilityUseAmount) {
        super(properties
                .component(BzDataComponents.ABILITY_ESSENCE_ABILITY_DATA.get(), new AbilityEssenceAbilityData())
                .component(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData())
                .component(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get(), new AbilityEssenceActivityData()));
        this.cooldownTickLength = cooldownTickLength;
        this.abilityUseAmount = abilityUseAmount;
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack itemStack) {
        if (itemStack.get(BzDataComponents.ABILITY_ESSENCE_ABILITY_DATA.get()) == null) {
            itemStack.set(BzDataComponents.ABILITY_ESSENCE_ABILITY_DATA.get(), new AbilityEssenceAbilityData());
        }
        if (itemStack.get(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get()) == null) {
            itemStack.set(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData());
        }
        if (itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get()) == null) {
            itemStack.set(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get(), new AbilityEssenceActivityData());
        }
    }

    public abstract int getColor();

    public int getCooldownTickLength() {
        return cooldownTickLength.get();
    }

    public void incrementCooldownTime(ItemStack itemStack) {
        AbilityEssenceCooldownData abilityEssenceCooldownData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get());
        if (abilityEssenceCooldownData.forcedCooldown()) {
            int currentCooldownTime = abilityEssenceCooldownData.cooldownTime();
            if (currentCooldownTime < cooldownTickLength.get()) {
                itemStack.set(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData(currentCooldownTime + 1, true));
            }
            else {
                itemStack.set(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData(0, false));
                rechargeAbilityEntirely(itemStack);
            }
        }
    }

    public void setDepleted(ItemStack itemStack, ServerPlayer serverPlayer, boolean vanillaItemCooldown) {
        itemStack.set(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData(0, true));
        AbilityEssenceActivityData abilityEssenceAbilityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
        itemStack.set(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get(), new AbilityEssenceActivityData(abilityEssenceAbilityData.isInInventory(), false, abilityEssenceAbilityData.isLocked()));
        if (vanillaItemCooldown) {
            serverPlayer.getCooldowns().addCooldown(this, getCooldownTickLength());
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (itemStack.is(BzTags.ABILITY_ESSENCE_ITEMS) && entity instanceof ServerPlayer serverPlayer) {
            AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
            boolean isInInventory = abilityEssenceActivityData.isInInventory();
            boolean isLocked = abilityEssenceActivityData.isLocked();
            boolean isActive = abilityEssenceActivityData.isActive();

            AbilityEssenceCooldownData abilityEssenceCooldownData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get());
            int cooldownTime = abilityEssenceCooldownData.cooldownTime();
            boolean forcedCooldown = abilityEssenceCooldownData.forcedCooldown();

            if (!isInInventory) {
                isInInventory = true;
            }

            if (!EssenceOfTheBees.hasEssence(serverPlayer)) {
                itemStack.set(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get(), new AbilityEssenceActivityData(isInInventory, false, true));
                return;
            }
            else if (isLocked) {
                isLocked = false;
            }

            if (forcedCooldown) {
                if (!serverPlayer.getCooldowns().isOnCooldown(itemStack.getItem())) {
                    serverPlayer.getCooldowns().addCooldown(itemStack.getItem(), getCooldownTickLength() - cooldownTime);
                }
                incrementCooldownTime(itemStack);
            }
            else {
                if (serverPlayer.getCooldowns().isOnCooldown(itemStack.getItem())) {
                    ItemCooldowns.CooldownInstance cooldownInstance = ((ItemCooldownsAccessor)serverPlayer.getCooldowns()).getCooldowns().get(itemStack.getItem());
                    int tempCooldownTime = ((ItemCooldownsAccessor)serverPlayer.getCooldowns()).getTickCount() - ((CooldownInstanceAccessor)cooldownInstance).getStartTime();

                    if (tempCooldownTime > 5) {
                        forcedCooldown = true;
                        cooldownTime = tempCooldownTime;
                        isActive = false;
                    }
                }
                else {
                    if (serverPlayer.getOffhandItem() == itemStack) {
                        if (!isActive) {
                            isActive = true;
                        }
                        applyAbilityEffects(itemStack, level, serverPlayer);
                    }
                    else {
                        rechargeAbilitySlowly(itemStack, serverPlayer);
                        if (isActive) {
                            isActive = false;
                        }
                    }
                }
            }

            if (abilityEssenceActivityData.isDifferent(isInInventory, isActive, isLocked)) {
                itemStack.set(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get(), new AbilityEssenceActivityData(isInInventory, isActive, isLocked));
            }

            if (abilityEssenceCooldownData.isDifferent(cooldownTime, forcedCooldown)) {
                itemStack.set(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData(cooldownTime, forcedCooldown));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> components, TooltipFlag tooltipFlag) {
        AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
        if (abilityEssenceActivityData.isLocked()) {
            components.add(Component.translatable("item.the_bumblezone.essence_locked").withStyle(ChatFormatting.DARK_RED));
            components.add(Component.translatable("item.the_bumblezone.essence_locked_description_1").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("item.the_bumblezone.essence_locked_description_2").withStyle(ChatFormatting.GRAY));
        }
        else if (abilityEssenceActivityData.isActive()) {
            components.add(Component.translatable("item.the_bumblezone.essence_active").withStyle(ChatFormatting.RED));
            components.add(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(itemStack), getMaxAbilityUseAmount()).withStyle(ChatFormatting.YELLOW));
            components.add(Component.empty());
            addDescriptionComponents(components);
            return;
        }

        AbilityEssenceCooldownData abilityEssenceCooldownData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get());
        int cooldownTime = abilityEssenceCooldownData.cooldownTime();
        boolean forcedCooldown = abilityEssenceCooldownData.forcedCooldown();
        if (forcedCooldown) {
            components.add(Component.translatable("item.the_bumblezone.essence_depleted").withStyle(ChatFormatting.DARK_RED));
            components.add(Component.translatable("item.the_bumblezone.essence_cooldown", GeneralUtils.formatTickDurationNoMilliseconds(getCooldownTickLength() - cooldownTime, tooltipContext.tickRate())).withStyle(ChatFormatting.DARK_RED));
            components.add(Component.empty());
            addDescriptionComponents(components);
        }
        else {
            components.add(Component.translatable("item.the_bumblezone.essence_ready").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(itemStack), getMaxAbilityUseAmount()).withStyle(ChatFormatting.YELLOW));
            components.add(Component.empty());
            addDescriptionComponents(components);
        }
    }

    abstract void addDescriptionComponents(List<Component> components);

    abstract void applyAbilityEffects(ItemStack itemStack, Level level, ServerPlayer serverPlayer);

    public int getAbilityUseRemaining(ItemStack itemStack) {
        AbilityEssenceAbilityData abilityEssenceAbilityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ABILITY_DATA.get());
        if (abilityEssenceAbilityData.abilityUseRemaining() < 0) {
            return getMaxAbilityUseAmount();
        }
        return abilityEssenceAbilityData.abilityUseRemaining();
    }

    public int getMaxAbilityUseAmount() {
        return abilityUseAmount.get();
    }

    public void setAbilityUseRemaining(ItemStack itemStack, int abilityUseRemaining) {
        itemStack.set(BzDataComponents.ABILITY_ESSENCE_ABILITY_DATA.get(), new AbilityEssenceAbilityData(abilityUseRemaining));
    }

    /**
     * @return Whether the ability usage has been depleted
     */
    public boolean decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer, int decreaseAmount) {
        int getRemainingUse = Math.max(getAbilityUseRemaining(stack) - decreaseAmount, 0);
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, false);
            return true;
        }
        return false;
    }

    public void rechargeAbilityEntirely(ItemStack stack) {
        setAbilityUseRemaining(stack, getMaxAbilityUseAmount());
    }

    public void rechargeAbilitySlowly(ItemStack itemStack, ServerPlayer serverPlayer) {
        int abilityUseRemaining = getAbilityUseRemaining(itemStack);
        if (abilityUseRemaining < getMaxAbilityUseAmount()) {
            AbilityEssenceLastChargeData abilityEssenceLastChargeData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_LAST_CHARGE_DATA.get());
            long lastChargeTime = abilityEssenceLastChargeData.lastChargeTime();
            if (lastChargeTime == 0 || serverPlayer.tickCount < lastChargeTime) {
                itemStack.set(BzDataComponents.ABILITY_ESSENCE_LAST_CHARGE_DATA.get(), new AbilityEssenceLastChargeData(serverPlayer.tickCount));
            }
            else {
                long timeFromLastCharge = serverPlayer.tickCount - lastChargeTime;
                int chargeTimeIncrement = Math.max(getCooldownTickLength() / getMaxAbilityUseAmount(), 1);
                if (timeFromLastCharge % chargeTimeIncrement == 0) {
                    setAbilityUseRemaining(itemStack, abilityUseRemaining + 1);
                }
            }

            itemStack.set(BzDataComponents.ABILITY_ESSENCE_LAST_CHARGE_DATA.get(), new AbilityEssenceLastChargeData(serverPlayer.tickCount));
        }
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

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        int remainingUse = this.getAbilityUseRemaining(itemStack);
        return remainingUse != 0 && this.getMaxAbilityUseAmount() != remainingUse;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        float remainingUse = (float)this.getAbilityUseRemaining(itemStack);
        float maxAmount = (float)this.getMaxAbilityUseAmount();
        return Math.round((remainingUse / maxAmount) * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        float remainingUse = (float)this.getAbilityUseRemaining(itemStack);
        float maxAmount = (float)this.getMaxAbilityUseAmount();
        float redValue = Math.max(0.0F, remainingUse / maxAmount);
        return Mth.hsvToRgb(redValue / 3.0F, 1.0F, 1.0F);
    }
}