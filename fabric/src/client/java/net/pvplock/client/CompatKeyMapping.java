package net.pvplock.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;

/**
 * Builds a KeyMapping without hard-depending on one Minecraft version's API shape.
 *
 * Mojang changed KeyMapping's constructor at some point in the 1.21.x line: older
 * versions take a plain String category ("category.<modid>.<path>") built from
 * ResourceLocation, newer versions require a KeyMapping.Category object registered
 * via Identifier. This probes which shape is actually present at runtime and uses it,
 * so one jar keeps working across that API change instead of crashing on load.
 */
public final class CompatKeyMapping {
	private CompatKeyMapping() {
	}

	public static KeyMapping create(String translationKey, InputConstants.Type inputType, int keyCode, String modId, String categoryPath) {
		KeyMapping newStyle = tryNewStyle(translationKey, inputType, keyCode, modId, categoryPath);
		if (newStyle != null) {
			return newStyle;
		}

		try {
			Constructor<KeyMapping> ctor = KeyMapping.class.getConstructor(String.class, InputConstants.Type.class, int.class, String.class);
			return ctor.newInstance(translationKey, inputType, keyCode, "category." + modId + "." + categoryPath);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Could not construct KeyMapping via any known constructor shape", e);
		}
	}

	private static KeyMapping tryNewStyle(String translationKey, InputConstants.Type inputType, int keyCode, String modId, String categoryPath) {
		try {
			Class<?> categoryClass = Class.forName("net.minecraft.client.KeyMapping$Category");

			Class<?> idClass;
			try {
				idClass = Class.forName("net.minecraft.resources.Identifier");
			} catch (ClassNotFoundException e) {
				idClass = Class.forName("net.minecraft.resources.ResourceLocation");
			}

			Method fromNamespaceAndPath = idClass.getMethod("fromNamespaceAndPath", String.class, String.class);
			Object id = fromNamespaceAndPath.invoke(null, modId, categoryPath);

			Method register = categoryClass.getMethod("register", idClass);
			Object category = register.invoke(null, id);

			Constructor<KeyMapping> ctor = KeyMapping.class.getConstructor(String.class, InputConstants.Type.class, int.class, categoryClass);
			return ctor.newInstance(translationKey, inputType, keyCode, category);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}
}
