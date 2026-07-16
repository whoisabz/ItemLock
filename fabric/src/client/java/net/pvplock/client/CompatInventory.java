package net.pvplock.client;

import java.lang.reflect.Field;

import net.minecraft.world.entity.player.Inventory;

/**
 * Older Minecraft versions expose the selected hotbar slot as a public field
 * ("selected"); newer versions encapsulate it behind getSelectedSlot(). This calls
 * getSelectedSlot() directly (a compile-time reference, correctly remapped by Fabric
 * Loader in production) and only falls back to reflection on the field if that's
 * missing on the running version - see CompatKeyMapping for why a string-based
 * Class.forName()/getMethod() lookup as the primary path breaks in production.
 */
public final class CompatInventory {
	private CompatInventory() {
	}

	public static int getSelectedSlot(Inventory inventory) {
		try {
			return inventory.getSelectedSlot();
		} catch (NoSuchMethodError e) {
			try {
				Field selected = Inventory.class.getField("selected");
				return selected.getInt(inventory);
			} catch (ReflectiveOperationException ex) {
				throw new IllegalStateException("Could not determine the selected hotbar slot on this Minecraft version", ex);
			}
		}
	}
}
