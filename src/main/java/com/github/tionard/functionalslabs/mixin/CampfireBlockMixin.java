package com.github.tionard.functionalslabs.mixin;

import com.github.tionard.functionalslabs.block.FunctionalSlabGeometry;
import com.github.tionard.functionalslabs.registry.FunctionalSlabTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
abstract class CampfireBlockMixin {
    @Inject(method = "isSmokeSource", at = @At("HEAD"), cancellable = true)
    private void functionalSlabs$recognizeTouchingHaySlab(
            BlockState state,
            CallbackInfoReturnable<Boolean> callback
    ) {
        if (state.is(FunctionalSlabTags.SIGNAL_FIRE_BASE_BLOCKS)
                && FunctionalSlabGeometry.touchesFace(state, Direction.UP)) {
            callback.setReturnValue(true);
        }
    }
}
