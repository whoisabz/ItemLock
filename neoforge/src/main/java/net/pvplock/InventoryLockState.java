package net.pvplock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.neoforged.fml.loading.FMLPaths;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class InventoryLockState {
	private static final Set<Integer> lockedSlots = new LinkedHashSet<>();
	private static final Path SAVE_PATH = FMLPaths.CONFIGDIR.get().resolve("pvplockmod_locked_slots.txt");

	private InventoryLockState() {
	}

	public static boolean isLocked() {
		return !lockedSlots.isEmpty();
	}

	public static boolean isSlotLocked(int slot) {
		return lockedSlots.contains(slot);
	}

	public static Set<Integer> getLockedSlots() {
		return Collections.unmodifiableSet(lockedSlots);
	}

	/**
	 * Pressing the toggle key locks whichever hotbar slot is currently selected,
	 * in addition to any slots already locked. Pressing it again on an already-locked
	 * slot unlocks just that one.
	 */
	public static void toggleForSlot(int currentSlot) {
		Minecraft client = Minecraft.getInstance();
		Component message;

		if (lockedSlots.remove(currentSlot)) {
			message = Component.literal("Slot unlocked").withStyle(ChatFormatting.GREEN);
		} else {
			lockedSlots.add(currentSlot);
			message = Component.literal("Slot " + (currentSlot + 1) + " locked").withStyle(ChatFormatting.RED);
		}

		save();

		if (client.player != null) {
			client.player.displayClientMessage(message, true);
		}
	}

	public static void load() {
		if (!Files.exists(SAVE_PATH)) {
			return;
		}
		try {
			String content = Files.readString(SAVE_PATH).trim();
			lockedSlots.clear();
			if (!content.isEmpty()) {
				for (String part : content.split(",")) {
					lockedSlots.add(Integer.parseInt(part.trim()));
				}
			}
		} catch (IOException | NumberFormatException e) {
			PvPLockMod.LOGGER.warn("Failed to load locked slots", e);
		}
	}

	private static void save() {
		try {
			Files.writeString(SAVE_PATH, lockedSlots.stream().map(String::valueOf).collect(Collectors.joining(",")));
		} catch (IOException e) {
			PvPLockMod.LOGGER.warn("Failed to save locked slots", e);
		}
	}
}
