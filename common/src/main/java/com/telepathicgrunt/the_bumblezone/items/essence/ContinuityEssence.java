package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class ContinuityEssence extends AbilityEssenceItem {

    private static final Supplier<Integer> cooldownLengthInTicks = () -> BzGeneralConfigs.continuityEssenceCooldown;
    private static final Supplier<Integer> abilityUseAmount = () -> 1;
    private static final ConcurrentLinkedQueue<TickCapsule> NEXT_TICK_PARTICLES = new ConcurrentLinkedQueue<>();
    private record TickCapsule(Runnable runnable, long tickTarget) {}

    public ContinuityEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_white_description_1").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_white_description_2").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC));
    }

    public void decrementAbilityUseRemaining(ItemStack stack, ServerPlayer serverPlayer, int decreaseAmount) {
        int getRemainingUse = Math.max(getAbilityUseRemaining(stack) - decreaseAmount, 0);
        setAbilityUseRemaining(stack, getRemainingUse);
        if (getRemainingUse == 0) {
            setDepleted(stack, serverPlayer, true);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
        TickCapsule tickCapsule = NEXT_TICK_PARTICLES.poll();
        if (tickCapsule != null) {
            if (level.getGameTime() > tickCapsule.tickTarget) {
                tickCapsule.runnable().run();
            }
            else {
                NEXT_TICK_PARTICLES.add(tickCapsule);
            }
        }

        // Uncomment this for debugging purposes
//        if (entity instanceof ServerPlayer serverPlayer) {
//            serverPlayer.getCooldowns().removeCooldown(this);
//        }

        super.inventoryTick(stack, level, entity, i, bl);
    }

    @Override
    void applyAbilityEffects(ItemStack stack, Level level, ServerPlayer serverPlayer) {}

    public static boolean CancelledDeath(EntityDeathEvent event) {
        LivingEntity livingEntity = event.entity();
        if (livingEntity instanceof ServerPlayer player) {
            ItemStack stack = player.getOffhandItem();
            if (player.isDeadOrDying() &&
                stack.getItem() instanceof ContinuityEssence continuityEssence &&
                getIsActive(stack) &&
                !player.getCooldowns().isOnCooldown(stack.getItem()))
            {
                player.setHealth(player.getMaxHealth());
                player.getFoodData().setExhaustion(0);
                player.getFoodData().eat(100, 100);
                player.clearFire();
                player.setAirSupply(player.getMaxAirSupply());
                player.invulnerableTime = 40;
                player.deathTime = 0;
                player.fallDistance = 0;
                player.stopSleeping();
                player.removeVehicle();
                player.ejectPassengers();
                player.setPortalCooldown(40);
                player.setDeltaMovement(new Vec3(0, 0, 0));
                player.setOldPosAndRot();

                List<MobEffectInstance> mobEffectInstances = new ArrayList<>(player.getActiveEffects());
                for (MobEffectInstance mobEffectInstance : mobEffectInstances) {
                    if (!mobEffectInstance.getEffect().isBeneficial()) {
                        player.removeEffect(mobEffectInstance.getEffect());
                    }
                }

                MinecraftServer server = player.level().getServer();
                if (server != null) {
                    spawnParticles(player.serverLevel(), player.position(), player.getRandom());
                    respawn(stack, continuityEssence, player, server);
                }
                return true;
            }
        }
        return false;
    }

    private static void respawn(ItemStack stack, ContinuityEssence continuityEssence, ServerPlayer serverPlayer, MinecraftServer server) {
        ResourceKey<Level> respawnDimension = serverPlayer.getRespawnDimension();
        BlockPos respawningLinkedPosition = serverPlayer.getRespawnPosition();
        float respawnAngle = serverPlayer.getRespawnAngle();
        boolean forcedRespawn = serverPlayer.isRespawnForced();

        ServerLevel desiredDestination = server.getLevel(respawnDimension);
        Optional<Vec3> optionalRespawnPoint = desiredDestination != null && respawningLinkedPosition != null ? Player.findRespawnPositionAndUseSpawnBlock(desiredDestination, respawningLinkedPosition, respawnAngle, forcedRespawn, true) : Optional.empty();
        ServerLevel finalDestination = desiredDestination != null && optionalRespawnPoint.isPresent() ? desiredDestination : server.overworld();

        if (optionalRespawnPoint.isPresent()) {
            Vec3 playerRespawnPosition = optionalRespawnPoint.get();
            BlockPos playerRespawnBlockPos = BlockPos.containing(playerRespawnPosition);

            BlockState blockState = finalDestination.getBlockState(respawningLinkedPosition);
            boolean isRespawnAnchor = blockState.is(Blocks.RESPAWN_ANCHOR);

            BzWorldSavedData.queueEntityToGenericTeleport(serverPlayer, finalDestination.dimension(), playerRespawnBlockPos, () -> {
                continuityEssence.decrementAbilityUseRemaining(stack, serverPlayer, 1);

                if (isRespawnAnchor) {
                    serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, respawningLinkedPosition.getX(), respawningLinkedPosition.getY(), respawningLinkedPosition.getZ(), 1.0f, 1.0f, finalDestination.getRandom().nextLong()));
                }

                NEXT_TICK_PARTICLES.add(new TickCapsule(() -> spawnParticles(finalDestination, playerRespawnPosition, finalDestination.getRandom()), serverPlayer.serverLevel().getGameTime() + 5));
            });
        }
        else if (respawningLinkedPosition != null) {
            serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0f));
        }
    }

    public static void spawnParticles(ServerLevel world, Vec3 location, RandomSource random) {
        world.sendParticles(
                ParticleTypes.FIREWORK,
                location.x(),
                location.y() + 1,
                location.z(),
                100,
                random.nextGaussian() * 0.1D,
                (random.nextGaussian() * 0.1D) + 0.1,
                random.nextGaussian() * 0.1D,
                random.nextFloat() * 0.4 + 0.2f);

        world.sendParticles(
                ParticleTypes.ENCHANT,
                location.x(),
                location.y() + 1,
                location.z(),
                400,
                1,
                1,
                1,
                random.nextFloat() * 0.5 + 1.2f);
    }
}