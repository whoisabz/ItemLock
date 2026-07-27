package net.pvplock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * Forge 52 - 54 build variant (Minecraft 1.21.1 - 1.21.4).
 *
 * Differences from the 1.21.11 build:
 *  - Legacy EventBus. SubscribeEvent lives in net.minecraftforge.eventbus.api here; it moves
 *    to ...eventbus.api.listener in Forge 56 (1.21.6), and the mod bus is obtained as an
 *    IEventBus rather than as a bus group.
 *  - Keybind categories are plain translation-key Strings. KeyMapping.Category only arrives
 *    in Minecraft 1.21.9.
 *  - Inventory.getSelectedSlot() does not exist until 1.21.5, so the selected slot is read
 *    from the public "selected" field. Forge runs on official Mojang names, so this is a
 *    direct field access - no MappingResolver dance is needed here, that being a Fabric-only
 *    concern.
 *  - The HUD badge is drawn from GuiMixin, not a Forge event: Forge 51, 56 and 57 ship no
 *    AddGuiOverlayLayersEvent at all, so a mixin on Gui.render is the one approach that
 *    works across the whole 1.21.x range.
 */
@Mod(PvPLockMod.MODID)
public class PvPLockMod {
	public static final String MODID = "pvplockmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static KeyMapping toggleLockKey;

	public PvPLockMod(FMLJavaModLoadingContext context) {
		IEventBus modEventBus = context.getModEventBus();
		modEventBus.addListener(this::onClientSetup);
	}

	private void onClientSetup(FMLClientSetupEvent event) {
		InventoryLockState.load();
	}

	public static KeyMapping getToggleLockKey() {
		return toggleLockKey;
	}

	@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ClientTickEvents {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
			Minecraft mc = Minecraft.getInstance();
			while (toggleLockKey != null && toggleLockKey.consumeClick()) {
				if (mc.player != null) {
					InventoryLockState.toggleForSlot(mc.player.getInventory().getSelectedSlot());
				}
			}
		}
	}
}
