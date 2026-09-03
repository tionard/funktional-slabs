package com.github.tionard.functionalslabs.block;

import com.github.tionard.functionalslabs.mixin.TargetBlockInvoker;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public final class TargetSlabBlock extends SlabBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public TargetSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWER, 0));
    }

    @Override
    protected void onProjectileHit(
            Level level,
            BlockState state,
            BlockHitResult hit,
            Projectile projectile
    ) {
        if (!level.isClientSide()) {
            int power = TargetBlockInvoker.functionalSlabs$updateRedstoneOutput(
                    level,
                    state,
                    hit,
                    projectile
            );
            if (projectile.getOwner() instanceof ServerPlayer player) {
                player.awardStat(Stats.TARGET_HIT);
                CriteriaTriggers.TARGET_BLOCK_HIT.trigger(
                        player,
                        projectile,
                        hit.getLocation(),
                        power
                );
            }
        }
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(POWER);
    }

    @Override
    protected int getSignal(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            Direction direction
    ) {
        return FunctionalSlabGeometry.canPowerThroughFace(state, direction)
                ? state.getValue(POWER)
                : 0;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWER) != 0) {
            level.setBlock(pos, state.setValue(POWER, 0), 18);
        }
    }

    @Override
    protected void onPlace(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState oldState,
            boolean movedByPiston
    ) {
        if (!level.isClientSide()
                && !state.is(oldState.getBlock())
                && state.getValue(POWER) > 0
                && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.setBlock(pos, state.setValue(POWER, 0), 18);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER);
    }
}
