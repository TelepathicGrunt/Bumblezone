package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class CombCutterEnchantment extends Enchantment {
    private static final Lazy<Set<Block>> TARGET_BLOCKS = Lazy.of(
        () -> {
            Set<Block> validBlocks = new HashSet<>();
            ForgeRegistries.BLOCKS.getEntries().forEach(entry ->{
                if(entry.getKey().getLocation().getPath().contains("comb")){
                    validBlocks.add(entry.getValue());
                }
            });
            return validBlocks;
        }
    );

    public CombCutterEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentType.VANISHABLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    public Set<Block> getTargetBlocks(){
        return TARGET_BLOCKS.get();
    }

    public static void fasterMiningCombs(PlayerEvent.BreakSpeed event){
        if(BzEnchantments.COMB_CUTTER.get().getTargetBlocks().contains(event.getState().getBlock())){
            float breakSpeed = event.getNewSpeed();
            PlayerEntity playerEntity = event.getPlayer();
            ItemStack itemStack = playerEntity.getHeldItemMainhand();
            int equipmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(BzEnchantments.COMB_CUTTER.get(), playerEntity);
            if (equipmentLevel > 0 && !itemStack.isEmpty()) {
                breakSpeed += (float)(equipmentLevel * equipmentLevel + 13);
            }
            event.setNewSpeed(breakSpeed);
        }
    }

    public static void increasedCombDrops(PlayerEntity playerEntity, World world, BlockPos pos){
        ItemStack itemStack = playerEntity.getHeldItemMainhand();
        int equipmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(BzEnchantments.COMB_CUTTER.get(), playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            Block.spawnAsEntity(world, pos, new ItemStack(Items.HONEYCOMB, equipmentLevel * 3));
        }
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem || stack.getItem() instanceof SwordItem;
    }
}
