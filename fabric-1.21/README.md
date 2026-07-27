# ItemLock — Fabric, Minecraft 1.21 – 1.21.5

This folder is the Fabric/Quilt build for **Minecraft 1.21 through 1.21.5**. Other Minecraft versions are built from sibling folders — see the [repository README](../README.md) for the full folder-to-version table and what differs between the variants.

This is the most divergent variant, because it predates two API changes:

- **Rendering.** These versions batch GUI draws per render type and flush them in a fixed order, with the default `RenderType.gui()` batch emitted *before* item icons — so a badge drawn with a plain `fill()` is painted underneath the item and only ever shows on empty slots. This variant uses the `fill(RenderType, ...)` overload with `RenderType.guiOverlay()`, the same layer vanilla uses for durability bars. Strata replace this scheme in 1.21.6.
- **Selected slot.** `Inventory.getSelectedSlot()` only arrives in 1.21.5, so this variant reads the underlying `selected` field, resolving its runtime name through Fabric Loader's `MappingResolver` rather than hardcoding it.

## Requirements

- Minecraft 1.21, 1.21.1, 1.21.2, 1.21.3, 1.21.4 or 1.21.5
- [Fabric Loader](https://fabricmc.net/use/) `>=0.18.4` (or Quilt Loader, via its Fabric-compatibility layer)
- [Fabric API](https://modrinth.com/mod/fabric-api) (Quilted Fabric API on Quilt)
- Java 21

## Building from source

```
./gradlew build
```

The output jar is written to `build/libs/`.

## License

CC0-1.0 — do whatever you'd like with it.
