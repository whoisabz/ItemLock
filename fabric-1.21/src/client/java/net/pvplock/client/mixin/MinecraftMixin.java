package net.pvplock.client.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.pvplock.client.CompatInventory;
import net.pvplock.client.InventoryLockState;
import net.pvplock.client.PvPLockModClient;

/**
 * Blocks the swap-offhand key (default F) while the selected hotbar slot is locked.
 *
 * Swapping to the offhand never goes through MultiPlayerGameMode.handleInventoryMouseClick
 * - it is sent straight to the server as a player-action packet - so the inventory-click
 * guard cannot see it, and a locked item would leave its slot with nothing stopping it.
 *
 * Rather than cancel the vanilla branch, this consumes the keybind's queued clicks before
 * vanilla reads them. consumeClick() drains one press per call, so the loop empties the
 * queue and vanilla then sees no press at all.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

	@Inject(method = "handleKeybinds", at = @At("HEAD"), require = 1)
	private void pvplockmod$blockOffhandSwap(CallbackInfo ci) {
		Minecraft self = (Minecraft) (Object) this;
		LocalPlayer player = self.player;

		if (player == null || !InventoryLockState.isLocked()) {
			return;
		}
		int selected;
		try {
			selected = CompatInventory.getSelectedSlot(player.getInventory());
		} catch (IllegalStateException e) {
			// Compat detection failed on this version - don't block the swap over a check
			// we can't perform, matching how the Q-drop guard degrades.
			return;
		}
		if (!InventoryLockState.isSlotLocked(selected)) {
			return;
		}
		boolean blocked = false;
		while (self.options.keySwapOffhand.consumeClick()) {
			blocked = true;
		}

		if (blocked) {
			player.displayClientMessage(
				Component.literal("That slot is locked - press ")
					.append(PvPLockModClient.getToggleLockKey().getTranslatedKeyMessage())
					.append(" to unlock")
					.withStyle(ChatFormatting.RED),
				true
			);
		}
	}
}
