package net.telepathicgrunt.bumblezone.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class HoneyCrystalShield extends ShieldItem {

    public HoneyCrystalShield() {
        //starts off with 20 durability so it is super weak
        super(new Item.Settings().maxDamage(20).group(BzItems.BUMBLEZONE_CREATIVE_TAB));
    }


    /**
     * Specify what item can repair this shield
     */
    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair) {
        return BzItems.HONEY_CRYSTAL_SHARDS == repair.getItem();
    }

    /**
     * Display the shield level (repair cost)
     */
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            tooltip.add(new TranslatableText("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (repairLevel + 1)));
        }
    }
}
