package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;


public class BeehiveBeeswax extends Block {

    public static final MapCodec<BeehiveBeeswax> CODEC = Block.simpleCodec(BeehiveBeeswax::new);

    public BeehiveBeeswax() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(0.6F, 0.3F)
                .sound(SoundType.WOOD));
    }

    public BeehiveBeeswax(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends BeehiveBeeswax> codec() {
        return CODEC;
    }

}
