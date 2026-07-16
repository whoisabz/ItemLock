package net.pvplock.client.mixin;

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

import net.pvplock.client.InventoryLockState;
import net.pvplock.client.LockHudRenderer;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

	@Shadow
	protected int leftPos;

	@Shadow
	protected int topPos;

	@Inject(method = "renderContents", at = @At("TAIL"))
	private void pvplockmod$drawLockBadges(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (!InventoryLockState.isLocked()) {
			return;
		}

		AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
		AbstractContainerMenu menu = self.getMenu();

		for (Slot slot : menu.slots) {
			if (slot.container instanceof Inventory && InventoryLockState.isSlotLocked(slot.getContainerSlot())) {
				LockHudRenderer.drawBadge(graphics, leftPos + slot.x - 1, topPos + slot.y - 1);
			}
		}
	}
}
