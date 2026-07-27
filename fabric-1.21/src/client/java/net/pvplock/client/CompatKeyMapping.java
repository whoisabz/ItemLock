package net.pvplock.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;

/**
 * 1.21 - 1.21.5 build variant.
 *
 * On these versions KeyMapping takes a plain String category translation key.
 * KeyMapping.Category (1.21.9+) and the ResourceLocation -> Identifier rename (1.21.11)
 * are handled by their own source variants, so this needs no runtime probing - just a
 * direct compile-time call against the API this range actually has.
 */
public final class CompatKeyMapping {
	private CompatKeyMapping() {
	}

	public static KeyMapping create(String translationKey, InputConstants.Type inputType, int keyCode, String modId, String categoryPath) {
		return new KeyMapping(translationKey, inputType, keyCode, "category." + modId + "." + categoryPath);
	}
}
