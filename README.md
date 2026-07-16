# ItemLock

A Minecraft mod that stops you from losing loot to a misclick mid-fight.

Press a key (default **P**) while on a hotbar slot to lock it in place. While a slot is locked:

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
- Minecraft 1.21.11
- NeoForge >= 21.11.0-beta
- Java 21

**Forge**
- Minecraft 1.21.11
- Forge >= 1.21.11-61.0.0
- Java 21

## Repository layout

This repo contains four independent loader-specific implementations of the same mod, since each loader has its own mod-loading and rendering APIs:

- [`fabric/`](fabric) — Fabric (also covers Quilt via its Fabric-compatibility layer)
- [`neoforge/`](neoforge) — NeoForge
- [`forge/`](forge) — Forge

Each subfolder is a self-contained Gradle project. See the loader-specific README (where present) or just run `./gradlew build` from inside that folder.

## Reporting bugs

Found a bug? [Open an issue](../../issues).

## License

CC0-1.0 — do whatever you'd like with it. See [LICENSE](LICENSE).
