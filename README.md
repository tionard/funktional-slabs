# Functional Slabs

Functional Slabs is a Fabric mod for Minecraft Java 26.2. It adds half-height versions of useful vanilla blocks while preserving the behavior that makes each source block special.

## Included slabs

- Ice, packed ice, and blue ice
- Block of redstone
- Honey block and slime block
- Obsidian
- Redstone lamp
- Soul sand
- Magma block
- Copper bulbs in every oxidation stage, including waxed variants

Slabs use the normal vanilla recipe of three blocks in a horizontal row for six slabs. Every slab also has a stonecutter recipe that turns one source block into two slabs.

## Preserved behavior

- Ice slipperiness, Silk Touch drops, and regular ice melting
- Constant redstone power from faces occupied by redstone slabs
- Honey slowdown, wall sliding, fall cushioning, and contact-aware piston adhesion
- Slime bouncing, movement slowdown, and contact-aware piston adhesion
- Magma damage and downward bubble columns
- Soul sand slowdown, upward bubble columns, Soul Speed, soul fire, and nether wart support
- Redstone lamp and copper bulb power response only through occupied faces
- Copper bulb toggling, comparator output, oxidation, waxing, scraping, and light levels

## Development

The project targets Java 25, Fabric Loader 0.19.3, Fabric API 0.156.0+26.2, and Fabric Loom 1.17.19.

```bash
./gradlew build
```

The release JAR is written to `build/libs/functional-slabs-<version>.jar`.
