package net.pvplock.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import net.pvplock.client.CompatInventory;
import net.pvplock.client.InventoryLockState;
import net.pvplock.client.PvPLockModClient;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

	@Inject(method = "drop(Z)Z", at = @At("HEAD"), cancellable = true)
	private void pvplockmod$blockDrop(boolean dropStack, CallbackInfoReturnable<Boolean> cir) {
		LocalPlayer self = (LocalPlayer) (Object) this;
		int selected;
		try {
			selected = CompatInventory.getSelectedSlot(self.getInventory());
		} catch (IllegalStateException e) {
			// Compat detection failed on this version - let the drop happen rather than
			// crash the game over a locked-slot check we can't perform.
			PvPLockModClient.LOGGER.error("Could not read the selected hotbar slot on this Minecraft version", e);
			return;
		}

		if (InventoryLockState.isSlotLocked(selected)) {
			self.displayClientMessage(
				Component.literal("That slot is locked - press ")
					.append(PvPLockModClient.getToggleLockKey().getTranslatedKeyMessage())
					.append(" to unlock")
					.withStyle(ChatFormatting.RED),
				true
			);
			cir.setReturnValue(false);
		}
	}
}
