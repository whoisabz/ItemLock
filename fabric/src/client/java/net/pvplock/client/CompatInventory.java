package net.pvplock.client;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.world.entity.player.Inventory;

/**
 * Older Minecraft versions expose the selected hotbar slot as a public field
 * ("selected"); newer versions encapsulate it behind getSelectedSlot(). Probing at
 * runtime instead of hard-depending on one shape keeps one jar working across both.
 */
public final class CompatInventory {
	private CompatInventory() {
	}

	public static int getSelectedSlot(Inventory inventory) {
		try {
			Method getSelectedSlot = Inventory.class.getMethod("getSelectedSlot");
			return (int) getSelectedSlot.invoke(inventory);
		} catch (ReflectiveOperationException e) {
			try {
				Field selected = Inventory.class.getField("selected");
				return selected.getInt(inventory);
			} catch (ReflectiveOperationException ex) {
				throw new IllegalStateException("Could not determine the selected hotbar slot on this Minecraft version", ex);
			}
		}
	}
}
