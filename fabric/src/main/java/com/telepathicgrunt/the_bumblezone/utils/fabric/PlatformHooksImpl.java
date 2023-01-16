package com.telepathicgrunt.the_bumblezone.utils.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Contract;

public class PlatformHooksImpl {

    public static ModInfo getModInfo(String modid, boolean qualifierIsVersion) {
        return FabricLoader.getInstance()
                .getModContainer(modid)
                .map(container -> new FabricModInfo(container.getMetadata()))
                .orElse(null);
    }

    @Contract(pure = true)
    @SuppressWarnings("UnstableApiUsage")
    public static Fluid getBucketFluid(BucketItem bucket) {
        final ItemStack bucketStack = new ItemStack(bucket);
        final Storage<FluidVariant> storage = FluidStorage.ITEM.find(bucketStack, ContainerItemContext.withConstant(bucketStack));
        if (storage instanceof FullItemFluidStorage fullItem) {
            return fullItem.getResource().getFluid();
        }
        return Fluids.EMPTY;
    }

    @Contract(pure = true)
    public static boolean hasCraftingRemainder(ItemStack stack) {
        return stack.getItem().hasCraftingRemainingItem();
    }

    @Contract(pure = true)
    public static ItemStack getCraftingRemainder(ItemStack stack) {
        final Item item = stack.getItem().getCraftingRemainingItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    @Contract(pure = true)
    public static int getXpDrop(LivingEntity entity, Player attackingPlayer, int xp) {
        //TODO Find event for experience drop
        return xp;
    }

    @Contract(pure = true)
    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Contract(pure = true)
    public static boolean isFakePlayer(ServerPlayer player) {
        //Crude way of doing it but it should work for almost all cases.
        return player != null && player.getClass() == ServerPlayer.class;
    }

    @Contract(pure = true)
    public static ServerPlayer getFakePlayer(ServerLevel level) {
        return new FakePlayerBuilder(new ResourceLocation(Bumblezone.MODID, "default_fake_player"))
                .create(level.getServer(), level, "fake_player");
    }

    @Contract(pure = true)
    public static int canEntitySpawn(Mob entity, LevelAccessor world, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason) {
        //TODO Find event for entity spawn
        return 0;
    }

    public static boolean sendBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        boolean result = PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, player, pos, state, entity);
        if (!result) {
            PlayerBlockBreakEvents.CANCELED.invoker().onBlockBreakCanceled(level, player, pos, state, entity);
            return true;
        }
        return false;
    }

    public static void afterBlockBreakEvent(Level level, BlockPos pos, BlockState state, BlockEntity entity, Player player) {
        PlayerBlockBreakEvents.AFTER.invoker().afterBlockBreak(level, player, pos, state, entity);
    }
}
