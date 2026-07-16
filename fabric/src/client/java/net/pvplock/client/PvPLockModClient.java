package net.pvplock.client;

import com.mojang.blaze3d.platform.InputConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.KeyMapping;

public class PvPLockModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pvplockmod");

	private static KeyMapping toggleLockKey;

	@Override
	public void onInitializeClient() {
		InventoryLockState.load();

		toggleLockKey = KeyBindingHelper.registerKeyBinding(CompatKeyMapping.create(
			"key.pvplockmod.toggle_lock",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_P,
			"pvplockmod",
			"general"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleLockKey.consumeClick()) {
				if (client.player != null) {
					try {
						InventoryLockState.toggleForSlot(CompatInventory.getSelectedSlot(client.player.getInventory()));
					} catch (IllegalStateException e) {
						LOGGER.error("Could not read the selected hotbar slot on this Minecraft version - the toggle key won't work", e);
						break;
					}
				}
			}
		});

		HudRenderCallback.EVENT.register(LockHudRenderer::render);
	}

	public static KeyMapping getToggleLockKey() {
		return toggleLockKey;
	}
}
