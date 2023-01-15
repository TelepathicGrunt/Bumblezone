package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionOfBeesCompat {
    private static EntityType<?> SPLASH_POTION_OF_BEES_ENTITY;
    private static EntityType<?> LINGERING_POTION_OF_BEES_ENTITY;
    private static Item POTION_OF_BEES;
    private static Item SPLASH_POTION_OF_BEES;
    private static Item LINGERING_POTION_OF_BEES;

    public static void setupCompat() {
        POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation("potionofbees", "potion_of_bees"));
        SPLASH_POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation("potionofbees", "splash_potion_of_bees"));
        LINGERING_POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation("potionofbees", "lingering_potion_of_bees"));
        SPLASH_POTION_OF_BEES_ENTITY = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("potionofbees", "splash_potion_of_bees"));
        LINGERING_POTION_OF_BEES_ENTITY = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("potionofbees", "lingering_potion_of_bees"));

        if (POTION_OF_BEES != null && BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock.get()) {
            PotionOfBeesBeePotionDispenseBehavior.DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(POTION_OF_BEES));
            DispenserBlock.registerBehavior(POTION_OF_BEES, new PotionOfBeesBeePotionDispenseBehavior()); // adds compatibility with bee potions in dispensers
        }

        if (SPLASH_POTION_OF_BEES != null && BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock.get()) {
            PotionOfBeesBeePotionDispenseBehavior.DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(SPLASH_POTION_OF_BEES));
            DispenserBlock.registerBehavior(SPLASH_POTION_OF_BEES, new PotionOfBeesBeePotionDispenseBehavior()); // adds compatibility with bee splash potion in dispensers
        }

        if (LINGERING_POTION_OF_BEES != null && BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock.get()) {
            PotionOfBeesBeePotionDispenseBehavior.DEFAULT_LINGERING_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(LINGERING_POTION_OF_BEES));
            DispenserBlock.registerBehavior(LINGERING_POTION_OF_BEES, new PotionOfBeesBeePotionDispenseBehavior()); // adds compatibility with bee splash potion in dispensers
        }

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(PotionOfBeesCompat::ProjectileImpactEvent);

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.potionOfBeesPresent = true;
    }

    public static boolean isPotionOfBeesItem(ItemStack itemStack) {
        return itemStack.is(POTION_OF_BEES);
    }

    public static boolean isSplashPotionOfBeesItem(ItemStack itemStack) {
        return itemStack.is(SPLASH_POTION_OF_BEES);
    }

    public static boolean isLingeringPotionOfBeesItem(ItemStack itemStack) {
        return itemStack.is(LINGERING_POTION_OF_BEES);
    }

    public static InteractionResult potionOfBeeInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        if (isPotionOfBeesItem(itemstack)) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced potion of bee with glass bottle
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    /*
     * Check for if potion of bee's item was thrown and impacted empty honeycomb block to revive it
     */
    private static void ProjectileImpactEvent(ProjectileImpactEvent event) {
        if(BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock.get() && event.getProjectile() != null) {
            if (event.getProjectile().getType() == SPLASH_POTION_OF_BEES_ENTITY) {
                PotionOfBeesCompat.reviveLarvaBlockEvent(event.getProjectile(), event.getRayTraceResult(), 1);
            }
            else if (event.getProjectile().getType() == LINGERING_POTION_OF_BEES_ENTITY) {
                PotionOfBeesCompat.reviveLarvaBlockEvent(event.getProjectile(), event.getRayTraceResult(), 3);
            }
        }
    }

    private static void reviveLarvaBlockEvent(Projectile projectile, HitResult rayTraceResult, int range) {
        Level world = projectile.level; // world we threw in
        Vec3 hitBlockPos = rayTraceResult.getLocation(); //position of the collision
        BlockPos originalPosition = new BlockPos(hitBlockPos);
        reviveBroodsInRange(world, originalPosition, range);
    }

    public static void reviveBroodsInRange(Level world, BlockPos originalPosition, int range) {
        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos().set(originalPosition);
        BlockState block;
        //revive nearby larva blocks
        for(int x = -range; x <= range; x++) {
            for(int y = -range; y <= range; y++) {
                for(int z = -range; z <= range; z++) {
                    position.set(originalPosition).move(x, y, z);
                    block = world.getBlockState(position);

                    if(block.getBlock().equals(BzBlocks.EMPTY_HONEYCOMB_BROOD.get())) {
                        reviveLarvaBlock(world, block, position);
                    }
                }
            }
        }
    }

    private static void reviveLarvaBlock(Level world, BlockState state, BlockPos position) {
        world.setBlockAndUpdate(position,
            BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                .setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING))
                .setValue(HoneycombBrood.STAGE, 3 - world.random.nextInt(world.random.nextInt(world.random.nextInt(4) + 1) + 1)));
    }
}
