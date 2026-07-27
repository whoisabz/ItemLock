# ItemLock — Fabric, Minecraft 1.21.9 – 1.21.10

This folder is the Fabric/Quilt build for **Minecraft 1.21.9 and 1.21.10**. Other Minecraft versions are built from sibling folders — see the [repository README](../README.md) for the full folder-to-version table and what differs between the variants.

This range introduced `KeyMapping.Category` but still calls the id class `ResourceLocation`; the rename to `Identifier` lands in 1.21.11, which is why these two versions need their own source.

## Requirements

- Minecraft 1.21.9 or 1.21.10
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
