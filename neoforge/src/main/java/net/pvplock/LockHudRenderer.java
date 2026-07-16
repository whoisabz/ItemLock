package net.pvplock;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(modid = PvPLockMod.MODID, value = Dist.CLIENT)
public final class LockHudRenderer {
	private static final int SHACKLE_COLOR = 0xFFC6CAD2;
	private static final int OUTLINE_COLOR = 0xFF000000;
	private static final int BODY_COLOR = 0xFFCE4034;

	// Faded variant used on the pause menu, to visually match the dimmed/blurred backdrop.
	private static final int SHACKLE_COLOR_FADED = 0x80C6CAD2;
	private static final int OUTLINE_COLOR_FADED = 0x80000000;
	private static final int BODY_COLOR_FADED = 0x80CE4034;

	private LockHudRenderer() {
	}

	/**
	 * The background hotbar HUD keeps rendering behind most screens (inventory, pause,
	 * chat), so this fires there too - except Settings/Options screens, which should
	 * stay clean.
	 */
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		Screen screen = mc.screen;
		GuiGraphics graphics = event.getGuiGraphics();

		if (player == null || mc.options.hideGui || !InventoryLockState.isLocked()) {
			return;
		}
		if (screen instanceof OptionsScreen || screen instanceof OptionsSubScreen) {
			return;
		}

		boolean faded = screen instanceof PauseScreen;
		for (int lockedSlot : InventoryLockState.getLockedSlots()) {
			int[] pos = hotbarSlotPosition(graphics, lockedSlot);
			if (faded) {
				drawBadgeFaded(graphics, pos[0], pos[1]);
			} else {
				drawBadge(graphics, pos[0], pos[1]);
			}
		}
	}

	/** Top-left pixel of the given hotbar slot, for the fixed bottom-center hotbar bar. */
	public static int[] hotbarSlotPosition(GuiGraphics graphics, int hotbarSlot) {
		int screenWidth = graphics.guiWidth();
		int screenHeight = graphics.guiHeight();
		int hotbarLeft = screenWidth / 2 - 91;
		int hotbarTop = screenHeight - 22;

		int slotX = hotbarLeft + 2 + hotbarSlot * 20;
		int slotY = hotbarTop + 2;
		return new int[] {slotX, slotY};
	}

	/** Draws the padlock badge anchored to the bottom-right of a 16px slot whose top-left is (slotX, slotY). */
	public static void drawBadge(GuiGraphics graphics, int slotX, int slotY) {
		drawBadge(graphics, slotX, slotY, false);
	}

	/** Same badge, but translucent - used where the backdrop itself is dimmed/blurred (e.g. the pause menu). */
	public static void drawBadgeFaded(GuiGraphics graphics, int slotX, int slotY) {
		drawBadge(graphics, slotX, slotY, true);
	}

	private static void drawBadge(GuiGraphics graphics, int slotX, int slotY, boolean faded) {
		int bx = slotX + 10;
		int by = slotY + 9;

		int shackle = faded ? SHACKLE_COLOR_FADED : SHACKLE_COLOR;
		int outline = faded ? OUTLINE_COLOR_FADED : OUTLINE_COLOR;
		int body = faded ? BODY_COLOR_FADED : BODY_COLOR;

		// shackle (staple shape)
		graphics.fill(bx + 1, by, bx + 5, by + 1, shackle);
		graphics.fill(bx + 1, by, bx + 2, by + 2, shackle);
		graphics.fill(bx + 4, by, bx + 5, by + 2, shackle);

		// body with keyhole
		graphics.fill(bx, by + 2, bx + 6, by + 7, outline);
		graphics.fill(bx + 1, by + 3, bx + 5, by + 6, body);
		graphics.fill(bx + 2, by + 4, bx + 4, by + 5, outline);
	}
}
