package net.pvplock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.platform.InputConstants;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

@Mod(PvPLockMod.MODID)
public class PvPLockMod {
	public static final String MODID = "pvplockmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static KeyMapping toggleLockKey;

	public PvPLockMod(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::onClientSetup);
	}

	private void onClientSetup(FMLClientSetupEvent event) {
		InventoryLockState.load();
	}

	public static KeyMapping getToggleLockKey() {
		return toggleLockKey;
	}

	@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
			toggleLockKey = new KeyMapping(
				"key.pvplockmod.toggle_lock",
				InputConstants.Type.KEYSYM,
				InputConstants.KEY_O,
				"category." + MODID + ".general"
			);
			event.register(toggleLockKey);
		}
	}

	@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	public static class ClientTickEvents {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			Minecraft mc = Minecraft.getInstance();
			while (toggleLockKey != null && toggleLockKey.consumeClick()) {
				if (mc.player != null) {
					InventoryLockState.toggleForSlot(mc.player.getInventory().selected);
				}
			}
		}
	}
}
