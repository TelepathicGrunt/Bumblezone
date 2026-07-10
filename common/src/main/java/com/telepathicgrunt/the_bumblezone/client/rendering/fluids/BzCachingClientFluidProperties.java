package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class BzCachingClientFluidProperties implements BzDiagonalClientFluidProperties {

    private SpriteId cachedStillSpriteId = null;
    public SpriteId stillSpriteId() {
        if (cachedStillSpriteId == null) {
            cachedStillSpriteId = new SpriteId(Sheets.BLOCKS_MAPPER.sheet(), still().sprite());
        }
        return cachedStillSpriteId;
    }

    private SpriteId cachedFlowingSpriteId = null;
    public SpriteId flowingSpriteId() {
        if (cachedFlowingSpriteId == null) {
            cachedFlowingSpriteId = new SpriteId(Sheets.BLOCKS_MAPPER.sheet(), flowing().sprite());
        }
        return cachedFlowingSpriteId;
    }

    private SpriteId cachedFlowingDiagonalSpriteId = null;
    public SpriteId flowingDiagonalSpriteId() {
        if (cachedFlowingDiagonalSpriteId == null) {
            cachedFlowingDiagonalSpriteId = new SpriteId(Sheets.BLOCKS_MAPPER.sheet(), flowingDiagonal().sprite());
        }
        return cachedFlowingDiagonalSpriteId;
    }

    private SpriteId cachedOverlaySpriteId = null;
    public SpriteId overlaySpriteId() {
        if (cachedOverlaySpriteId == null) {
            cachedOverlaySpriteId = new SpriteId(Sheets.BLOCKS_MAPPER.sheet(), overlay().sprite());
        }
        return cachedOverlaySpriteId;
    }

    @Override
    public Material still() {
        throw new NotImplementedException();
    }

    @Override
    public Material flowing() {
        throw new NotImplementedException();
    }

    @Override
    public Material overlay() {
        throw new NotImplementedException();
    }

    @Override
    public Identifier screenOverlay() {
        throw new NotImplementedException();
    }

    @Override
    public int tintColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, @Nullable FluidState state) {
        throw new NotImplementedException();
    }
}