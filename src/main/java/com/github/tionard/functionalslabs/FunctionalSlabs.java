package com.github.tionard.functionalslabs;

import com.github.tionard.functionalslabs.registry.FunctionalSlabBlocks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FunctionalSlabs implements ModInitializer {
    public static final String MOD_ID = "functionalslabs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        FunctionalSlabBlocks.initialize();
        LOGGER.info("Initialized Functional Slabs with {} slab variants", FunctionalSlabBlocks.all().size());
    }
}
