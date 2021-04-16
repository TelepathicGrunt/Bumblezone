package net.telepathicgrunt.bumblezone.enchantments;

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
import net.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import net.telepathicgrunt.bumblezone.utils.GeneralUtils;

import java.util.HashSet;
import java.util.Set;

public class CombCutterEnchantment extends Enchantment {
    private static final GeneralUtils.Lazy<Set<Block>> TARGET_BLOCKS = new GeneralUtils.Lazy<>();

    public CombCutterEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
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

    public static float fasterMiningCombs(float breakSpeed, PlayerEntity playerEntity){
        ItemStack itemStack = playerEntity.getMainHandStack();
        int equipmentLevel = EnchantmentHelper.getEquipmentLevel(BzEnchantments.COMB_CUTTER, playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            breakSpeed += (float)(equipmentLevel * equipmentLevel + 12);
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
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem || stack.getItem() instanceof SwordItem;
    }
}
