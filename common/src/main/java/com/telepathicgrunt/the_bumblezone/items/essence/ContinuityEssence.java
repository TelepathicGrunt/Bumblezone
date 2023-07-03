package com.telepathicgrunt.the_bumblezone.items.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
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
    private static final Style INTENTIONAL_GAME_DESIGN_STYLE = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://bugs.mojang.com/browse/MCPE-28723")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("MCPE-28723")));

    private record TickCapsule(Runnable runnable, long tickTarget) {}

    public ContinuityEssence(Properties properties) {
        super(properties, cooldownLengthInTicks, abilityUseAmount);
    }

    @Override
    void addDescriptionComponents(List<Component> components) {
        components.add(Component.translatable("item.the_bumblezone.essence_continuity_description_1").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC));
        components.add(Component.translatable("item.the_bumblezone.essence_continuity_description_2").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC));
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
                    respawn(stack, continuityEssence, player, server, event.source());
                }
                return true;
            }
        }
        return false;
    }

    private static void respawn(ItemStack stack, ContinuityEssence continuityEssence, ServerPlayer serverPlayer, MinecraftServer server, DamageSource damageSource) {
        ResourceKey<Level> oldDimension = serverPlayer.level().dimension();
        BlockPos oldPosition = serverPlayer.blockPosition();
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

            ItemStack newBook = Items.WRITTEN_BOOK.getDefaultInstance();
            CompoundTag compoundTag = newBook.getOrCreateTag();
            compoundTag.putString(WrittenBookItem.TAG_TITLE, "Essence of Continuity Record");
            compoundTag.putString(WrittenBookItem.TAG_AUTHOR, serverPlayer.getName().getString());

            ListTag listTag = new ListTag();
            Entity causer = damageSource.getEntity();
            if (causer == null) {
                listTag.add(StringTag.valueOf(Component.Serializer.toJson(Component.translatable(
                        "item.the_bumblezone.essence_continuity_written_book_body_no_causer",
                        java.time.LocalDate.now(),
                        oldDimension.location(),
                        oldPosition.getX(),
                        oldPosition.getY(),
                        oldPosition.getZ(),
                        getDeathMessage(finalDestination, damageSource, serverPlayer)))));
            }
            else {
                listTag.add(StringTag.valueOf(Component.Serializer.toJson(Component.translatable(
                        "item.the_bumblezone.essence_continuity_written_book_body",
                        java.time.LocalDate.now(),
                        oldDimension.location(),
                        oldPosition.getX(),
                        oldPosition.getY(),
                        oldPosition.getZ(),
                        causer.getName(),
                        getDeathMessage(finalDestination, damageSource, serverPlayer)))));
            }
            compoundTag.put(WrittenBookItem.TAG_PAGES, listTag);

            ItemEntity itementity = new ItemEntity(finalDestination,
                    playerRespawnPosition.x(),
                    playerRespawnPosition.y(),
                    playerRespawnPosition.z(),
                    newBook);
            itementity.setDefaultPickUpDelay();
            finalDestination.addFreshEntity(itementity);
        }
        else if (respawningLinkedPosition != null) {
            serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0f));
        }
    }

    public static Component getDeathMessage(ServerLevel serverLevel, DamageSource damageSource, ServerPlayer serverPlayer) {
        if (damageSource.type() == serverLevel.damageSources().generic().type()) {
            return Component.translatable("death.attack.generic", serverPlayer.getDisplayName());
        }
        DeathMessageType deathMessageType = damageSource.type().deathMessageType();
        if (deathMessageType == DeathMessageType.FALL_VARIANTS && damageSource.getEntity() != null) {
            return getFallMessage(damageSource.getEntity(), serverPlayer);
        }
        if (deathMessageType == DeathMessageType.INTENTIONAL_GAME_DESIGN) {
            String string = "death.attack." + damageSource.getMsgId();
            MutableComponent component = ComponentUtils.wrapInSquareBrackets(Component.translatable(string + ".link")).withStyle(INTENTIONAL_GAME_DESIGN_STYLE);
            return Component.translatable(string + ".message", serverPlayer.getDisplayName(), component);
        }
        return damageSource.getLocalizedDeathMessage(serverPlayer);
    }

    private static Component getFallMessage(Entity entity, ServerPlayer serverPlayer) {
        Component component = entity.getDisplayName();
        if (component != null) {
            return getMessageForAssistedFall(serverPlayer, entity, component, "death.fell.finish.item", "death.fell.finish");
        }
        return Component.translatable("death.fell.killer", entity.getDisplayName());
    }

    private static Component getMessageForAssistedFall(ServerPlayer serverPlayer, Entity entity, Component component, String string, String string2) {
        ItemStack itemStack;

        if (entity instanceof LivingEntity livingEntity) {
            itemStack = livingEntity.getMainHandItem();
        }
        else {
            itemStack = ItemStack.EMPTY;
        }

        if (!itemStack.isEmpty() && itemStack.hasCustomHoverName()) {
            return Component.translatable(string, serverPlayer.getDisplayName(), component, itemStack.getDisplayName());
        }

        return Component.translatable(string2, serverPlayer.getDisplayName(), component);
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