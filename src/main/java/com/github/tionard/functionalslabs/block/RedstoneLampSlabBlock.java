package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;

public final class RedstoneLampSlabBlock extends SlabBlock {
    public RedstoneLampSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(RedstoneLampBlock.LIT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null
                ? null
                : state.setValue(
                        RedstoneLampBlock.LIT,
                        context.getLevel().hasNeighborSignal(context.getClickedPos())
                );
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            Orientation orientation,
            boolean movedByPiston
    ) {
        if (level.isClientSide()) {
            return;
        }
        boolean lit = state.getValue(RedstoneLampBlock.LIT);
        if (lit == level.hasNeighborSignal(pos)) {
            return;
        }
        if (lit) {
            level.scheduleTick(pos, this, 4);
        } else {
            level.setBlock(pos, state.cycle(RedstoneLampBlock.LIT), 2);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(RedstoneLampBlock.LIT) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(RedstoneLampBlock.LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RedstoneLampBlock.LIT);
    }
}
