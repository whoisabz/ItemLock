# ItemLock

A Minecraft mod that stops you from losing loot to a misclick mid-fight.

Press a key (default **O**) while on a hotbar slot to lock it in place. While a slot is locked:

- Pressing **Q** (or Ctrl+Q) does nothing while that slot is selected — you can't accidentally drop the item.
- Clicking, dragging, shift-clicking, or number-key-swapping that item out of its slot is blocked, in any inventory screen.
- Everything else in your inventory stays freely usable.
- A small lock icon appears over the locked hotbar slot — in the normal HUD, in your inventory, and (faded) on the pause menu.

You can lock multiple slots at once. Locked slots persist across game restarts. This is a client-side mod — no server-side install required.

## Downloads

Available on [Modrinth](https://modrinth.com/mod/item-lock) for Fabric, Quilt, NeoForge, and Forge.

## Requirements

**Fabric / Quilt**
- Minecraft 1.21.x
- Fabric Loader >= 0.18.4 (or Quilt Loader, via its built-in Fabric compatibility layer)
- Fabric API (Quilted Fabric API on Quilt)
- Java 21

**NeoForge**
- Minecraft 1.21.1 (NeoForge >= 21.1.0) or 1.21.11 (NeoForge >= 21.11.0-beta)
- Java 21

**Forge**
- Minecraft 1.21.11
- Forge >= 1.21.11-61.0.0
- Java 21

## Repository layout

Every top-level folder is a self-contained Gradle project — run `./gradlew build` from inside it. There is one project per *loader* and per *Minecraft API generation*, because each loader has its own mod-loading and rendering APIs, and Minecraft itself broke the relevant APIs several times across 1.21.x.

| Folder | Loader | Minecraft |
|---|---|---|
| [`fabric/`](fabric) | Fabric / Quilt | 1.21.11 |
| [`fabric-1.21.9/`](fabric-1.21.9) | Fabric / Quilt | 1.21.9 – 1.21.10 |
| [`fabric-1.21.6/`](fabric-1.21.6) | Fabric / Quilt | 1.21.6 – 1.21.8 |
| [`fabric-1.21/`](fabric-1.21) | Fabric / Quilt | 1.21 – 1.21.5 |
| [`neoforge/`](neoforge) | NeoForge | 1.21.11 |
| [`neoforge-1.21.1/`](neoforge-1.21.1) | NeoForge | 1.21.1 |
| [`forge/`](forge) | Forge | 1.21.11 |

The Fabric projects also cover Quilt, via Quilt's Fabric-compatibility layer. Each row ships as its own jar, declaring only the versions it was built and tested against.

The variants are near-identical; the differences are confined to a few files. What changed where, going backwards from 1.21.11:

- **1.21.11** renamed `ResourceLocation` to `Identifier`.
- **1.21.9** introduced `KeyMapping.Category`; before that, keybind categories were plain `String`s.
- **1.21.6** replaced the render-type batching scheme with strata (`GuiGraphics.nextStratum()`). Below that, an overlay drawn with a plain `fill()` is painted *under* item icons, so `fabric-1.21/` draws through the `RenderType.guiOverlay()` overload instead.
- **1.21.5** added `Inventory.getSelectedSlot()`. `fabric-1.21/` reads the underlying `selected` field instead, resolving its name through Fabric's `MappingResolver` at runtime.

## Reporting bugs

Found a bug? [Open an issue](../../issues).

## License

CC0-1.0 — do whatever you'd like with it. See [LICENSE](LICENSE).
