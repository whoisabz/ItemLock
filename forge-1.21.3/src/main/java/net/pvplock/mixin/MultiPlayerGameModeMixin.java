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
import net.minecraft.world.item.ItemStack;
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

		// Double-clicking a stack (PICKUP_ALL) sweeps every matching item in the container
		// into the cursor - including locked slots. The click itself lands on some other,
		// unlocked slot, so the per-slot check below never sees it, and a locked stack is
		// quietly drained. This is the one that loses items mid-fight: restocking by
		// double-clicking a matching stack is completely ordinary play.
		if (clickType == ClickType.PICKUP_ALL && wouldDrainLockedSlot(player)) {
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

	/**
	 * True if a "gather all" click would pull items out of a locked hotbar slot, i.e. the
	 * cursor holds something a locked slot also holds. Matching uses the same
	 * item-and-components test vanilla uses to decide what to sweep up.
	 */
	private static boolean wouldDrainLockedSlot(Player player) {
		AbstractContainerMenu menu = player.containerMenu;
		if (menu == null) {
			return false;
		}

		ItemStack carried = menu.getCarried();
		if (carried.isEmpty()) {
			return false;
		}

		Inventory inventory = player.getInventory();
		for (int lockedSlot : InventoryLockState.getLockedSlots()) {
			// Locked slots are hotbar indices by construction; bound-check defensively so a
			// stale saved value can never index out of the inventory.
			if (lockedSlot < 0 || lockedSlot > 8) {
				continue;
			}
			ItemStack lockedStack = inventory.getItem(lockedSlot);
			if (!lockedStack.isEmpty() && ItemStack.isSameItemSameComponents(carried, lockedStack)) {
				return true;
			}
		}
		return false;
	}
}
