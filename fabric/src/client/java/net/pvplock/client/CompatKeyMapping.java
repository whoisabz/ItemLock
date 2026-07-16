package net.pvplock.client;

import java.lang.reflect.Constructor;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

/**
 * Builds a KeyMapping without hard-depending on one Minecraft version's API shape.
 *
 * Mojang changed KeyMapping's constructor at some point in the 1.21.x line: older
 * versions take a plain String category, newer versions require a KeyMapping.Category
 * object registered via Identifier. This is compiled against the newer shape (a direct
 * reference, which Fabric Loader's remapping handles correctly in production), and only
 * falls back to reflection - triggered by the LinkageError the JVM throws when it can't
 * resolve that constructor against whatever's actually running - for older versions.
 *
 * Note: this deliberately does NOT use Class.forName() with a class name string for the
 * primary path. Fabric Loader remaps a mod's own compiled bytecode references (like the
 * direct call below) to match the running game's actual names, but it has no way to
 * remap an arbitrary string handed to reflection at runtime - a Class.forName() lookup
 * by name works in a dev environment (where names already match) but silently fails in
 * a real published build, which is what caused this to crash on load in the first place.
 */
public final class CompatKeyMapping {
	private CompatKeyMapping() {
	}

	public static KeyMapping create(String translationKey, InputConstants.Type inputType, int keyCode, String modId, String categoryPath) {
		try {
			KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(modId, categoryPath));
			return new KeyMapping(translationKey, inputType, keyCode, category);
		} catch (NoSuchMethodError | NoClassDefFoundError e) {
			try {
				Constructor<KeyMapping> ctor = KeyMapping.class.getConstructor(String.class, InputConstants.Type.class, int.class, String.class);
				return ctor.newInstance(translationKey, inputType, keyCode, "category." + modId + "." + categoryPath);
			} catch (ReflectiveOperationException ex) {
				throw new IllegalStateException("Could not construct KeyMapping via any known constructor shape", ex);
			}
		}
	}
}
