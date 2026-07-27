package net.pvplock.client;

import net.minecraft.world.entity.player.Inventory;

/**
 * 1.21.9 - 1.21.10 build variant.
 *
 * Inventory.getSelectedSlot() exists from 1.21.5 onward, so this variant calls it
 * directly - no reflection needed. Only the 1.21 - 1.21.4 range has to fall back to
 * reading the underlying field, which its own variant handles.
 */
public final class CompatInventory {
	private CompatInventory() {
	}

	public static int getSelectedSlot(Inventory inventory) {
		return inventory.getSelectedSlot();
	}
}
