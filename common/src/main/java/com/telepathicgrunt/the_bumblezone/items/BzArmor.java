package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BzArmor extends ArmorItem implements ItemExtension {
    public BzArmor(Holder<ArmorMaterial> armorMaterial, ArmorItem.Type armorType, Properties properties) {
        super(armorMaterial, armorType, properties);
    }

    public void bz$onArmorTick(ItemStack itemstack, Level world, Player player) { }
}
