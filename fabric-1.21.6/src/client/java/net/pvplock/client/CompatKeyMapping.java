package net.pvplock.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;

/**
 * 1.21.6 - 1.21.8 build variant.
 *
 * This range already uses the stratum rendering system, so the badge composites on top
 * with a plain fill() - no guiOverlay render type as 1.21 - 1.21.5 requires. But
 * KeyMapping still takes a plain String category here; KeyMapping.Category only arrives
 * in 1.21.9.
 */
public final class CompatKeyMapping {
	private CompatKeyMapping() {
	}

	public static KeyMapping create(String translationKey, InputConstants.Type inputType, int keyCode, String modId, String categoryPath) {
		return new KeyMapping(translationKey, inputType, keyCode, "category." + modId + "." + categoryPath);
	}
}
