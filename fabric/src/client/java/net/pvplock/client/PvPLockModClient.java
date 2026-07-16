package net.pvplock.client;

import com.mojang.blaze3d.platform.InputConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class PvPLockModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pvplockmod");

	private static KeyMapping toggleLockKey;

	@Override
	public void onInitializeClient() {
		InventoryLockState.load();

		KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("pvplockmod", "general"));

		toggleLockKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.pvplockmod.toggle_lock",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_P,
			category
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleLockKey.consumeClick()) {
				if (client.player != null) {
					InventoryLockState.toggleForSlot(client.player.getInventory().getSelectedSlot());
				}
			}
		});

		HudRenderCallback.EVENT.register(LockHudRenderer::render);
	}

	public static KeyMapping getToggleLockKey() {
		return toggleLockKey;
	}
}
