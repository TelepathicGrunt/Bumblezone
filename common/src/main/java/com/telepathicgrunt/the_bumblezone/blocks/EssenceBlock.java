package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


public abstract class EssenceBlock extends BaseEntityBlock {
    public static final GeneralUtils.Lazy<StructurePlaceSettings> PLACEMENT_SETTINGS = new GeneralUtils.Lazy<>(() ->
            new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setKeepLiquids(false)
                .setIgnoreEntities(true)
                .setKnownShape(true)
                .addProcessor(new BlockIgnoreProcessor(List.of(BzBlocks.ESSENCE_BLOCK_WHITE.get()))));

    public EssenceBlock(Properties properties) {
        super(properties
                .strength(-1.0f, 3600000.8f)
                .lightLevel((blockState) -> 15)
                .noCollission()
                .noLootTable()
                .noOcclusion()
                .forceSolidOn()
                .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                .pushReaction(PushReaction.BLOCK));
    }

    public abstract ResourceLocation getArenaNbt();

    public abstract int getEventTimeFrame();

    public abstract void performUniqueArenaTick(Level level, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity);

    public abstract ServerEssenceEvent getServerEssenceEvent();

    public abstract ItemStack getEssenceItemReward();

    public abstract int getEssenceXpReward();

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssenceBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ctx) {
            Entity entity = ctx.getEntity();
            if (entity == null) {
                return Shapes.empty();
            }

            if ((entity instanceof LivingEntity && !(entity instanceof ServerPlayer)) ||
                (entity instanceof ServerPlayer serverPlayer && !EssenceOfTheBees.hasEssence(serverPlayer)))
            {
                if (entity.getBoundingBox().inflate(0.01D).intersects(new AABB(blockPos, blockPos.offset(1, 1, 1)))) {
                    if (entity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.displayClientMessage(
                                Component.translatable("essence.the_bumblezone.missing_essence_effect").withStyle(ChatFormatting.RED),
                                true);
                    }
                    else if (!(entity instanceof Player)) {
                        entity.hurt(entity.damageSources().magic(), 1);
                    }

                    Vec3 center = Vec3.atCenterOf(blockPos);
                    entity.push(
                            entity.getX() - center.x(),
                            entity.getY() - center.y(),
                            entity.getZ() - center.z());
                }

                return Shapes.block();
            }

            if (entity instanceof ServerPlayer serverPlayer) {
                entityInside(blockState, serverPlayer.level(), blockPos, serverPlayer);
            }
        }
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof ServerPlayer player
            && EssenceOfTheBees.hasEssence(player) &&
            blockState.getBlock() instanceof EssenceBlock essenceBlock)
        {
            ServerLevel serverLevel = ((ServerLevel) level);
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
            if (blockEntity instanceof EssenceBlockEntity essenceBlockEntity &&
                essenceBlockEntity.getPlayerInArena().isEmpty())
            {

                StructureTemplateManager structureTemplateManager = serverLevel.getStructureManager();
                Optional<StructureTemplate> optionalStructureTemplate = structureTemplateManager.get(getArenaNbt());
                optionalStructureTemplate.ifPresent(loadingStructureTemplate -> {
                    Vec3i size = loadingStructureTemplate.getSize();
                    BlockPos negativeHalfLengths = new BlockPos(-size.getX() / 2, -size.getY() / 2, -size.getZ() / 2);

                    // Save area
                    StructureTemplate savingStructureTemplate;
                    try {
                        savingStructureTemplate = structureTemplateManager.getOrCreate(essenceBlockEntity.getSavedNbt());
                    }
                    catch (ResourceLocationException resourceLocationException) {
                        Bumblezone.LOGGER.warn("Bumblezone Essence Block failed to create the NBT file to save area - {} - {}", essenceBlockEntity, blockState);
                        return;
                    }
                    savingStructureTemplate.fillFromWorld(serverLevel, blockPos.offset(negativeHalfLengths), size, !PLACEMENT_SETTINGS.getOrFillFromInternal().isIgnoreEntities(), essenceBlock);
                    try {
                        structureTemplateManager.save(essenceBlockEntity.getSavedNbt());
                    }
                    catch (ResourceLocationException resourceLocationException) {
                        Bumblezone.LOGGER.warn("Bumblezone Essence Block failed to save area into NBT file - {} - {}", essenceBlockEntity, blockState);
                        return;
                    }

                    // load arena
                    GeneralUtils.placeInWorldWithouNeighborUpdate(
                            serverLevel,
                            loadingStructureTemplate,
                            blockPos.offset(negativeHalfLengths),
                            blockPos.offset(negativeHalfLengths),
                            PLACEMENT_SETTINGS.getOrFillFromInternal(),
                            serverLevel.getRandom(),
                            Block.UPDATE_CLIENTS + Block.UPDATE_KNOWN_SHAPE
                    );

                    Vec3 centerPos = Vec3.atCenterOf(blockPos);
                    Direction direction = Direction.NORTH;
                    double largestDistance = Float.MIN_VALUE;
                    double xDiff = centerPos.x() - entity.getX();
                    double yDiff = centerPos.y() - entity.getY();
                    double zDiff = centerPos.z() - entity.getZ();
                    for (Direction direction2 : Direction.Plane.HORIZONTAL) {
                        double distance = xDiff * (float)direction2.getNormal().getX() + yDiff * (float)direction2.getNormal().getY() + zDiff * (float)direction2.getNormal().getZ();
                        if (!(distance > largestDistance)) continue;
                        largestDistance = distance;
                        direction = direction2;
                    }

                    List<ServerPlayer> players = ((ServerLevel) level).getPlayers(p ->
                            (blockPos.getX() + size.getX()) > p.blockPosition().getX() &&
                            (blockPos.getY() + size.getY()) > p.blockPosition().getY() &&
                            (blockPos.getZ() + size.getZ()) > p.blockPosition().getZ() &&
                            (blockPos.getX() - size.getX()) < p.blockPosition().getX() &&
                            (blockPos.getY() - size.getY()) < p.blockPosition().getY() &&
                            (blockPos.getZ() - size.getZ()) < p.blockPosition().getZ()
                    );

                    essenceBlockEntity.getPlayerInArena().clear();
                    for (int i = 0; i < players.size(); i++) {
                        ServerPlayer serverPlayer = players.get(i);
                        essenceBlockEntity.getPlayerInArena().add(serverPlayer.getUUID());

                        // Prevent players stuck in walls
                        if ((blockPos.getX() + size.getX() - 2) < serverPlayer.blockPosition().getX() ||
                            (blockPos.getY() + size.getY() - 2) < serverPlayer.blockPosition().getY() ||
                            (blockPos.getZ() + size.getZ() - 2) < serverPlayer.blockPosition().getZ() ||
                            (blockPos.getX() - size.getX() + 2) > serverPlayer.blockPosition().getX() ||
                            (blockPos.getY() - size.getY() + 2) > serverPlayer.blockPosition().getY() ||
                            (blockPos.getZ() - size.getZ() + 2) > serverPlayer.blockPosition().getZ())
                        {
                            serverPlayer.setDeltaMovement(0, 0, 0);
                            serverPlayer.teleportTo(
                                    blockPos.getX() - ((7 * direction.getStepX()) + (i % 2 == 0 ? i : -i)),
                                    blockPos.getY() + negativeHalfLengths.getY() + 1,
                                    blockPos.getZ() - (7 * direction.getStepZ())
                            );
                        }

                        spawnParticles(serverLevel, serverPlayer.position(), serverPlayer.getRandom());
                    }
                });

                essenceBlockEntity.setEventTimer(this.getEventTimeFrame());
                essenceBlockEntity.setChanged();
            }
        }
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.1f) {
            level.addParticle(
                    BzParticles.SPARKLE_PARTICLE.get(),
                    (double)blockPos.getX() + (randomSource.nextDouble() * 1.5D) - 0.25D,
                    (double)blockPos.getY() + (randomSource.nextDouble() * 1.5D) - 0.25D,
                    (double)blockPos.getZ() + (randomSource.nextDouble() * 1.5D) - 0.25D,
                    randomSource.nextGaussian() * 0.003d,
                    randomSource.nextGaussian() * 0.003d,
                    randomSource.nextGaussian() * 0.003d);
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

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return EssenceBlock.createEssenceTicker(level, blockEntityType, BzBlockEntities.ESSENCE_BLOCK.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createEssenceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends EssenceBlockEntity> blockEntityType2) {
        return level.isClientSide ? null : EssenceBlock.createTickerHelper(blockEntityType, blockEntityType2, EssenceBlockEntity::serverTick);
    }
}
