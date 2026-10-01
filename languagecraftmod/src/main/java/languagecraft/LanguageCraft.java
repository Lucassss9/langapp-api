package languagecraft;

import languagecraft.command.TalkCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import languagecraft.npc.NpcManager;

public class LanguageCraft implements ModInitializer {
	public static final String MOD_ID = "languagecraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> TalkCommand.register(dispatcher));

		NpcManager.register();

		LOGGER.info("LanguageCraft loaded");
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
