package net.pvplock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

import net.pvplock.LockHudRenderer;

/**
 * Draws the hotbar lock badges by appending to the vanilla HUD render.
 *
 * Forge's AddGuiOverlayLayersEvent would be the tidier way to do this, but it is missing
 * from Forge 51 (1.21), 56 (1.21.6) and 57 (1.21.7) - present on either side of both gaps.
 * Gui.render(GuiGraphics, DeltaTracker) is byte-identical across all of 1.21 - 1.21.11, so
 * mixing into it is the one approach that works uniformly on every version in the range.
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

	@Inject(method = "render", at = @At("TAIL"), remap = false)
	private void pvplockmod$drawHotbarLockBadges(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		LockHudRenderer.render(graphics, deltaTracker);
	}
}
