package com.telepathicgrunt.bumblezone.enchantments;

import com.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class CombCutterEnchantment extends Enchantment {
    private static final GeneralUtils.Lazy<Set<Block>> TARGET_BLOCKS = new GeneralUtils.Lazy<>();
    private static final GeneralUtils.Lazy<Set<Block>> LESSER_TARGET_BLOCKS = new GeneralUtils.Lazy<>();

    public CombCutterEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentTarget.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public Set<Block> getTargetBlocks(){
        return TARGET_BLOCKS.getOrCompute(() -> {
            Set<Block> validBlocks = new HashSet<>();
            Registry.BLOCK.getEntries().forEach(entry ->{
                if(entry.getKey().getValue().getPath().contains("comb")){
                    validBlocks.add(entry.getValue());
                }
            });
            return validBlocks;
        });
    }

    public Set<Block> getLesserTargetBlocks(){
        return LESSER_TARGET_BLOCKS.getOrCompute(() -> {
            Set<Block> validBlocks = new HashSet<>();
            Registry.BLOCK.getEntries().forEach(entry ->{
                String path = entry.getKey().getValue().getPath();
                if(entry.getValue() instanceof BeehiveBlock || path.contains("hive") || path.contains("nest") || (path.contains("wax") && !path.contains("waxed"))){
                    validBlocks.add(entry.getValue());
                }
            });
            return validBlocks;
        });
    }

    public static float attemptFasterMining(float breakSpeed, boolean lesserTarget, PlayerEntity playerEntity){
        ItemStack itemStack = playerEntity.getMainHandStack();
        int equipmentLevel = EnchantmentHelper.getEquipmentLevel(BzEnchantments.COMB_CUTTER, playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            breakSpeed += (float)(equipmentLevel * equipmentLevel + (lesserTarget ? 3 : 13));
        }
        return breakSpeed;
    }

    public static void increasedCombDrops(PlayerEntity playerEntity, World world, BlockPos pos){
        ItemStack itemStack = playerEntity.getMainHandStack();
        int equipmentLevel = EnchantmentHelper.getEquipmentLevel(BzEnchantments.COMB_CUTTER, playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            Block.dropStack(world, pos, new ItemStack(Items.HONEYCOMB, equipmentLevel * 3));
        }
    }

    @Override
    public int getMinPower(int level) {
        return 10 * (level - 1);
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 13;
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem || stack.getItem() instanceof SwordItem || stack.isOf(Items.BOOK);
    }
}
