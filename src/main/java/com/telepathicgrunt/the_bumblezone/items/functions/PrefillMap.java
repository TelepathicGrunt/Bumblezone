package com.telepathicgrunt.the_bumblezone.items.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class PrefillMap extends LootItemConditionalFunction {
    final int scaleLevel;

    public PrefillMap(LootItemCondition[] lootItemConditions, int scaleLevel) {
        super(lootItemConditions);
        this.scaleLevel = scaleLevel;
    }

    @Override
    public LootItemFunctionType getType() {
        return BzLootFunctionTypes.PREFILL_MAP.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        if (itemStack.is(Items.MAP)) {
            Vec3 vec3 = lootContext.getParamOrNull(LootContextParams.ORIGIN);
            if (vec3 != null) {
                BlockPos blockPos = new BlockPos(vec3);
                ItemStack newFilledMap = MapItem.create(lootContext.getLevel(), blockPos.getX(), blockPos.getZ(), (byte) scaleLevel, true, true);
                Integer mapId = MapItem.getMapId(newFilledMap);
                MapItemSavedData data = MapItem.getSavedData(mapId, lootContext.getLevel());
                if (data != null) {
                    ThreadExecutor.mapFilling(lootContext.getLevel(), blockPos, data);
                    newFilledMap.setTag(newFilledMap.getOrCreateTag().merge(itemStack.getOrCreateTag()));
                    return newFilledMap;
                }
            }
            else if (itemStack.hasTag()) {
                itemStack.setTag(new CompoundTag());
            }
        }
        return itemStack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<PrefillMap> {
        public void serialize(JsonObject jsonObject, PrefillMap prefillMap, JsonSerializationContext context) {
            super.serialize(jsonObject, prefillMap, context);
            if (prefillMap.scaleLevel != 0) {
                jsonObject.addProperty("scale", prefillMap.scaleLevel);
            }
        }

        public PrefillMap deserialize(JsonObject jsonObject, JsonDeserializationContext context, LootItemCondition[] lootItemConditions) {
            return new PrefillMap(lootItemConditions, GsonHelper.getAsInt(jsonObject, "scale", 0));
        }
    }

    public static void update(Level level, BlockPos blockPos, MapItemSavedData data) {
        if (level.dimension() == data.dimension) {
            boolean bumblezoneDim = level.dimension().equals(BzDimension.BZ_WORLD_KEY);
            boolean ceilingDim = level.dimensionType().hasCeiling();
            int scale = 1 << data.scale;
            int xStart = data.x;
            int zStart = data.z;
            int intervalSpacing = 2;
            int radiusToSkip = ceilingDim ? 64 : 128;

            for(int scanX = 0; scanX < 128; scanX += intervalSpacing) {
                double d0 = 0.0D;
                for(int scanZ = 0; scanZ < 128; scanZ += intervalSpacing) {
                    int blockPosX = (xStart / scale + scanX - 64) * scale;
                    int blockPosZ = (zStart / scale + scanZ - 64) * scale;

                    int xDiff = Math.abs(blockPos.getX() - blockPosX);
                    int zDiff = Math.abs(blockPos.getZ() - blockPosZ);
                    if ((xDiff * xDiff) + (zDiff * zDiff) < radiusToSkip * radiusToSkip) {
                        continue;
                    }
                    if (((scanX % 18) > 9 || (scanZ % 18) > 9)) {
                        continue;
                    }
                    if (((scanX % 9) == 0 || (scanX % 9) == 8) && ((scanZ % 9) == 0 || (scanZ % 9) == 8)) {
                        continue;
                    }

                    BlockPos currentPos = new BlockPos(blockPosX, 0, blockPosZ);
                    Multiset<MaterialColor> multiset = LinkedHashMultiset.create();
                    LevelChunk levelchunk = level.getChunkAt(currentPos);
                    if (!levelchunk.isEmpty()) {
                        ChunkPos chunkpos = levelchunk.getPos();
                        int k3 = 0;
                        double d1 = 0.0D;
                        if (ceilingDim && !bumblezoneDim) {
                            int l3 = blockPosX + blockPosZ * 231871;
                            l3 = l3 * l3 * 31287121 + l3 * 11;
                            if ((l3 >> 20 & 1) == 0) {
                                multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
                            }
                            else {
                                multiset.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
                            }

                            d1 = 100.0D;
                        }
                        else {
                            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                            BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
                            int scanY = levelchunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPosX, blockPosZ) + 1;
                            if (scanY >= 255 && bumblezoneDim) {
                                scanY = 110;
                            }
                            BlockState blockstate;
                            if (scanY <= level.getMinBuildHeight() + 1) {
                                blockstate = Blocks.BEDROCK.defaultBlockState();
                            }
                            else {
                                do {
                                    --scanY;
                                    mutableBlockPos.set(chunkpos.getMinBlockX() + blockPosX, scanY, chunkpos.getMinBlockZ() + blockPosZ);
                                    blockstate = levelchunk.getBlockState(mutableBlockPos);
                                } while(blockstate.getMapColor(level, mutableBlockPos) == MaterialColor.NONE && scanY > level.getMinBuildHeight());

                                if (scanY > level.getMinBuildHeight() && !blockstate.getFluidState().isEmpty()) {
                                    int secondScanY = scanY - 1;
                                    mutableBlockPos1.set(mutableBlockPos);

                                    BlockState blockstate1;
                                    do {
                                        mutableBlockPos1.setY(secondScanY--);
                                        blockstate1 = levelchunk.getBlockState(mutableBlockPos1);
                                        ++k3;
                                    } while(secondScanY > level.getMinBuildHeight() && !blockstate1.getFluidState().isEmpty());

                                    blockstate = getCorrectStateForFluidBlock(level, blockstate, mutableBlockPos);
                                }
                            }

                            d1 += (double)scanY / (double)(scale * scale);
                            multiset.add(blockstate.getMapColor(level, mutableBlockPos));
                        }

                        k3 /= scale * scale;
                        MaterialColor materialcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MaterialColor.NONE);
                        MaterialColor.Brightness materialcolor$brightness;
                        if (materialcolor == MaterialColor.WATER) {
                            double d2 = (double)k3 * 0.1D;
                            if (d2 < 0.5D) {
                                materialcolor$brightness = MaterialColor.Brightness.HIGH;
                            }
                            else if (d2 > 0.9D) {
                                materialcolor$brightness = MaterialColor.Brightness.LOW;
                            }
                            else {
                                materialcolor$brightness = MaterialColor.Brightness.NORMAL;
                            }
                        }
                        else {
                            double slopeState = (d1 - d0) * 4.0D / (double)(scale + 4) - 0.2D;
                            if (slopeState > 0.6D) {
                                materialcolor$brightness = MaterialColor.Brightness.HIGH;
                            }
                            else if (slopeState < -0.6D) {
                                materialcolor$brightness = MaterialColor.Brightness.LOW;
                            }
                            else {
                                materialcolor$brightness = MaterialColor.Brightness.NORMAL;
                            }
                        }

                        d0 = d1;
                        data.updateColor(scanX, scanZ, materialcolor.getPackedId(materialcolor$brightness));
                    }
                }
            }
        }
    }

    private static BlockState getCorrectStateForFluidBlock(Level level, BlockState state, BlockPos pos) {
        FluidState fluidstate = state.getFluidState();
        return !fluidstate.isEmpty() && !state.isFaceSturdy(level, pos, Direction.UP) ? fluidstate.createLegacyBlock() : state;
    }
}