package com.telepathicgrunt.the_bumblezone.utils.neoforge;

import com.telepathicgrunt.the_bumblezone.fluids.neoforge.BzClientFluidExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.Map;

public final class BzFluidSpriteCache {
    private static Map<ResourceLocation, TextureAtlasSprite> textureLookup = Map.of();
    private static TextureAtlasSprite missingSprite = null;

    public static TextureAtlasSprite[] getFluidSprites(BlockAndTintGetter level, BlockPos pos, FluidState fluid) {
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation overlay = props.getOverlayTexture(fluid, level, pos);
        Map<ResourceLocation, TextureAtlasSprite> textures = textureLookup;

        TextureAtlasSprite[] sprites;

        if (props instanceof BzClientFluidExtension bzClientFluidExtension) {
            sprites = new TextureAtlasSprite[6];
            sprites[0] = textures.getOrDefault(props.getStillTexture(fluid, level, pos), missingSprite);
            sprites[1] = textures.getOrDefault(props.getFlowingTexture(fluid, level, pos), missingSprite);
            sprites[2] = overlay == null ? null : textures.getOrDefault(overlay, missingSprite);

            sprites[3] = textures.getOrDefault(bzClientFluidExtension.getDiagonalTexture(), missingSprite);
        }
        else {
            sprites = new TextureAtlasSprite[3];
            sprites[0] = textures.getOrDefault(props.getStillTexture(fluid, level, pos), missingSprite);
            sprites[1] = textures.getOrDefault(props.getFlowingTexture(fluid, level, pos), missingSprite);
            sprites[2] = overlay == null ? null : textures.getOrDefault(overlay, missingSprite);
        }

        return sprites;
    }

    public static void reload() {
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        textureLookup = atlas.getTextures();
        missingSprite = textureLookup.get(MissingTextureAtlasSprite.getLocation());
    }

    private BzFluidSpriteCache() {}
}
