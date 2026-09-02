package com.github.tionard.functionalslabs.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.TargetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TargetBlock.class)
public interface TargetBlockInvoker {
    @Invoker("updateRedstoneOutput")
    static int functionalSlabs$updateRedstoneOutput(
            LevelAccessor level,
            BlockState state,
            BlockHitResult hit,
            Entity entity
    ) {
        throw new AssertionError();
    }
}
