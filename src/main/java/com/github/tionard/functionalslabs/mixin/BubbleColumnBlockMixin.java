package com.github.tionard.functionalslabs.mixin;

import com.github.tionard.functionalslabs.block.BubbleColumnSlabBlock;
import com.github.tionard.functionalslabs.registry.FunctionalSlabTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BubbleColumnBlock.class)
abstract class BubbleColumnBlockMixin {
    @Inject(method = "getColumnState", at = @At("HEAD"), cancellable = true)
    private static void functionalSlabs$requireWaterTouchingSource(
            Block bubbleColumn,
            BlockState sourceState,
            BlockState currentState,
            CallbackInfoReturnable<BlockState> callback
    ) {
        if (sourceState.is(FunctionalSlabTags.BUBBLE_COLUMN_SOURCES)
                && !BubbleColumnSlabBlock.supportsBubbleColumnAbove(sourceState)) {
            callback.setReturnValue(
                    currentState.is(bubbleColumn)
                            ? Blocks.WATER.defaultBlockState()
                            : currentState
            );
        }
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void functionalSlabs$validateSlabSource(
            BlockState state,
            LevelReader level,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> callback
    ) {
        BlockState sourceState = level.getBlockState(pos.below());
        if (sourceState.is(FunctionalSlabTags.BUBBLE_COLUMN_SOURCES)) {
            callback.setReturnValue(BubbleColumnSlabBlock.supportsBubbleColumnAbove(sourceState));
        }
    }
}
