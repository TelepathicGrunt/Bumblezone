package net.telepathicgrunt.bumblezone.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class HoneyCrystalShield extends ShieldItem {

    public HoneyCrystalShield() {
        //starts off with 20 durability so it is super weak
        super(new Item.Properties().maxDamage(20).group(BzItems.BUMBLEZONE_CREATIVE_TAB));
    }


    /**
     * Specify what item can repair this shield
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return BzItems.HONEY_CRYSTAL_SHARDS == repair.getItem();
    }

    /**
     * Display the shield level (repair cost)
     */
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            tooltip.add(new TranslationTextComponent("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (repairLevel + 1)));
        }
    }
}
