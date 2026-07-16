package net.pvplock.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import net.pvplock.InventoryLockState;
import net.pvplock.PvPLockMod;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

	@Inject(method = "drop(Z)Z", at = @At("HEAD"), cancellable = true)
	private void pvplockmod$blockDrop(boolean dropStack, CallbackInfoReturnable<Boolean> cir) {
		LocalPlayer self = (LocalPlayer) (Object) this;
		int selected = self.getInventory().getSelectedSlot();

		if (InventoryLockState.isSlotLocked(selected)) {
			self.displayClientMessage(
				Component.literal("That slot is locked - press ")
					.append(PvPLockMod.getToggleLockKey().getTranslatedKeyMessage())
					.append(" to unlock")
					.withStyle(ChatFormatting.RED),
				true
			);
			cir.setReturnValue(false);
		}
	}
}
