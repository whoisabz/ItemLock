package net.pvplock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import net.pvplock.InventoryLockState;
import net.pvplock.LockHudRenderer;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

	@Shadow(remap = false)
	protected int leftPos;

	@Shadow(remap = false)
	protected int topPos;

	// Targets render() only. renderContents() does not exist until Minecraft 1.21.6, and
	// naming a method that is absent is fatal on Forge - it is only a warning on Fabric,
	// which is why the Fabric variants can list both names with require = 0 and this cannot.
	@Inject(method = "render", at = @At("TAIL"), remap = false)
	private void pvplockmod$drawLockBadges(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (!InventoryLockState.isLocked()) {
			return;
		}

		AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
		AbstractContainerMenu menu = self.getMenu();

		// Slot item icons sit at z-offset 150 on this Minecraft generation, so the badge is
		// lifted above that; ordering against the item itself is handled by drawing on the
		// guiOverlay render type inside drawBadge.
		graphics.pose().pushPose();
		graphics.pose().translate(0.0F, 0.0F, 200.0F);
		for (Slot slot : menu.slots) {
			if (slot.container instanceof Inventory && InventoryLockState.isSlotLocked(slot.getContainerSlot())) {
				LockHudRenderer.drawBadge(graphics, leftPos + slot.x - 1, topPos + slot.y - 1);
			}
		}
		graphics.pose().popPose();
	}
}
