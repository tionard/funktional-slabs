package com.github.tionard.functionalslabs.block;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class HoneySlabBlock extends SlabBlock {
    private static final VoxelShape BOTTOM_SHAPE = Block.column(14.0, 0.0, 8.0);
    private static final VoxelShape TOP_SHAPE = Block.column(14.0, 8.0, 15.0);
    private static final VoxelShape DOUBLE_SHAPE = Block.column(14.0, 0.0, 15.0);

    public HoneySlabBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return switch (state.getValue(TYPE)) {
            case BOTTOM -> BOTTOM_SHAPE;
            case TOP -> TOP_SHAPE;
            case DOUBLE -> DOUBLE_SHAPE;
        };
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0f, 1.0f);
        if (!level.isClientSide()) {
            level.broadcastEntityEvent(entity, (byte) 54);
        }
        if (entity.causeFallDamage(fallDistance, 0.2f, level.damageSources().fall())) {
            entity.playSound(
                    this.soundType.getFallSound(),
                    this.soundType.getVolume() * 0.5f,
                    this.soundType.getPitch() * 0.75f
            );
        }
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity,
            InsideBlockEffectApplier effectApplier,
            boolean isPrecise
    ) {
        if (isSlidingDown(state, pos, entity)) {
            maybeTriggerSlideAdvancement(entity, pos);
            applySlideMovement(entity);
            maybePlaySlideEffects(level, entity);
        }
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    private static boolean isSlidingDown(BlockState state, BlockPos pos, Entity entity) {
        if (entity.onGround()) {
            return false;
        }
        double top = switch (state.getValue(TYPE)) {
            case BOTTOM -> 0.5;
            case TOP, DOUBLE -> 0.9375;
        };
        if (entity.getY() > pos.getY() + top - 1.0E-7) {
            return false;
        }
        if (oldDeltaY(entity.getDeltaMovement().y) >= -0.08) {
            return false;
        }
        double dx = Math.abs(pos.getX() + 0.5 - entity.getX());
        double dz = Math.abs(pos.getZ() + 0.5 - entity.getZ());
        double overlapDistance = 0.4375 + entity.getBbWidth() / 2.0f;
        return dx + 1.0E-7 > overlapDistance || dz + 1.0E-7 > overlapDistance;
    }

    private static void maybeTriggerSlideAdvancement(Entity entity, BlockPos pos) {
        if (entity instanceof ServerPlayer player && entity.level().getGameTime() % 20L == 0L) {
            CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger(player, entity.level().getBlockState(pos));
        }
    }

    private static void applySlideMovement(Entity entity) {
        Vec3 movement = entity.getDeltaMovement();
        if (oldDeltaY(movement.y) < -0.13) {
            double horizontalScale = -0.05 / oldDeltaY(movement.y);
            entity.setDeltaMovement(new Vec3(
                    movement.x * horizontalScale,
                    newDeltaY(-0.05),
                    movement.z * horizontalScale
            ));
        } else {
            entity.setDeltaMovement(new Vec3(movement.x, newDeltaY(-0.05), movement.z));
        }
        entity.resetFallDistance();
    }

    private static void maybePlaySlideEffects(Level level, Entity entity) {
        if (!(entity instanceof LivingEntity)
                && !(entity instanceof AbstractMinecart)
                && !(entity instanceof PrimedTnt)
                && !(entity instanceof AbstractBoat)) {
            return;
        }
        RandomSource random = level.getRandom();
        if (random.nextInt(5) == 0) {
            entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0f, 1.0f);
        }
        if (!level.isClientSide() && random.nextInt(5) == 0) {
            level.broadcastEntityEvent(entity, (byte) 53);
        }
    }

    private static double oldDeltaY(double deltaY) {
        return deltaY / 0.98f + 0.08;
    }

    private static double newDeltaY(double deltaY) {
        return (deltaY - 0.08) * 0.98f;
    }
}
