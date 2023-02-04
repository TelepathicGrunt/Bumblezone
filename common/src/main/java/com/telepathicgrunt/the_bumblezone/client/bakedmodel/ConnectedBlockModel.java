package com.telepathicgrunt.the_bumblezone.client.bakedmodel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class ConnectedBlockModel {

    private final Predicate<BlockState> connectionPredicate;

    public ConnectedBlockModel(Predicate<BlockState> connectionPredicate) {
        this.connectionPredicate = connectionPredicate;
    }

    public Set<Texture> getSprites(BlockAndTintGetter level, BlockPos pos, Direction direction) {
        Set<Texture> connections = new HashSet<>();
        switch (direction) {
            case UP, DOWN -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    connections.add(Texture.BOTTOM_LEFT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Texture.TOP_LEFT) || !connections.contains(Texture.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.BOTTOM_LEFT);
                }
                if ((!connections.contains(Texture.TOP_RIGHT) || !connections.contains(Texture.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    connections.add(Texture.TOP_RIGHT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }

                for (Texture connection : Texture.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case NORTH -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Texture.BOTTOM_LEFT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Texture.TOP_LEFT) || !connections.contains(Texture.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.BOTTOM_LEFT);
                }
                if ((!connections.contains(Texture.TOP_RIGHT) || !connections.contains(Texture.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    connections.add(Texture.TOP_RIGHT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }

                for (Texture connection : Texture.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case EAST -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Texture.BOTTOM_LEFT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Texture.TOP_LEFT) || !connections.contains(Texture.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.BOTTOM_LEFT);
                }
                if ((!connections.contains(Texture.TOP_RIGHT) || !connections.contains(Texture.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    connections.add(Texture.TOP_RIGHT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }

                for (Texture connection : Texture.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case WEST -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Texture.BOTTOM_LEFT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Texture.TOP_LEFT) || !connections.contains(Texture.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.BOTTOM_LEFT);
                }
                if ((!connections.contains(Texture.TOP_RIGHT) || !connections.contains(Texture.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    connections.add(Texture.TOP_RIGHT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }

                for (Texture connection : Texture.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case SOUTH -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Texture.BOTTOM_LEFT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Texture.TOP_LEFT) || !connections.contains(Texture.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    connections.add(Texture.TOP_LEFT);
                    connections.add(Texture.BOTTOM_LEFT);
                }
                if ((!connections.contains(Texture.TOP_RIGHT) || !connections.contains(Texture.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    connections.add(Texture.TOP_RIGHT);
                    connections.add(Texture.BOTTOM_RIGHT);
                }

                for (Texture connection : Texture.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
        }
        return connections;
    }

    public boolean canConnect(BlockState state) {
        return connectionPredicate.test(state);
    }

    public enum Texture {
        BASE(),
        PARTICLE(),
        FRONT(),
        TOP_LEFT(),
        TOP_RIGHT(),
        BOTTOM_LEFT(),
        BOTTOM_RIGHT();

        public static final Texture[] BASELESS = new Texture[]{TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};

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
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.EAST);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.WEST);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.EAST);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.WEST);
                    default -> pos;
                };
                case SOUTH -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.WEST);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.EAST);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.WEST);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.EAST);
                    default -> pos;
                };
                case WEST -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.NORTH);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.SOUTH);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.NORTH);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.SOUTH);
                    default -> pos;
                };
                case EAST -> switch (this) {
                    case TOP_LEFT -> pos.relative(Direction.UP).relative(Direction.SOUTH);
                    case TOP_RIGHT -> pos.relative(Direction.UP).relative(Direction.NORTH);
                    case BOTTOM_LEFT -> pos.relative(Direction.DOWN).relative(Direction.SOUTH);
                    case BOTTOM_RIGHT -> pos.relative(Direction.DOWN).relative(Direction.NORTH);
                    default -> pos;
                };
            };
        }

        public Texture hFlip() {
            return switch (this) {
                case TOP_LEFT -> TOP_RIGHT;
                case TOP_RIGHT -> TOP_LEFT;
                case BOTTOM_LEFT -> BOTTOM_RIGHT;
                case BOTTOM_RIGHT -> BOTTOM_LEFT;
                default -> this;
            };
        }

        public Texture vFlip() {
            return switch (this) {
                case TOP_LEFT -> BOTTOM_LEFT;
                case TOP_RIGHT -> BOTTOM_RIGHT;
                case BOTTOM_LEFT -> TOP_LEFT;
                case BOTTOM_RIGHT -> TOP_RIGHT;
                default -> this;
            };
        }

        public static Optional<Texture> tryParse(String connection) {
            try {
                return Optional.of(Texture.valueOf(connection.toUpperCase(Locale.ROOT)));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }
}
