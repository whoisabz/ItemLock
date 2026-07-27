package net.pvplock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * Forge 56 - 58 build variant (Minecraft 1.21.6 - 1.21.8).
 *
 * Every listener is registered explicitly in the constructor rather than through
 * @Mod.EventBusSubscriber. On EventBus 7 the annotation did not pick up
 * RegisterKeyMappingsEvent at all here - the mod loaded with no errors, but the keybind
 * was silently never registered, so the mod appeared completely dead in game. Explicit
 * registration is what the Forge 56 MDK itself demonstrates, and it makes the bus each
 * event belongs to obvious rather than inferred:
 *
 *  - RegisterKeyMappingsEvent and FMLClientSetupEvent are mod-bus events (IModBusEvent),
 *    reached through getBus(modBusGroup).
 *  - TickEvent.ClientTickEvent.Post is a game-bus event, reached through its static BUS.
 *
 * Other differences from the 1.21.11 build:
 *  - Keybind categories are plain translation-key Strings; KeyMapping.Category is 1.21.9+.
 *  - The HUD badge comes from GuiMixin rather than AddGuiOverlayLayersEvent, which Forge
 *    56 and 57 do not ship at all.
 */
@Mod(PvPLockMod.MODID)
public class PvPLockMod {
	public static final String MODID = "pvplockmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static KeyMapping toggleLockKey;

	public PvPLockMod(FMLJavaModLoadingContext context) {
		var modBusGroup = context.getModBusGroup();
		FMLClientSetupEvent.getBus(modBusGroup).addListener(this::onClientSetup);
		RegisterKeyMappingsEvent.getBus(modBusGroup).addListener(PvPLockMod::onRegisterKeyMappings);
		TickEvent.ClientTickEvent.Post.BUS.addListener(PvPLockMod::onClientTick);
	}

	private void onClientSetup(FMLClientSetupEvent event) {
		InventoryLockState.load();
	}

	public static KeyMapping getToggleLockKey() {
		return toggleLockKey;
	}

	private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
		toggleLockKey = new KeyMapping(
			"key.pvplockmod.toggle_lock",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_O,
			"category." + MODID + ".general"
		);
		event.register(toggleLockKey);
		LOGGER.info("Registered ItemLock keybind");
	}

	private static void onClientTick(TickEvent.ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		while (toggleLockKey != null && toggleLockKey.consumeClick()) {
			if (mc.player != null) {
				InventoryLockState.toggleForSlot(mc.player.getInventory().getSelectedSlot());
			}
		}
	}
}
