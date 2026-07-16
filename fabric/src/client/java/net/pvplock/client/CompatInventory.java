package net.pvplock.client;

import java.lang.reflect.Field;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import net.minecraft.world.entity.player.Inventory;

/**
 * Older Minecraft versions expose the selected hotbar slot as a public field
 * ("selected"); newer versions encapsulate it behind getSelectedSlot(). This calls
 * getSelectedSlot() directly (a compile-time reference, correctly remapped by Fabric
 * Loader in production) and only falls back to reflection on the field if that's
 * missing on the running version.
 *
 * Unlike a constructor (identified purely by parameter types, so plain reflection is
 * safe there - see CompatKeyMapping), a field lookup is identified by name, and field
 * names get remapped between the "named" mappings this code is written against and
 * whatever namespace is actually loaded at runtime (typically intermediary in a real
 * install). A hardcoded "selected" string would hit the same production-vs-dev
 * mismatch that caused the crash CompatKeyMapping was fixed for. Fabric Loader's own
 * MappingResolver translates the named field name to the runtime's actual name before
 * the reflection lookup, so this works correctly in a real published install too.
 */
public final class CompatInventory {
	private static final String INVENTORY_CLASS = "net/minecraft/world/entity/player/Inventory";

	private CompatInventory() {
	}

	public static int getSelectedSlot(Inventory inventory) {
		try {
			return inventory.getSelectedSlot();
		} catch (NoSuchMethodError e) {
			try {
				MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
				String runtimeFieldName = mappings.mapFieldName("named", INVENTORY_CLASS, "selected", "I");
				Field selected = Inventory.class.getField(runtimeFieldName);
				return selected.getInt(inventory);
			} catch (ReflectiveOperationException ex) {
				throw new IllegalStateException("Could not determine the selected hotbar slot on this Minecraft version", ex);
			}
		}
	}
}
