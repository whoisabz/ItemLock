# ItemLock — Fabric, Minecraft 1.21.6 – 1.21.8

This folder is the Fabric/Quilt build for **Minecraft 1.21.6 through 1.21.8**. Other Minecraft versions are built from sibling folders — see the [repository README](../README.md) for the full folder-to-version table and what differs between the variants.

This range is the first to use the stratum rendering system, so the lock badge composites over item icons with a plain `fill()`. Keybind categories are still plain `String`s here — `KeyMapping.Category` arrives in 1.21.9.

## Requirements

- Minecraft 1.21.6, 1.21.7 or 1.21.8
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
