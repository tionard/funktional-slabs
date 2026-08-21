package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;

public class CopperBulbSlabBlock extends SlabBlock {
    public CopperBulbSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(
                defaultBlockState()
                        .setValue(CopperBulbBlock.LIT, false)
                        .setValue(CopperBulbBlock.POWERED, false)
        );
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel serverLevel) {
            checkAndFlip(state, serverLevel, pos);
        }
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
        if (level instanceof ServerLevel serverLevel) {
            checkAndFlip(state, serverLevel, pos);
        }
    }

    private void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean signal = level.hasNeighborSignal(pos);
        if (signal == state.getValue(CopperBulbBlock.POWERED)) {
            return;
        }

        BlockState newState = state;
        if (!state.getValue(CopperBulbBlock.POWERED)) {
            newState = newState.cycle(CopperBulbBlock.LIT);
            level.playSound(
                    null,
                    pos,
                    newState.getValue(CopperBulbBlock.LIT)
                            ? SoundEvents.COPPER_BULB_TURN_ON
                            : SoundEvents.COPPER_BULB_TURN_OFF,
                    SoundSource.BLOCKS
            );
        }
        level.setBlock(pos, newState.setValue(CopperBulbBlock.POWERED, signal), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CopperBulbBlock.LIT, CopperBulbBlock.POWERED);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return level.getBlockState(pos).getValue(CopperBulbBlock.LIT) ? 15 : 0;
    }
}
