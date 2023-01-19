package com.telepathicgrunt.the_bumblezone.client.bakedmodel;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

public abstract class ConnectedBlockModel {

    private final EnumMap<Connection, TextureAtlasSprite> sprites;

    public ConnectedBlockModel(TextureAtlasSprite base, TextureAtlasSprite topLeft, TextureAtlasSprite topRight, TextureAtlasSprite bottomLeft, TextureAtlasSprite bottomRight) {
        this.sprites = new EnumMap<>(Connection.class);
        this.sprites.put(Connection.BASE, base);
        this.sprites.put(Connection.TOP_LEFT, topLeft);
        this.sprites.put(Connection.TOP_RIGHT, topRight);
        this.sprites.put(Connection.BOTTOM_LEFT, bottomLeft);
        this.sprites.put(Connection.BOTTOM_RIGHT, bottomRight);
    }

    public TextureAtlasSprite getBase() {
        return this.sprites.get(Connection.BASE);
    }

    public Collection<TextureAtlasSprite> getSprites(BlockAndTintGetter level, BlockPos pos, Direction direction) {
        EnumMap<Connection, TextureAtlasSprite> sprites = new EnumMap<>(Connection.class);
        switch (direction) {
            case UP, DOWN -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }
                if ((!sprites.containsKey(Connection.TOP_LEFT) || !sprites.containsKey(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                }
                if ((!sprites.containsKey(Connection.TOP_RIGHT) || !sprites.containsKey(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!sprites.containsKey(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        sprites.put(connection, this.sprites.get(connection));
                    }
                }
            }
            case NORTH -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }
                if ((!sprites.containsKey(Connection.TOP_LEFT) || !sprites.containsKey(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                }
                if ((!sprites.containsKey(Connection.TOP_RIGHT) || !sprites.containsKey(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!sprites.containsKey(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        sprites.put(connection, this.sprites.get(connection));
                    }
                }
            }
            case EAST -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }
                if ((!sprites.containsKey(Connection.TOP_LEFT) || !sprites.containsKey(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                }
                if ((!sprites.containsKey(Connection.TOP_RIGHT) || !sprites.containsKey(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!sprites.containsKey(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        sprites.put(connection, this.sprites.get(connection));
                    }
                }
            }
            case WEST -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }
                if ((!sprites.containsKey(Connection.TOP_LEFT) || !sprites.containsKey(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                }
                if ((!sprites.containsKey(Connection.TOP_RIGHT) || !sprites.containsKey(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!sprites.containsKey(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        sprites.put(connection, this.sprites.get(connection));
                    }
                }
            }
            case SOUTH -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }
                if ((!sprites.containsKey(Connection.TOP_LEFT) || !sprites.containsKey(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    sprites.put(Connection.TOP_LEFT, this.sprites.get(Connection.TOP_LEFT));
                    sprites.put(Connection.BOTTOM_LEFT, this.sprites.get(Connection.BOTTOM_LEFT));
                }
                if ((!sprites.containsKey(Connection.TOP_RIGHT) || !sprites.containsKey(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    sprites.put(Connection.TOP_RIGHT, this.sprites.get(Connection.TOP_RIGHT));
                    sprites.put(Connection.BOTTOM_RIGHT, this.sprites.get(Connection.BOTTOM_RIGHT));
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!sprites.containsKey(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        sprites.put(connection, this.sprites.get(connection));
                    }
                }
            }
        }
        return sprites.values();
    }

    abstract boolean canConnect(BlockState state);

    public enum Connection {
        BASE(),
        TOP_LEFT(),
        TOP_RIGHT(),
        BOTTOM_LEFT(),
        BOTTOM_RIGHT();

        public static final Connection[] BASELESS = new Connection[]{TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};

        public BlockPos offset(BlockPos pos, Direction direction) {
            return switch (direction) {
                case UP, DOWN -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.NORTH).relative(Direction.WEST);
                    case TOP_RIGHT -> pos.relative(Direction.NORTH).relative(Direction.EAST);
                    case BOTTOM_LEFT -> pos.relative(Direction.SOUTH).relative(Direction.WEST);
                    case BOTTOM_RIGHT -> pos.relative(Direction.SOUTH).relative(Direction.EAST);
                    default -> pos;
                };
                case NORTH -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.WEST);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.EAST);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.WEST);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.EAST);
                    default -> pos;
                };
                case SOUTH -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.EAST);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.WEST);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.EAST);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.WEST);
                    default -> pos;
                };
                case WEST -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.SOUTH);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.NORTH);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.SOUTH);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.NORTH);
                    default -> pos;
                };
                case EAST -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.NORTH);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.SOUTH);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.NORTH);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.SOUTH);
                    default -> pos;
                };
            };
        }
    }
}
