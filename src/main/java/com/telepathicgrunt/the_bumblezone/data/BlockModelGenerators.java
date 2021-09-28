package com.telepathicgrunt.the_bumblezone.data;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelGenerators extends BlockModelProvider {

    public BlockModelGenerators(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Bumblezone.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {


        // Simple shaped blocks

        cubeAll("beehive_beeswax", modLoc("block/beehive_beeswax"))
                .texture("all", modLoc("block/beehive_beeswax"));

        cubeAll("filled_porous_honeycomb_block", modLoc("block/filled_porous_honeycomb_block"))
                .texture("all", modLoc("block/filled_porous_honeycomb_block"));

        cubeAll("porous_honeycomb_block", modLoc("block/porous_honeycomb_block"))
                .texture("all", modLoc("block/porous_honeycomb_block"));

        cubeAll("sugar_infused_cobblestone", modLoc("block/sugar_infused_cobblestone"))
                .texture("all", modLoc("block/sugar_infused_cobblestone"));

        cubeAll("sugar_infused_stone", modLoc("block/sugar_infused_stone"))
                .texture("all", modLoc("block/sugar_infused_stone"));

        cubeAll("sugar_water_still", modLoc("block/sugar_water_still"))
                .texture("all", modLoc("block/sugar_water_still"));


        // More complex blocks

        withExistingParent("empty_honeycomb_brood_block", mcLoc("block/block"))
                .texture("side", modLoc("block/porous_honeycomb_block"))
                .texture("front", modLoc("block/empty_honeycomb_brood_block"))
                .texture("particle", modLoc("block/porous_honeycomb_block"))
                .element().from(0f, 0f, 0f).to(16f, 16f, 16f)
                .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(0f, 16f, 16f, 0f).texture("#side").cullface(Direction.UP).end()
                .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#front").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.WEST).end()
                .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.EAST).end()
                .end();
        
        withExistingParent("stage_1_honeycomb_brood_block", mcLoc("block/block"))
                .texture("side", modLoc("block/filled_porous_honeycomb_block"))
                .texture("front", modLoc("block/stage_1_honeycomb_brood_block"))
                .texture("particle", modLoc("block/filled_porous_honeycomb_block"))
                .element().from(0f, 0f, 0f).to(16f, 16f, 16f)
                .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(0f, 16f, 16f, 0f).texture("#side").cullface(Direction.UP).end()
                .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#front").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.WEST).end()
                .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.EAST).end()
                .end();
        
        withExistingParent("stage_2_honeycomb_brood_block", mcLoc("block/block"))
                .texture("side", modLoc("block/filled_porous_honeycomb_block"))
                .texture("front", modLoc("block/stage_2_honeycomb_brood_block"))
                .texture("particle", modLoc("block/filled_porous_honeycomb_block"))
                .element().from(0f, 0f, 0f).to(16f, 16f, 16f)
                .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(0f, 16f, 16f, 0f).texture("#side").cullface(Direction.UP).end()
                .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#front").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.WEST).end()
                .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.EAST).end()
                .end();
        
        withExistingParent("stage_3_honeycomb_brood_block", mcLoc("block/block"))
                .texture("side", modLoc("block/filled_porous_honeycomb_block"))
                .texture("front", modLoc("block/stage_3_honeycomb_brood_block"))
                .texture("particle", modLoc("block/filled_porous_honeycomb_block"))
                .element().from(0f, 0f, 0f).to(16f, 16f, 16f)
                .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(0f, 16f, 16f, 0f).texture("#side").cullface(Direction.UP).end()
                .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#front").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.WEST).end()
                .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.EAST).end()
                .end();
        
        withExistingParent("stage_4_honeycomb_brood_block", mcLoc("block/block"))
                .texture("side", modLoc("block/filled_porous_honeycomb_block"))
                .texture("front", modLoc("block/stage_4_honeycomb_brood_block"))
                .texture("particle", modLoc("block/filled_porous_honeycomb_block"))
                .element().from(0f, 0f, 0f).to(16f, 16f, 16f)
                .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(0f, 16f, 16f, 0f).texture("#side").cullface(Direction.UP).end()
                .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#front").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.WEST).end()
                .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.EAST).end()
                .end();

        getBuilder("sticky_honey_residue").ao(false).texture("residue", modLoc("block/sticky_honey_residue"))
                .texture("particle", modLoc("block/sticky_honey_residue"))
                .element().from(0f, 0.2f, 0f).to(16f, 0.2f, 16f)
                .face(Direction.DOWN).texture("#residue").end()
                .face(Direction.UP).texture("#residue").end()
                .face(Direction.NORTH).texture("#residue").end()
                .face(Direction.WEST).texture("#residue").end()
                .face(Direction.EAST).texture("#residue").end()
                .face(Direction.SOUTH).texture("#residue").end()
                .end();

        getBuilder("sticky_honey_redstone_lit").ao(false)
                .texture("residue", modLoc("block/sticky_honey_redstone_lit"))
                .texture("particle", modLoc("block/sticky_honey_redstone_lit"))
                .element().from(0f, 0.2f, 0f).to(16f, 0.2f, 16f)
                .face(Direction.DOWN).texture("#residue").end()
                .face(Direction.UP).texture("#residue").end()
                .face(Direction.NORTH).texture("#residue").end()
                .face(Direction.WEST).texture("#residue").end()
                .face(Direction.EAST).texture("#residue").end()
                .face(Direction.SOUTH).texture("#residue").end()
                .end();
        
        getBuilder("sticky_honey_redstone_unlit").ao(false)
                .texture("residue", modLoc("block/sticky_honey_redstone_unlit"))
                .texture("particle", modLoc("block/sticky_honey_redstone_unlit"))
                .element().from(0f, 0.2f, 0f).to(16f, 0.2f, 16f)
                .face(Direction.DOWN).texture("#residue").end()
                .face(Direction.UP).texture("#residue").end()
                .face(Direction.NORTH).texture("#residue").end()
                .face(Direction.WEST).texture("#residue").end()
                .face(Direction.EAST).texture("#residue").end()
                .face(Direction.SOUTH).texture("#residue").end()
                .end();
    }

}
