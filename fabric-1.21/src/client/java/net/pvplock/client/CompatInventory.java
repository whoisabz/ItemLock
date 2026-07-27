package net.pvplock.client;

import java.lang.reflect.Field;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import net.minecraft.world.entity.player.Inventory;

/**
 * 1.21 - 1.21.5 build variant.
 *
 * Inventory.getSelectedSlot() only exists from 1.21.5 onward, so this range has to read
 * the underlying "selected" field instead - it is present across the whole range. The
 * field's visibility is not guaranteed to stay public once the getter was introduced,
 * so this goes through reflection with setAccessible rather than a direct field access
 * that could throw IllegalAccessError on the later versions in the range.
 *
 * The field name is translated through Fabric Loader's MappingResolver rather than being
 * hardcoded: field lookups are by name, and names differ between the "named" mappings
 * this is written against and the namespace actually loaded at runtime. A hardcoded
 * string works in a dev environment and silently fails in a real install - the exact
 * production-only crash this mod already shipped once.
 */
public final class CompatInventory {
	private static final String INVENTORY_CLASS = "net/minecraft/world/entity/player/Inventory";

	private static Field selectedField;

	private CompatInventory() {
	}

	public static int getSelectedSlot(Inventory inventory) {
		try {
			return resolveSelectedField().getInt(inventory);
		} catch (ReflectiveOperationException | RuntimeException e) {
			throw new IllegalStateException("Could not determine the selected hotbar slot on this Minecraft version", e);
		}
	}

	private static Field resolveSelectedField() throws ReflectiveOperationException {
		Field cached = selectedField;
		if (cached != null) {
			return cached;
		}

		MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
		String runtimeName = mappings.mapFieldName("named", INVENTORY_CLASS, "selected", "I");

		Field field;
		try {
			field = Inventory.class.getDeclaredField(runtimeName);
		} catch (NoSuchFieldException e) {
			// Fall back to the named spelling in case the runtime namespace is already "named".
			field = Inventory.class.getDeclaredField("selected");
		}
		field.setAccessible(true);
		selectedField = field;
		return field;
	}
}
