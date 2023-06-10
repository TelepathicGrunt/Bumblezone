package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
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

import java.util.EnumSet;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class PotionOfBeesCompat implements ModCompat {
    private static Optional<EntityType<?>> SPLASH_POTION_OF_BEES_ENTITY;
    private static Optional<EntityType<?>> LINGERING_POTION_OF_BEES_ENTITY;
    private static Optional<Item> POTION_OF_BEES;
    private static Optional<Item> SPLASH_POTION_OF_BEES;
    private static Optional<Item> LINGERING_POTION_OF_BEES;

    public PotionOfBeesCompat() {
        POTION_OF_BEES = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("potionofbees", "potion_of_bees"));
        SPLASH_POTION_OF_BEES = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("potionofbees", "splash_potion_of_bees"));
        LINGERING_POTION_OF_BEES = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("potionofbees", "lingering_potion_of_bees"));
        SPLASH_POTION_OF_BEES_ENTITY = BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation("potionofbees", "splash_potion_of_bees"));
        LINGERING_POTION_OF_BEES_ENTITY = BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation("potionofbees", "lingering_potion_of_bees"));

        if (POTION_OF_BEES.isPresent() && BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock) {
            PotionOfBeesBeePotionDispenseBehavior.DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(POTION_OF_BEES.get()));
            DispenserBlock.registerBehavior(POTION_OF_BEES.get(), new PotionOfBeesBeePotionDispenseBehavior()); // adds compatibility with bee potions in dispensers
        }

        if (SPLASH_POTION_OF_BEES.isPresent() && BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock) {
            PotionOfBeesBeePotionDispenseBehavior.DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(SPLASH_POTION_OF_BEES.get()));
            DispenserBlock.registerBehavior(SPLASH_POTION_OF_BEES.get(), new PotionOfBeesBeePotionDispenseBehavior()); // adds compatibility with bee splash potion in dispensers
        }

        if (LINGERING_POTION_OF_BEES.isPresent() && BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock) {
            PotionOfBeesBeePotionDispenseBehavior.DEFAULT_LINGERING_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(LINGERING_POTION_OF_BEES.get()));
            DispenserBlock.registerBehavior(LINGERING_POTION_OF_BEES.get(), new PotionOfBeesBeePotionDispenseBehavior()); // adds compatibility with bee splash potion in dispensers
        }

        ProjectileHitEvent.EVENT.addListener(PotionOfBeesCompat::onProjectileHit);

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.potionOfBeesPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.EMPTY_BROOD);
    }

    public static boolean isPotionOfBeesItem(ItemStack itemStack) {
        return POTION_OF_BEES.isPresent() && itemStack.is(POTION_OF_BEES.get());
    }

    public static boolean isSplashPotionOfBeesItem(ItemStack itemStack) {
        return SPLASH_POTION_OF_BEES.isPresent() && itemStack.is(SPLASH_POTION_OF_BEES.get());
    }

    public static boolean isLingeringPotionOfBeesItem(ItemStack itemStack) {
        return LINGERING_POTION_OF_BEES.isPresent() && itemStack.is(LINGERING_POTION_OF_BEES.get());
    }

    /*
     * Check for if potion of bee's item was thrown and impacted empty honeycomb block to revive it
     */
    private static void onProjectileHit(boolean cancelled, ProjectileHitEvent event) {
        if(BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock && event.projectile() != null) {
            if (SPLASH_POTION_OF_BEES_ENTITY.isPresent() && event.projectile().getType() == SPLASH_POTION_OF_BEES_ENTITY.get()) {
                PotionOfBeesCompat.reviveLarvaBlockEvent(event.projectile(), event.hitResult(), 1);
            }
            else if (LINGERING_POTION_OF_BEES_ENTITY.isPresent() && event.projectile().getType() == LINGERING_POTION_OF_BEES_ENTITY.get()) {
                PotionOfBeesCompat.reviveLarvaBlockEvent(event.projectile(), event.hitResult(), 3);
            }
        }
    }

    private static void reviveLarvaBlockEvent(Projectile projectile, HitResult rayTraceResult, int range) {
        Level world = projectile.level(); // world we threw in
        Vec3 hitBlockPos = rayTraceResult.getLocation(); //position of the collision
        BlockPos originalPosition = BlockPos.containing(hitBlockPos);
        reviveBroodsInRange(world, originalPosition, range);
    }

    @Override
    public InteractionResult onEmptyBroodInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        if (!BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock) return InteractionResult.PASS;
        if (isPotionOfBeesItem(itemstack)) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced potion of bee with glass bottle
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
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
