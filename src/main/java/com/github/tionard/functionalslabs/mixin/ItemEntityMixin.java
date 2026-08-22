package com.github.tionard.functionalslabs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin {
    @ModifyArg(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ),
            index = 0
    )
    private BlockPos functionalSlabs$useBottomIceSlabForFriction(BlockPos originalPos) {
        ItemEntity item = (ItemEntity) (Object) this;
        BlockPos slabPos = originalPos.above();
        BlockState slabState = item.level().getBlockState(slabPos);
        if (slabState.is(BlockTags.ICE)
                && slabState.hasProperty(SlabBlock.TYPE)
                && slabState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
            return slabPos;
        }
        return originalPos;
    }
}
