package net.pvplock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import net.pvplock.InventoryLockState;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {

	@Inject(method = "handleInventoryMouseClick", at = @At("HEAD"), cancellable = true, remap = false)
	private void pvplockmod$blockLockedSlotClick(int containerId, int slotId, int mouseButton, ClickType clickType, Player player, CallbackInfo ci) {
		if (!InventoryLockState.isLocked()) {
			return;
		}

		// Pressing a number key to swap a locked slot with whatever is hovered.
		if (clickType == ClickType.SWAP && InventoryLockState.isSlotLocked(mouseButton)) {
			ci.cancel();
			return;
		}

		AbstractContainerMenu menu = player.containerMenu;
		if (menu != null && slotId >= 0 && slotId < menu.slots.size()) {
			Slot slot = menu.getSlot(slotId);
			if (slot.container instanceof Inventory && InventoryLockState.isSlotLocked(slot.getContainerSlot())) {
				ci.cancel();
			}
		}
	}
}
