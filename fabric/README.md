# ItemLock

A small Fabric mod for Minecraft that stops you from losing loot to a misclick mid-fight.

## What it does

Press **P** while on a hotbar slot to lock it in place. While a slot is locked:

- Pressing **Q** (or Ctrl+Q) does nothing while that slot is selected — you can't accidentally drop the item.
- Clicking, dragging, shift-clicking, or number-key-swapping that item out of its slot is blocked, in any inventory screen.
- Everything else in your inventory stays freely usable — you can still loot chests and reorganize other slots.
- A small lock icon appears in the bottom-right corner of the locked hotbar slot so you can see it's active at a glance, even after you switch away to a different slot.

The lock stays on that slot as you switch hotbar slots around it. Press **P** again while back on the locked slot to unlock it, or switch to a different slot and press **P** to move the lock there instead. An action-bar message confirms every lock/unlock.

This is a client-side mod — it works in singleplayer and on any multiplayer server, no server-side install required.

## Requirements

- Minecraft 1.21.x
- [Fabric Loader](https://fabricmc.net/use/) `>=0.19.3`
- [Fabric API](https://modrinth.com/mod/fabric-api)
- Java 21

## Building from source

```
./gradlew build
```

The output jar is written to `build/libs/`.

## License

CC0-1.0 — do whatever you'd like with it.
