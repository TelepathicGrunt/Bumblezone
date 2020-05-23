package net.telepathicgrunt.bumblezone.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HoneyCrystalShield extends ShieldItem{

    public HoneyCrystalShield() {
	//starts off with 20 durability so it is super weak
	super(new Item.Properties().maxDamage(20).group(BzItems.BUMBLEZONE_CREATIVE_TAB));
    }

    
    /**
     * Specify what item can repair this shield
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
       return BzItems.HONEY_CRYSTAL_SHARDS.get() == repair.getItem();
    }
    
    /**
     * It's a shield of course.
     */
    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity){
        return true;
    }


    /**
     * Display the shield level (repair cost)
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
	if(stack.hasTag()) {
	    int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
	    tooltip.add(new TranslationTextComponent("item.the_bumblezone.honey_crystal_shield.level_tooltip").appendText(": "+(repairLevel+1)));
	}
    }
    
    /**
     * Increases the durability of the shield by 10 for every shield level (repair cost)
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getMaxDamage(ItemStack stack)
    {
	if(stack.hasTag()) {
	    int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
	    if (repairLevel != 0) {
		return getItem().getMaxDamage() + repairLevel * 10;
	    }
	}
        return getItem().getMaxDamage();
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    @Override
    public void setDamage(ItemStack stack, int damage) {
	if (stack.hasTag()) {
	    int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
	    int damageCaused = this.getDamage(stack) - damage;

	    // ignore anvil repairing
	    if (damageCaused < 0 && repairLevel != 0) {

		int reducedDamage = Math.min(-1, damageCaused + (repairLevel / 14));
		stack.getOrCreateTag().putInt("Damage", Math.max(0, this.getDamage(stack) + reducedDamage));
	    }
	}

	stack.getOrCreateTag().putInt("Damage", Math.max(0, damage));
    }

    /**
     * blacklisted mending enchantment
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
    {
	if(enchantment == Enchantments.MENDING)
	    return false;

	if(enchantment == Enchantments.BINDING_CURSE)
	    return true;
	
	return enchantment.type.canEnchantItem(stack.getItem());
    }
}
