package com.github.tionard.functionalslabs.registry;

import com.github.tionard.functionalslabs.FunctionalSlabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class FunctionalSlabTags {
    public static final TagKey<Block> SLIME_BLOCKS = blockTag("slime_blocks");
    public static final TagKey<Block> HONEY_BLOCKS = blockTag("honey_blocks");
    public static final TagKey<Block> SIGNAL_FIRE_BASE_BLOCKS = blockTag("signal_fire_base_blocks");
    public static final TagKey<Block> BUBBLE_COLUMN_SOURCES = blockTag("bubble_column_sources");

    private FunctionalSlabTags() {
    }

    private static TagKey<Block> blockTag(String path) {
        return TagKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(FunctionalSlabs.MOD_ID, path)
        );
    }
}
