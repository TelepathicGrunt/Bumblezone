package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class EssenceBlockGreen extends EssenceBlock {
    public EssenceBlockGreen() {
        super(Properties.of().mapColor(MapColor.COLOR_GREEN));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/green_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 2000;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.green_essence_event",
                BossEvent.BossBarColor.GREEN,
                BossEvent.BossBarOverlay.NOTCHED_6
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_LIFE.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        essenceBlockEntity.getEventBar().setProgress((float)essenceBlockEntity.getEventTimer() / getEventTimeFrame());
        BlockPos rootminPos = blockPos.offset(9, -3, 0);

        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();
        if (eventEntitiesInArena.isEmpty()) {
            Entity entity = BzEntities.ROOTMIN.get().spawn(serverLevel, rootminPos, MobSpawnType.TRIGGERED);
            if (entity instanceof RootminEntity rootminEntity) {
                rootminEntity.setEssenceController(essenceBlockEntity.getUUID());
                rootminEntity.setEssenceControllerBlockPos(essenceBlockEntity.getBlockPos());
                rootminEntity.setEssenceControllerDimension(serverLevel.dimension());
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atLowerCornerOf(Direction.SOUTH.getNormal()));
                eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));
            }
        }

        // Do behavior of shooting and stuff
        if (!eventEntitiesInArena.isEmpty()) {
            EssenceBlockEntity.EventEntities eventEntity = eventEntitiesInArena.get(0);
            Entity entity = serverLevel.getEntity(eventEntity.uuid());

            if (entity == null ||
                !(entity instanceof RootminEntity rootminEntity && rootminEntity.getEssenceController().equals(essenceBlockEntity.getUUID())))
            {
                eventEntitiesInArena.remove(0);
                return;
            }

            entity.moveTo(Vec3.atCenterOf(rootminPos).add(0, -0.5d, 0));
            entity.lookAt(EntityAnchorArgument.Anchor.FEET, Vec3.atLowerCornerOf(Direction.WEST.getNormal()).add(entity.position()));

            if (entity.tickCount % 20 == 0) {
                //rootminEntity.setTarget(serverPlayer);
            }
        }
    }
}
