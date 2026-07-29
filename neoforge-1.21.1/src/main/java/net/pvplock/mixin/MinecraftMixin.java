package net.pvplock.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.pvplock.InventoryLockState;
import net.pvplock.PvPLockMod;

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

	@Inject(method = "handleKeybinds", at = @At("HEAD"))
	private void pvplockmod$blockOffhandSwap(CallbackInfo ci) {
		Minecraft self = (Minecraft) (Object) this;
		LocalPlayer player = self.player;

		if (player == null || !InventoryLockState.isLocked()) {
			return;
		}
		if (!InventoryLockState.isSlotLocked(player.getInventory().selected)) {
			return;
		}
		boolean blocked = false;
		while (self.options.keySwapOffhand.consumeClick()) {
			blocked = true;
		}

		if (blocked) {
			player.displayClientMessage(
				Component.literal("That slot is locked - press ")
					.append(PvPLockMod.getToggleLockKey().getTranslatedKeyMessage())
					.append(" to unlock")
					.withStyle(ChatFormatting.RED),
				true
			);
		}
	}
}
