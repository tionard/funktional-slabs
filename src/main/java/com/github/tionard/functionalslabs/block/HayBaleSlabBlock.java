package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class HayBaleSlabBlock extends SlabBlock {
    private static final float FALL_DAMAGE_MULTIPLIER = 0.2f;

    public HayBaleSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        entity.causeFallDamage(fallDistance, FALL_DAMAGE_MULTIPLIER, level.damageSources().fall());
    }
}
