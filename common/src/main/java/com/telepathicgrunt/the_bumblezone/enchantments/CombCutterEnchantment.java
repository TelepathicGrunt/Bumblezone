package com.telepathicgrunt.the_bumblezone.enchantments;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class CombCutterEnchantment extends BzEnchantment {
    private static final GeneralUtils.Lazy<Set<Block>> TARGET_BLOCKS = new GeneralUtils.Lazy<>();
    private static final GeneralUtils.Lazy<Set<Block>> LESSER_TARGET_BLOCKS = new GeneralUtils.Lazy<>();

    public CombCutterEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public Set<Block> getTargetBlocks() {
        return TARGET_BLOCKS.getOrCompute(() -> {
            Set<Block> validBlocks = new HashSet<>();
            for (ResourceLocation key : BuiltInRegistries.BLOCK.keySet()) {
                String path = key.getPath();
                if(path.contains("comb") || path.contains("luminescent_wax") || path.contains("ancient_wax")) {
                    validBlocks.add(BuiltInRegistries.BLOCK.get(key));
                }
            }
            return validBlocks;
        });
    }

    public Set<Block> getLesserTargetBlocks() {
        return LESSER_TARGET_BLOCKS.getOrCompute(() -> {
            Set<Block> validBlocks = new HashSet<>();
            for (ResourceLocation key : BuiltInRegistries.BLOCK.keySet()) {
                String path = key.getPath();
                Block block = BuiltInRegistries.BLOCK.get(key);
                if (block instanceof BeehiveBlock || path.contains("hive") || path.contains("nest") || (path.contains("wax") && !path.contains("waxed"))) {
                    validBlocks.add(block);
                }
            }
            return validBlocks;
        });
    }

    public static void attemptFasterMining(PlayerBreakSpeedEvent event){
        if (BzEnchantments.COMB_CUTTER.get().getTargetBlocks().contains(event.state().getBlock())){
            mineFaster(event, false);
        }
        else if (BzEnchantments.COMB_CUTTER.get().getLesserTargetBlocks().contains(event.state().getBlock())){
            mineFaster(event, true);
        }
    }

    private static void mineFaster(PlayerBreakSpeedEvent event, boolean lesserTarget) {
        Player playerEntity = event.player();
        ItemStack itemStack = playerEntity.getMainHandItem();
        int equipmentLevel = EnchantmentHelper.getEnchantmentLevel(BzEnchantments.COMB_CUTTER.get(), playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            int destroyModifier = (int) Math.max(Math.ceil(event.state().getBlock().defaultDestroyTime()), 0);
            double newSpeed = Math.min(Math.pow(equipmentLevel, 2 + destroyModifier) + (lesserTarget ? 3 : 13), 50);
            if (playerEntity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                int amplifier = playerEntity.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier();
                if (amplifier < 0) {
                    amplifier = 3;
                }
                newSpeed /= ((Math.pow(amplifier, 6) + 2) * 20);
            }

            event.speed().addAndGet(newSpeed);
        }
    }

    public static void increasedCombDrops(Player playerEntity, Level world, BlockPos pos) {
        ItemStack itemStack = playerEntity.getMainHandItem();
        int equipmentLevel = EnchantmentHelper.getEnchantmentLevel(BzEnchantments.COMB_CUTTER.get(), playerEntity);
        if (equipmentLevel > 0 && !itemStack.isEmpty()) {
            Block.popResource(world, pos, new ItemStack(Items.HONEYCOMB, equipmentLevel * 3));
            if(playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.COMB_CUTTER_EXTRA_DROPS_TRIGGER.trigger(serverPlayer);
            }
        }
    }

    @Override
    public int getMinCost(int level) {
        return 1 + (55 * (level - 1));
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 23;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(BzTags.ENCHANTABLE_COMB_CUTTER) || stack.is(Items.BOOK);
    }

    @Override
    public OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack) {
        return OptionalBoolean.of(this.canEnchant(stack));
    }
}
