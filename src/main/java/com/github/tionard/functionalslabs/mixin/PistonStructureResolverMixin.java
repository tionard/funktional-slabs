package com.github.tionard.functionalslabs.mixin;

import com.github.tionard.functionalslabs.registry.FunctionalSlabTags;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
abstract class PistonStructureResolverMixin {
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
}
