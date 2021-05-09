package com.telepathicgrunt.the_bumblezone.enchantments;

import com.resourcefulbees.resourcefulbees.registry.ModItems;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ResourcefulBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
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
                if(entry.getKey().location().getPath().contains("comb")){
                    validBlocks.add(entry.getValue());
                }
            });
            return validBlocks;
        }
    );

    private static final Lazy<Set<Block>> LESSER_TARGET_BLOCKS = Lazy.of(
            () -> {
                Set<Block> validBlocks = new HashSet<>();
                ForgeRegistries.BLOCKS.getEntries().forEach(entry ->{
                    String path = entry.getKey().location().getPath();
                    if(entry.getValue() instanceof BeehiveBlock || path.contains("hive") || path.contains("nest") || (path.contains("wax") && !path.contains("waxed"))){
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

    public Set<Block> getLesserTargetBlocks(){
        return LESSER_TARGET_BLOCKS.get();
    }

    public static void attemptFasterMining(PlayerEvent.BreakSpeed event){
        if(BzEnchantments.COMB_CUTTER.get().getTargetBlocks().contains(event.getState().getBlock())){
            mineFaster(event, false);
        }
        else if(BzEnchantments.COMB_CUTTER.get().getLesserTargetBlocks().contains(event.getState().getBlock())){
            mineFaster(event, true);
        }
    }

    private static void mineFaster(PlayerEvent.BreakSpeed event, boolean lesserTarget) {
        float breakSpeed = event.getNewSpeed();
        PlayerEntity playerEntity = event.getPlayer();
        ItemStack itemStack = playerEntity.getMainHandItem();
        int equipmentLevel = EnchantmentHelper.getEnchantmentLevel(BzEnchantments.COMB_CUTTER.get(), playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            breakSpeed += (float)(equipmentLevel * equipmentLevel + (lesserTarget ? 3 : 13));
        }
        event.setNewSpeed(breakSpeed);
    }

    public static void increasedCombDrops(PlayerEntity playerEntity, World world, BlockPos pos){
        ItemStack itemStack = playerEntity.getMainHandItem();
        int equipmentLevel = EnchantmentHelper.getEnchantmentLevel(BzEnchantments.COMB_CUTTER.get(), playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            Block.popResource(world, pos, new ItemStack(Items.HONEYCOMB, equipmentLevel * 3));
        }
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ShearsItem || item instanceof SwordItem || (ModChecker.resourcefulBeesPresent && ResourcefulBeesRedirection.isRBComb(item));
    }
}
