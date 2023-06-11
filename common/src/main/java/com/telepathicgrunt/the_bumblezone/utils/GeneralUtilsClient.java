package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Doubles;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class GeneralUtilsClient {

    public static boolean isSimilarInColor(Color color1, Color color2, int threshold) {
        return (Math.abs(color1.getRed() - color2.getRed()) +
                Math.abs(color1.getGreen() - color2.getGreen()) +
                Math.abs(color1.getBlue() - color2.getBlue())) < threshold;
    }

    public static boolean isSimilarInVisualColor(Color color1, Color color2, int hueThreshold, int valueThreshold) {
        double[] hue1 = ColorToHsv(color1);
        double[] hue2 = ColorToHsv(color2);

        double hueDiff = hue1[0] - hue2[0];
        if (hueDiff > 180) {
            hueDiff -= 360;
        }
        else if (hueDiff < -180) {
            hueDiff += 360;
        }
        double hueDistance = Math.sqrt(hueDiff * hueDiff);

        double valueDiff = Math.abs(hue1[2] - hue2[2]);

        return hueDistance < hueThreshold && valueDiff < valueThreshold;
    }

    // Source: http://www.java2s.com/example/csharp/system.drawing/calculate-the-difference-in-hue-between-two-s.html
    public static double[] ColorToHsv(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        double h = 0, s, v;
        double min = Math.min(Math.min(r, g), b);
        v = Math.max(Math.max(r, g), b);
        double delta = v - min;

        if (v == 0.0) {
            s = 0;
        }
        else {
            s = delta / v;
        }

        if (s == 0) {
            h = 0.0;
        }
        else {
            if (r == v) {
                h = (g - b) / delta;
            }
            else if (g == v) {
                h = 2 + (b - r) / delta;
            }
            else if (b == v) {
                h = 4 + (r - g) / delta;
            }

            h *= 60;
            if (h < 0.0) {
                h = h + 360;
            }
        }

        var hsv = new double[3];
        hsv[0] = h; // 0 to 360
        hsv[1] = s * 360; // 0 to 360
        hsv[2] = v; // 0 to 360
        return hsv;
    }
}
