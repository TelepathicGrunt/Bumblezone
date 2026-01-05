package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.items.datacomponents.AbilityEssenceAbilityData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.AbilityEssenceActivityData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.AbilityEssenceCooldownData;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.AbilityEssenceLastChargeData;
import com.telepathicgrunt.the_bumblezone.mixin.gameplay.ItemCooldownsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbilityEssenceItem extends Item implements ItemExtension {

    private final Supplier<Integer> cooldownTickLength;
    private final Supplier<Integer> abilityUseAmount;

    public AbilityEssenceItem(Properties properties, Supplier<Integer> cooldownTickLength, Supplier<Integer> abilityUseAmount) {
        super(properties
                .stacksTo(1)
                .fireResistant()
                .component(BzDataComponents.ABILITY_ESSENCE_ABILITY_DATA.get(), new AbilityEssenceAbilityData())
                .component(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get(), new AbilityEssenceCooldownData())
                .component(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get(), new AbilityEssenceActivityData())
                .component(BzDataComponents.ABILITY_ESSENCE_LAST_CHARGE_DATA.get(), new AbilityEssenceLastChargeData())
                .rarity(Rarity.EPIC));
        this.cooldownTickLength = cooldownTickLength;
        this.abilityUseAmount = abilityUseAmount;
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
            serverPlayer.getCooldowns().addCooldown(itemStack, getCooldownTickLength());
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, EquipmentSlot p_401900_) {
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
                if (!serverPlayer.getCooldowns().isOnCooldown(itemStack)) {
                    serverPlayer.getCooldowns().addCooldown(itemStack, getCooldownTickLength() - cooldownTime);
                }
                incrementCooldownTime(itemStack);
            }
            else {
                if (serverPlayer.getCooldowns().isOnCooldown(itemStack)) {
                    ItemCooldowns.CooldownInstance cooldownInstance = ((ItemCooldownsAccessor)serverPlayer.getCooldowns()).bumblezone$getCooldowns().get(itemStack.getItem());
                    int tempCooldownTime = ((ItemCooldownsAccessor)serverPlayer.getCooldowns()).bumblezone$getTickCount() - cooldownInstance.startTime();

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
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) {
        AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
        if (abilityEssenceActivityData.isLocked()) {
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_locked").withStyle(ChatFormatting.DARK_RED));
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_locked_description_1").withStyle(ChatFormatting.GRAY));
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_locked_description_2").withStyle(ChatFormatting.GRAY));
        }
        else if (abilityEssenceActivityData.isActive()) {
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_active").withStyle(ChatFormatting.RED));
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(itemStack), getMaxAbilityUseAmount()).withStyle(ChatFormatting.YELLOW));
            componentConsumer.accept(Component.empty());
            addDescriptionComponents(componentConsumer);
            return;
        }

        AbilityEssenceCooldownData abilityEssenceCooldownData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_COOLDOWN_DATA.get());
        int cooldownTime = abilityEssenceCooldownData.cooldownTime();
        boolean forcedCooldown = abilityEssenceCooldownData.forcedCooldown();
        if (forcedCooldown) {
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_depleted").withStyle(ChatFormatting.DARK_RED));
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_cooldown", GeneralUtils.formatTickDurationNoMilliseconds(getCooldownTickLength() - cooldownTime, tooltipContext.tickRate())).withStyle(ChatFormatting.DARK_RED));
            componentConsumer.accept(Component.empty());
            addDescriptionComponents(componentConsumer);
        }
        else {
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_ready").withStyle(ChatFormatting.GREEN));
            componentConsumer.accept(Component.translatable("item.the_bumblezone.essence_usage", getAbilityUseRemaining(itemStack), getMaxAbilityUseAmount()).withStyle(ChatFormatting.YELLOW));
            componentConsumer.accept(Component.empty());
            addDescriptionComponents(componentConsumer);
        }
    }

    abstract void addDescriptionComponents(Consumer<Component> components);

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
        if (serverPlayer.isCreative() || serverPlayer.isSpectator()) {
            return false;
        }
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