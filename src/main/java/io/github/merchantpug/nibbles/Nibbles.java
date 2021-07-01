package io.github.merchantpug.nibbles;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Nibbles implements ModInitializer {
	public static Logger LOGGER = LogManager.getLogger(Nibbles.class);

	@Override
	public void onInitialize() {
		LOGGER.info("Nibbles has been initialized. Eat your heart out!");
	}
}
