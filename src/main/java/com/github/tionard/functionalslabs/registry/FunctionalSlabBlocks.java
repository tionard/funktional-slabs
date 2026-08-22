package com.github.tionard.functionalslabs.registry;

import com.github.tionard.functionalslabs.FunctionalSlabs;
import com.github.tionard.functionalslabs.block.CopperBulbSlabBlock;
import com.github.tionard.functionalslabs.block.HoneySlabBlock;
import com.github.tionard.functionalslabs.block.IceSlabBlock;
import com.github.tionard.functionalslabs.block.MagmaSlabBlock;
import com.github.tionard.functionalslabs.block.RedstoneLampSlabBlock;
import com.github.tionard.functionalslabs.block.RedstoneSlabBlock;
import com.github.tionard.functionalslabs.block.SlimeSlabBlock;
import com.github.tionard.functionalslabs.block.SoulSandSlabBlock;
import com.github.tionard.functionalslabs.block.WeatheringCopperBulbSlabBlock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class FunctionalSlabBlocks {
    private static final List<Block> ALL = new ArrayList<>();

    public static final Block ICE_SLAB = register("ice_slab", Blocks.ICE, IceSlabBlock::new);
    public static final Block PACKED_ICE_SLAB = register("packed_ice_slab", Blocks.PACKED_ICE, SlabBlock::new);
    public static final Block BLUE_ICE_SLAB = register("blue_ice_slab", Blocks.BLUE_ICE, SlabBlock::new);
    public static final Block REDSTONE_BLOCK_SLAB = register("redstone_block_slab", Blocks.REDSTONE_BLOCK, RedstoneSlabBlock::new);
    public static final Block HONEY_BLOCK_SLAB = register("honey_block_slab", Blocks.HONEY_BLOCK, HoneySlabBlock::new);
    public static final Block SLIME_BLOCK_SLAB = register("slime_block_slab", Blocks.SLIME_BLOCK, SlimeSlabBlock::new);
    public static final Block OBSIDIAN_SLAB = register("obsidian_slab", Blocks.OBSIDIAN, SlabBlock::new);
    public static final Block REDSTONE_LAMP_SLAB = register("redstone_lamp_slab", Blocks.REDSTONE_LAMP, RedstoneLampSlabBlock::new);
    public static final Block SOUL_SAND_SLAB = register("soul_sand_slab", Blocks.SOUL_SAND, SoulSandSlabBlock::new);
    public static final Block MAGMA_BLOCK_SLAB = register("magma_block_slab", Blocks.MAGMA_BLOCK, MagmaSlabBlock::new);

    public static final Block COPPER_BULB_SLAB = registerWeatheringBulb("copper_bulb_slab", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final Block EXPOSED_COPPER_BULB_SLAB = registerWeatheringBulb("exposed_copper_bulb_slab", WeatheringCopper.WeatherState.EXPOSED);
    public static final Block WEATHERED_COPPER_BULB_SLAB = registerWeatheringBulb("weathered_copper_bulb_slab", WeatheringCopper.WeatherState.WEATHERED);
    public static final Block OXIDIZED_COPPER_BULB_SLAB = registerWeatheringBulb("oxidized_copper_bulb_slab", WeatheringCopper.WeatherState.OXIDIZED);

    public static final Block WAXED_COPPER_BULB_SLAB = registerWaxedBulb("waxed_copper_bulb_slab", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final Block WAXED_EXPOSED_COPPER_BULB_SLAB = registerWaxedBulb("waxed_exposed_copper_bulb_slab", WeatheringCopper.WeatherState.EXPOSED);
    public static final Block WAXED_WEATHERED_COPPER_BULB_SLAB = registerWaxedBulb("waxed_weathered_copper_bulb_slab", WeatheringCopper.WeatherState.WEATHERED);
    public static final Block WAXED_OXIDIZED_COPPER_BULB_SLAB = registerWaxedBulb("waxed_oxidized_copper_bulb_slab", WeatheringCopper.WeatherState.OXIDIZED);

    private FunctionalSlabBlocks() {
    }

    public static void initialize() {
        registerCopperTransitions();
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register(output -> ALL.forEach(output::accept));
    }

    public static Collection<Block> all() {
        return List.copyOf(ALL);
    }

    private static void registerCopperTransitions() {
        OxidizableBlocksRegistry.registerNextStage(COPPER_BULB_SLAB, EXPOSED_COPPER_BULB_SLAB);
        OxidizableBlocksRegistry.registerNextStage(EXPOSED_COPPER_BULB_SLAB, WEATHERED_COPPER_BULB_SLAB);
        OxidizableBlocksRegistry.registerNextStage(WEATHERED_COPPER_BULB_SLAB, OXIDIZED_COPPER_BULB_SLAB);

        OxidizableBlocksRegistry.registerWaxable(COPPER_BULB_SLAB, WAXED_COPPER_BULB_SLAB);
        OxidizableBlocksRegistry.registerWaxable(EXPOSED_COPPER_BULB_SLAB, WAXED_EXPOSED_COPPER_BULB_SLAB);
        OxidizableBlocksRegistry.registerWaxable(WEATHERED_COPPER_BULB_SLAB, WAXED_WEATHERED_COPPER_BULB_SLAB);
        OxidizableBlocksRegistry.registerWaxable(OXIDIZED_COPPER_BULB_SLAB, WAXED_OXIDIZED_COPPER_BULB_SLAB);
    }

    private static Block registerWeatheringBulb(String name, WeatheringCopper.WeatherState weatherState) {
        Block source = Blocks.COPPER_BULB.weathering().pick(weatherState);
        return register(name, source, properties -> new WeatheringCopperBulbSlabBlock(weatherState, properties));
    }

    private static Block registerWaxedBulb(String name, WeatheringCopper.WeatherState weatherState) {
        Block source = Blocks.COPPER_BULB.waxed().pick(weatherState);
        return register(name, source, CopperBulbSlabBlock::new);
    }

    private static Block register(
            String name,
            Block source,
            Function<BlockBehaviour.Properties, ? extends Block> factory
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(FunctionalSlabs.MOD_ID, name)
        );
        Block block = factory.apply(BlockBehaviour.Properties.ofFullCopy(source).setId(blockKey));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, blockKey.identifier());
        BlockItem item = new BlockItem(
                block,
                new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()
        );
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        item.registerBlocks(Item.BY_BLOCK, item);

        ALL.add(block);
        return block;
    }
}
