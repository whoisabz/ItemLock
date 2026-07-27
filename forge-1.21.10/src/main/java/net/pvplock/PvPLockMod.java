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
import net.minecraft.resources.ResourceLocation;

/**
 * Forge 59 - 60 build variant (Minecraft 1.21.9 - 1.21.10).
 *
 * KeyMapping.Category arrives in 1.21.9, but the id class is still called ResourceLocation
 * here - it is renamed to Identifier in 1.21.11. That two-version gap is the only reason
 * this generation is separate from the 1.21.11 build.
 *
 * Every listener is registered explicitly in the constructor rather than through
 * @Mod.EventBusSubscriber. On EventBus 7 that annotation did not pick up
 * RegisterKeyMappingsEvent on Forge 56 - the mod loaded with no errors at all, but the
 * keybind was silently never registered and the mod was inert in game. Explicit
 * registration is what the MDK demonstrates and it makes each event's bus obvious:
 *
 *  - RegisterKeyMappingsEvent and FMLClientSetupEvent are mod-bus events (IModBusEvent),
 *    reached through getBus(modBusGroup).
 *  - TickEvent.ClientTickEvent.Post is a game-bus event, reached through its static BUS.
 *
 * The HUD badge comes from GuiMixin rather than AddGuiOverlayLayersEvent. Forge 59/60 do
 * ship that event, but using the mixin keeps one rendering approach across every variant
 * below 1.21.11 - including 56/57, where the event does not exist at all.
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
		KeyMapping.Category category = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(MODID, "general"));
		toggleLockKey = new KeyMapping(
			"key.pvplockmod.toggle_lock",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_O,
			category
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
