package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class RedstoneSlabBlock extends SlabBlock {
    public RedstoneSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    @Override
    protected int getSignal(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            Direction direction
    ) {
        return FunctionalSlabGeometry.canPowerThroughFace(state, direction) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            Direction direction
    ) {
        return FunctionalSlabGeometry.canPowerThroughFace(state, direction) ? 15 : 0;
    }
}
