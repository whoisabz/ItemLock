package net.pvplock;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvPLockMod implements ModInitializer {
	public static final String MOD_ID = "pvplockmod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("PvP Inventory Lock initialized");
	}
}
