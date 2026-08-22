package com.github.tionard.functionalslabs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class EntityMovementMixin {
    @Inject(method = "getBlockPosBelowThatAffectsMyMovement", at = @At("RETURN"), cancellable = true)
    private void functionalSlabs$useBottomIceSlabForMovement(
            CallbackInfoReturnable<BlockPos> callback
    ) {
        Entity entity = (Entity) (Object) this;
        BlockPos supportPos = entity.getOnPos();
        BlockState supportState = entity.level().getBlockState(supportPos);
        if (supportState.is(BlockTags.ICE)
                && supportState.hasProperty(SlabBlock.TYPE)
                && supportState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
            callback.setReturnValue(supportPos);
        }
    }
}
