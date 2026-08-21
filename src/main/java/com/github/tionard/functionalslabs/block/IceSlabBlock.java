package com.github.tionard.functionalslabs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class IceSlabBlock extends SlabBlock {
    public IceSlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void playerDestroy(
            Level level,
            Player player,
            BlockPos pos,
            BlockState state,
            BlockEntity blockEntity,
            ItemStack destroyedWith
    ) {
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
        if (EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_ICE_MELTING)) {
            return;
        }
        if (waterEvaporates(level, pos)) {
            level.removeBlock(pos, false);
            return;
        }
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.blocksMotion() || belowState.liquid()) {
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightDampening()) {
            melt(level, pos);
        }
    }

    private static void melt(Level level, BlockPos pos) {
        if (waterEvaporates(level, pos)) {
            level.removeBlock(pos, false);
            return;
        }
        BlockState water = Blocks.WATER.defaultBlockState();
        level.setBlockAndUpdate(pos, water);
        level.neighborChanged(pos, water.getBlock(), null);
    }

    private static boolean waterEvaporates(Level level, BlockPos pos) {
        return level.environmentAttributes()
                .getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)
                .booleanValue();
    }
}
