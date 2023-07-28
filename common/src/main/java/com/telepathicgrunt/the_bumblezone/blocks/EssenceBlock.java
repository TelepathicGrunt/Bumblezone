package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.entities.living.BoundlessCrystalEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
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


public abstract class EssenceBlock extends BaseEntityBlock implements BlockExtension {
    public static final GeneralUtils.Lazy<StructurePlaceSettings> PLACEMENT_SETTINGS = new GeneralUtils.Lazy<>(() ->
            new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setKeepLiquids(false)
                .setIgnoreEntities(true)
                .setKnownShape(true)
                .addProcessor(new BlockIgnoreProcessor(List.of(
                    BzBlocks.ESSENCE_BLOCK_RED.get(),
                    BzBlocks.ESSENCE_BLOCK_PURPLE.get(),
                    BzBlocks.ESSENCE_BLOCK_BLUE.get(),
                    BzBlocks.ESSENCE_BLOCK_GREEN.get(),
                    BzBlocks.ESSENCE_BLOCK_YELLOW.get(),
                    BzBlocks.ESSENCE_BLOCK_WHITE.get()
                ))));

    public static final GeneralUtils.Lazy<StructurePlaceSettings> PLACEMENT_SETTINGS_WITH_ENTITIES = new GeneralUtils.Lazy<>(() ->
            new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setKeepLiquids(false)
                .setIgnoreEntities(false)
                .setKnownShape(true)
                .addProcessor(new BlockIgnoreProcessor(List.of(
                    BzBlocks.ESSENCE_BLOCK_RED.get(),
                    BzBlocks.ESSENCE_BLOCK_PURPLE.get(),
                    BzBlocks.ESSENCE_BLOCK_BLUE.get(),
                    BzBlocks.ESSENCE_BLOCK_GREEN.get(),
                    BzBlocks.ESSENCE_BLOCK_YELLOW.get(),
                    BzBlocks.ESSENCE_BLOCK_WHITE.get()
                ))));

    public EssenceBlock(Properties properties) {
        super(properties
                .strength(-1.0f, 3600000.8f)
                .lightLevel((blockState) -> 15)
                .noLootTable()
                .forceSolidOn()
                .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                .isViewBlocking((blockState, blockGetter, blockPos) -> false)
                .pushReaction(PushReaction.BLOCK));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ctx) {
            Entity entity = ctx.getEntity();
            if (entity == null || entity instanceof BoundlessCrystalEntity) {
                return Shapes.empty();
            }

            if ((entity instanceof LivingEntity && !(entity instanceof ServerPlayer)) ||
                (entity instanceof ServerPlayer serverPlayer && !EssenceOfTheBees.hasEssence(serverPlayer)))
            {
                if (entity.getBoundingBox().inflate(0.01D).intersects(new AABB(blockPos, blockPos.offset(1, 1, 1)))) {
                    if (entity instanceof ServerPlayer serverPlayer) {
                        BlockEntity blockEntity = level.getBlockEntity(blockPos);
                        if (blockEntity instanceof EssenceBlockEntity essenceBlockEntity &&
                                essenceBlockEntity.getPlayerInArena().isEmpty())
                        {
                            serverPlayer.displayClientMessage(
                                    Component.translatable("essence.the_bumblezone.missing_essence_effect").withStyle(ChatFormatting.RED),
                                    true);
                        }
                    }

                    entity.hurt(entity.damageSources().magic(), 0.5f);

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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssenceBlockEntity(blockPos, blockState);
    }

    public abstract ResourceLocation getArenaNbt();

    public abstract int getEventTimeFrame();

    public abstract void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity);

    public abstract ServerEssenceEvent getServerEssenceEvent();

    public abstract ItemStack getEssenceItemReward();

    public abstract int getEssenceXpReward();

    public void onEventEnd(ServerLevel serverLevel, EssenceBlockEntity essenceBlockEntity) {
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof ServerPlayer touchingPlayer
            && EssenceOfTheBees.hasEssence(touchingPlayer) &&
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
                    essenceBlockEntity.setArenaSize(new BlockPos(size));

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
                    GeneralUtils.placeInWorldWithoutNeighborUpdate(
                            serverLevel,
                            loadingStructureTemplate,
                            blockPos.offset(negativeHalfLengths),
                            blockPos.offset(negativeHalfLengths),
                            PLACEMENT_SETTINGS_WITH_ENTITIES.getOrFillFromInternal().setRotationPivot(blockPos),
                            serverLevel.getRandom(),
                            Block.UPDATE_CLIENTS + Block.UPDATE_KNOWN_SHAPE
                    );

                    List<ServerPlayer> players = ((ServerLevel) level).getPlayers(p ->
                            (blockPos.getX() + size.getX()) > p.blockPosition().getX() &&
                            (blockPos.getY() + size.getY()) > p.blockPosition().getY() &&
                            (blockPos.getZ() + size.getZ()) > p.blockPosition().getZ() &&
                            (blockPos.getX() - size.getX()) < p.blockPosition().getX() &&
                            (blockPos.getY() - size.getY()) < p.blockPosition().getY() &&
                            (blockPos.getZ() - size.getZ()) < p.blockPosition().getZ()
                    );

                    essenceBlockEntity.getPlayerInArena().clear();
                    for (ServerPlayer serverPlayer : players) {
                        essenceBlockEntity.getPlayerInArena().add(serverPlayer.getUUID());

                        // Teleport everyone to trigger player.
                        if (serverPlayer != touchingPlayer) {
                            serverPlayer.setDeltaMovement(
                                    touchingPlayer.getDeltaMovement().x(),
                                    touchingPlayer.getDeltaMovement().y(),
                                    touchingPlayer.getDeltaMovement().z()
                            );
                            serverPlayer.teleportTo(
                                    touchingPlayer.getX(),
                                    touchingPlayer.getY(),
                                    touchingPlayer.getZ()
                            );
                        }

                        spawnParticles(serverLevel, serverPlayer.position(), serverPlayer.getRandom());

                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                ChunkPos chunkPos = new ChunkPos(blockPos);
                                serverLevel.getChunk(chunkPos.x + x, chunkPos.z + z).setInhabitedTime(1512000);
                            }
                        }
                    }
                });

                essenceBlockEntity.getEventBar().setProgress(1f);
                essenceBlockEntity.setEventTimer(this.getEventTimeFrame());
                essenceBlockEntity.setChanged();
            }
        }
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        for (int i = 0; i <= 2; i++) {
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

    @Override
    public OptionalBoolean bz$shouldNotDisplayFluidOverlay() {
        return OptionalBoolean.TRUE;
    }
}
