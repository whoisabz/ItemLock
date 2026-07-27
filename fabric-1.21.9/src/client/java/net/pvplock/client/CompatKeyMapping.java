package net.pvplock.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

/**
 * 1.21.9 - 1.21.10 build variant.
 *
 * 1.21.9 introduced KeyMapping.Category but still calls the id class ResourceLocation;
 * it is renamed to Identifier in 1.21.11. That two-version gap between the two changes
 * is why this range needs its own variant rather than sharing the 1.21.6 or 1.21.11 source.
 */
public final class CompatKeyMapping {
	private CompatKeyMapping() {
	}

	public static KeyMapping create(String translationKey, InputConstants.Type inputType, int keyCode, String modId, String categoryPath) {
		KeyMapping.Category category = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(modId, categoryPath));
		return new KeyMapping(translationKey, inputType, keyCode, category);
	}
}
