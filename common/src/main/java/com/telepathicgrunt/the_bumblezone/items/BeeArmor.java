package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BeeArmor extends BzArmor implements ItemExtension {
    private final int variant;
    private final boolean transTexture;

    public BeeArmor(ArmorMaterial material, ArmorItem.Type armorType, Properties properties, int variant, boolean transTexture) {
        super(material, armorType, properties);
        this.variant = variant;
        this.transTexture = transTexture;
    }

    public boolean hasTransTexture() {
        return transTexture;
    }

    public int getVariant() {
        return variant;
    }

    public void bz$onArmorTick(ItemStack itemstack, Level world, Player player) { }
}