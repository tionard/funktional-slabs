package com.github.tionard.functionalslabs.mixin;

import com.github.tionard.functionalslabs.block.FunctionalSlabGeometry;
import com.github.tionard.functionalslabs.registry.FunctionalSlabTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
abstract class PistonStructureResolverMixin {
    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private Direction pushDirection;

    @Shadow
    private boolean addBlockLine(BlockPos pos, Direction direction) {
        throw new AssertionError();
    }

    @Inject(method = "isSticky", at = @At("HEAD"), cancellable = true)
    private static void functionalSlabs$recognizeStickySlabs(
            BlockState state,
            CallbackInfoReturnable<Boolean> callback
    ) {
        if (state.is(FunctionalSlabTags.SLIME_BLOCKS) || state.is(FunctionalSlabTags.HONEY_BLOCKS)) {
            callback.setReturnValue(true);
        }
    }

    @Inject(method = "canStickToEachOther", at = @At("HEAD"), cancellable = true)
    private static void functionalSlabs$keepHoneyAndSlimeSeparate(
            BlockState first,
            BlockState second,
            CallbackInfoReturnable<Boolean> callback
    ) {
        boolean firstHoney = first.is(FunctionalSlabTags.HONEY_BLOCKS);
        boolean firstSlime = first.is(FunctionalSlabTags.SLIME_BLOCKS);
        boolean secondHoney = second.is(FunctionalSlabTags.HONEY_BLOCKS);
        boolean secondSlime = second.is(FunctionalSlabTags.SLIME_BLOCKS);
        if (firstHoney && secondSlime || firstSlime && secondHoney) {
            callback.setReturnValue(false);
        }
    }

    @Redirect(
            method = "addBlockLine",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;canStickToEachOther(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    private boolean functionalSlabs$requireContactAlongPushAxis(
            BlockState first,
            BlockState second
    ) {
        return canStickByType(first, second)
                && FunctionalSlabGeometry.touchesAcrossBoundary(
                        first,
                        pushDirection.getOpposite(),
                        second
                );
    }

    @Redirect(
            method = "addBranchingBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;addBlockLine(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean functionalSlabs$onlyAddTouchingBranch(
            PistonStructureResolver resolver,
            BlockPos neighborPos,
            Direction direction,
            BlockPos sourcePos
    ) {
        BlockState source = level.getBlockState(sourcePos);
        BlockState neighbor = level.getBlockState(neighborPos);
        if (!FunctionalSlabGeometry.touchesAcrossBoundary(source, direction, neighbor)) {
            return true;
        }
        return addBlockLine(neighborPos, direction);
    }

    private static boolean canStickByType(BlockState first, BlockState second) {
        boolean firstHoney = first.is(FunctionalSlabTags.HONEY_BLOCKS);
        boolean firstSlime = first.is(FunctionalSlabTags.SLIME_BLOCKS);
        boolean secondHoney = second.is(FunctionalSlabTags.HONEY_BLOCKS);
        boolean secondSlime = second.is(FunctionalSlabTags.SLIME_BLOCKS);
        if (firstHoney && secondSlime || firstSlime && secondHoney) {
            return false;
        }
        return firstHoney || firstSlime || secondHoney || secondSlime;
    }
}
