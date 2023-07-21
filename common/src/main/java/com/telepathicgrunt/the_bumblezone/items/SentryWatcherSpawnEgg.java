package com.telepathicgrunt.the_bumblezone.items;


import com.telepathicgrunt.the_bumblezone.entities.nonliving.SentryWatcherEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class SentryWatcherSpawnEgg extends Item {
    private final Supplier<? extends EntityType<? extends Entity>> entityType;
    public static final UUID DISPENSER_OWNER_UUID = new UUID(0, 0);

    public SentryWatcherSpawnEgg(Supplier<? extends EntityType<? extends Entity>> typeIn, Properties builder) {
        super(builder);
        this.entityType = typeIn;
        setupDispenserBehavior();
    }

    protected void setupDispenserBehavior() {
        // Have to manually add dispenser behavior due to forge item registry event running too late.
        DispenserBlock.registerBehavior(
                this,
                new DefaultDispenseItemBehavior() {
                    public ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);

                        EntityType<?> entitytype = ((SentryWatcherSpawnEgg)stack.getItem()).getType(stack.getTag());
                        Entity entity = entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                        if (entity instanceof SentryWatcherEntity sentryWatcherEntity) {
                            sentryWatcherEntity.setTargetFacing(direction);
                            sentryWatcherEntity.setOwner(Optional.of(DISPENSER_OWNER_UUID));
                        }

                        stack.shrink(1);
                        return stack;
                    }
                });
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }
        else if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        else {
            ItemStack itemStack = useOnContext.getItemInHand();
            BlockPos blockPos = useOnContext.getClickedPos();
            Direction direction = useOnContext.getClickedFace();
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(Blocks.SPAWNER)) {
                if (!player.getAbilities().instabuild) {
                    if (player instanceof ServerPlayer) {
                        player.displayClientMessage(Component.translatable("system.the_bumblezone.sentry_watcher_egg_spawner_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), true);
                    }
                    return InteractionResult.FAIL;
                }

                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                    EntityType<?> entityType = this.getType(itemStack.getTag());
                    spawnerBlockEntity.setEntityId(entityType, level.getRandom());
                    blockEntity.setChanged();
                    level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                    level.gameEvent(useOnContext.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                    itemStack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockPos2;
            if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                blockPos2 = blockPos;
            }
            else {
                blockPos2 = blockPos.relative(direction);
            }

            EntityType<?> entityType2 = this.getType(itemStack.getTag());
            Entity entity = entityType2.spawn((ServerLevel)level, itemStack, useOnContext.getPlayer(), blockPos2, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
            if (entity != null) {
                if (entity instanceof SentryWatcherEntity sentryWatcherEntity) {
                    if (useOnContext.getClickedFace().getAxis() != Direction.Axis.Y) {
                        sentryWatcherEntity.setTargetFacing(useOnContext.getHorizontalDirection().getOpposite());
                    }
                    else {
                        sentryWatcherEntity.setTargetFacing(useOnContext.getHorizontalDirection());
                    }

                    if (player instanceof ServerPlayer serverPlayer) {
                        BzCriterias.SENTRY_WATCHER_SPAWN_EGG_USED_TRIGGER.trigger(serverPlayer);
                    }

                    if (!player.getAbilities().instabuild) {
                        sentryWatcherEntity.setOwner(Optional.of(player.getUUID()));
                        if (player instanceof ServerPlayer) {
                            player.displayClientMessage(Component.translatable("system.the_bumblezone.sentry_watcher_egg_removal_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), true);
                        }
                    }
                }

                itemStack.shrink(1);
                level.gameEvent(useOnContext.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }

            return InteractionResult.CONSUME;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        }
        else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        }
        else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(level.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemStack);
            }
            else if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
                EntityType<?> entityType = this.getType(itemStack.getTag());
                Entity entity = entityType.spawn((ServerLevel)level, itemStack, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemStack);
                }
                else {
                    if (entity instanceof SentryWatcherEntity sentryWatcherEntity) {
                        sentryWatcherEntity.setTargetFacing(player.getDirection());

                        if (player instanceof ServerPlayer serverPlayer) {
                            BzCriterias.SENTRY_WATCHER_SPAWN_EGG_USED_TRIGGER.trigger(serverPlayer);
                        }

                        if (!player.getAbilities().instabuild) {
                            sentryWatcherEntity.setOwner(Optional.of(player.getUUID()));
                            if (player instanceof ServerPlayer) {
                                player.displayClientMessage(Component.translatable("system.the_bumblezone.sentry_watcher_egg_removal_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD), true);
                            }
                        }
                    }

                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
                    return InteractionResultHolder.consume(itemStack);
                }
            }
            else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }

    public EntityType<?> getType(@Nullable CompoundTag compoundTag) {
        if (compoundTag != null && compoundTag.contains("EntityTag", 10)) {
            CompoundTag compoundTag2 = compoundTag.getCompound("EntityTag");
            if (compoundTag2.contains("id", 8)) {
                return EntityType.byString(compoundTag2.getString("id")).orElse(this.entityType.get());
            }
        }

        return this.entityType.get();
    }
}