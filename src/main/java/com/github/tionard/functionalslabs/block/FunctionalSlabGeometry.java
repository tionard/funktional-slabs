package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public final class FunctionalSlabGeometry {
    private FunctionalSlabGeometry() {
    }

    public static boolean touchesFace(BlockState state, Direction face) {
        if (!state.hasProperty(SlabBlock.TYPE) || face.getAxis().isHorizontal()) {
            return true;
        }

        SlabType type = state.getValue(SlabBlock.TYPE);
        return type == SlabType.DOUBLE
                || type == SlabType.BOTTOM && face == Direction.DOWN
                || type == SlabType.TOP && face == Direction.UP;
    }

    public static boolean touchesAcrossBoundary(
            BlockState first,
            Direction directionToSecond,
            BlockState second
    ) {
        return touchesFace(first, directionToSecond)
                && touchesFace(second, directionToSecond.getOpposite());
    }

    public static boolean hasTouchingNeighborSignal(BlockState state, Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (touchesFace(state, direction)
                    && level.getSignal(pos.relative(direction), direction) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean canPowerThroughFace(BlockState state, Direction queryDirection) {
        return touchesFace(state, queryDirection.getOpposite());
    }
}
