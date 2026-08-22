package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public abstract class BubbleColumnSlabBlock extends SlabBlock {
    private final boolean dragDown;

    protected BubbleColumnSlabBlock(BlockBehaviour.Properties properties, boolean dragDown) {
        super(properties);
        this.dragDown = dragDown;
    }

    public static boolean supportsBubbleColumnAbove(BlockState state) {
        SlabType type = state.getValue(TYPE);
        return type == SlabType.TOP
                || type == SlabType.DOUBLE
                || type == SlabType.BOTTOM && state.getValue(WATERLOGGED);
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess tickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        BlockState updated = super.updateShape(
                state,
                level,
                tickAccess,
                pos,
                direction,
                neighborPos,
                neighborState,
                random
        );
        tickAccess.scheduleTick(pos, this, 1);
        return updated;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BubbleColumnBlock.updateColumn(
                Blocks.BUBBLE_COLUMN,
                level,
                pos.above(),
                state
        );
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity,
            InsideBlockEffectApplier effects,
            boolean isInside
    ) {
        if (isInside && isActiveWaterloggedBottom(state) && intersectsWaterHalf(pos, entity)) {
            BlockState above = level.getBlockState(pos.above());
            boolean openSurface = above.getCollisionShape(level, pos.above()).isEmpty()
                    && above.getFluidState().isEmpty();
            if (openSurface) {
                entity.onAboveBubbleColumn(dragDown, pos);
            } else {
                entity.onInsideBubbleColumn(dragDown);
            }
        }
        super.entityInside(state, level, pos, entity, effects, isInside);
    }

    private static boolean isActiveWaterloggedBottom(BlockState state) {
        return state.getValue(TYPE) == SlabType.BOTTOM && state.getValue(WATERLOGGED);
    }

    private static boolean intersectsWaterHalf(BlockPos pos, Entity entity) {
        double waterBottom = pos.getY() + 0.5;
        double waterTop = pos.getY() + 1.0;
        return entity.getBoundingBox().maxY > waterBottom
                && entity.getBoundingBox().minY < waterTop;
    }
}
