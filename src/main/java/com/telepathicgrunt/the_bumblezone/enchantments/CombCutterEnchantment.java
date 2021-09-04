package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ResourcefulBeesCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.BlockTags;
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
                    if(entry.getValue() instanceof BeehiveBlock || path.contains("hive") || path.contains("nest") || (path.contains("wax") && !path.contains("waxed")) || entry.getValue().is(BzBlockTags.FORGE_STORAGE_BLOCK_WAX)){
                        validBlocks.add(entry.getValue());
                    }
                });
                return validBlocks;
            }
    );

    public CombCutterEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentType.create("comb_cutter", CombCutterEnchantment::canEnchantItem), new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
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
    public int getMinCost(int level) {
        return 10 * (level - 1);
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 13;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return canEnchantItem(stack.getItem());
    }

    public static boolean canEnchantItem(Item item) {
        return item instanceof ShearsItem || item instanceof SwordItem || item == Items.BOOK || (ModChecker.resourcefulBeesPresent && ResourcefulBeesCompat.isRBComb(item));
    }
}
