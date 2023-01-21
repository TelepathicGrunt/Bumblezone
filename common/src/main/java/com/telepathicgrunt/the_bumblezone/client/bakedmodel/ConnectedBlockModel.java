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

    public Set<Connection> getSprites(BlockAndTintGetter level, BlockPos pos, Direction direction) {
        Set<Connection> connections = new HashSet<>();
        switch (direction) {
            case UP, DOWN -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    connections.add(Connection.BOTTOM_LEFT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Connection.TOP_LEFT) || !connections.contains(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.BOTTOM_LEFT);
                }
                if ((!connections.contains(Connection.TOP_RIGHT) || !connections.contains(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    connections.add(Connection.TOP_RIGHT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case NORTH -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Connection.BOTTOM_LEFT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Connection.TOP_LEFT) || !connections.contains(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.BOTTOM_LEFT);
                }
                if ((!connections.contains(Connection.TOP_RIGHT) || !connections.contains(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    connections.add(Connection.TOP_RIGHT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case EAST -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Connection.BOTTOM_LEFT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Connection.TOP_LEFT) || !connections.contains(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.BOTTOM_LEFT);
                }
                if ((!connections.contains(Connection.TOP_RIGHT) || !connections.contains(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    connections.add(Connection.TOP_RIGHT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case WEST -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Connection.BOTTOM_LEFT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Connection.TOP_LEFT) || !connections.contains(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.NORTH)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.BOTTOM_LEFT);
                }
                if ((!connections.contains(Connection.TOP_RIGHT) || !connections.contains(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.SOUTH)))) {
                    connections.add(Connection.TOP_RIGHT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }

                for (Connection connection : Connection.BASELESS) {
                    if (!connections.contains(connection) && canConnect(level.getBlockState(connection.offset(pos, direction)))) {
                        connections.add(connection);
                    }
                }
            }
            case SOUTH -> {
                if (canConnect(level.getBlockState(pos.relative(Direction.UP)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.TOP_RIGHT);
                }
                if (canConnect(level.getBlockState(pos.relative(Direction.DOWN)))) {
                    connections.add(Connection.BOTTOM_LEFT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }
                if ((!connections.contains(Connection.TOP_LEFT) || !connections.contains(Connection.BOTTOM_LEFT)) && canConnect(level.getBlockState(pos.relative(Direction.WEST)))) {
                    connections.add(Connection.TOP_LEFT);
                    connections.add(Connection.BOTTOM_LEFT);
                }
                if ((!connections.contains(Connection.TOP_RIGHT) || !connections.contains(Connection.BOTTOM_RIGHT)) && canConnect(level.getBlockState(pos.relative(Direction.EAST)))) {
                    connections.add(Connection.TOP_RIGHT);
                    connections.add(Connection.BOTTOM_RIGHT);
                }

                for (Connection connection : Connection.BASELESS) {
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

    public enum Connection {
        BASE(),
        FRONT(),
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

        public Connection hFlip() {
            return switch (this) {
                case TOP_LEFT -> TOP_RIGHT;
                case TOP_RIGHT -> TOP_LEFT;
                case BOTTOM_LEFT -> BOTTOM_RIGHT;
                case BOTTOM_RIGHT -> BOTTOM_LEFT;
                default -> this;
            };
        }

        public Connection vFlip() {
            return switch (this) {
                case TOP_LEFT -> BOTTOM_LEFT;
                case TOP_RIGHT -> BOTTOM_RIGHT;
                case BOTTOM_LEFT -> TOP_LEFT;
                case BOTTOM_RIGHT -> TOP_RIGHT;
                default -> this;
            };
        }

        public static Optional<Connection> tryParse(String connection) {
            try {
                return Optional.of(Connection.valueOf(connection.toUpperCase(Locale.ROOT)));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }
}
