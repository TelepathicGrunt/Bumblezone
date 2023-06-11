package com.telepathicgrunt.the_bumblezone.mixins.forge.item;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemExtension.class)
public interface ItemExtensionMixin extends IForgeItem {

    @Shadow
    int bz$getMaxDamage(ItemStack stack);

    @Shadow
    void bz$setDamage(ItemStack stack, int damage);

    @Shadow
    OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment);

    @Shadow
    EquipmentSlot bz$getEquipmentSlot(ItemStack stack);

    @Shadow
    boolean bz$canPerformAction(ItemStack stack, String toolAction);


    @Shadow
    void bz$onArmorTick(ItemStack itemstack, Level world, Player player);

    @Override
    default int getMaxDamage(ItemStack stack) {
        return this.bz$getMaxDamage(stack);
    }

    @Override
    default void setDamage(ItemStack stack, int damage) {
        this.bz$setDamage(stack, damage);
    }

    @Override
    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.bz$canApplyAtEnchantingTable(stack, enchantment)
                .orElseGet(() -> IForgeItem.super.canApplyAtEnchantingTable(stack, enchantment));
    }

    @Override
    @Nullable
    default EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return this.bz$getEquipmentSlot(stack);
    }

    @Override
    default void onArmorTick(ItemStack itemstack, Level world, Player player) {
        this.bz$onArmorTick(itemstack, world, player);
    }

    @Override
    default boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return this.bz$canPerformAction(stack, toolAction.name());
    }
}
