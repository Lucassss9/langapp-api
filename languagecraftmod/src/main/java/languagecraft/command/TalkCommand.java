package languagecraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import languagecraft.LanguageCraft;
import languagecraft.api.ChatApi;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;

public final class TalkCommand {
	public static final Map<String, String> NPC_NAMES = Map.of(
			"npc_001", "Michael",
			"npc_002", "Sarah",
			"npc_003", "James",
			"npc_004", "Emily"
	);

	private TalkCommand() {
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("talk")
				.then(CommandManager.argument("npc", StringArgumentType.word())
						.suggests((ctx, builder) -> CommandSource.suggestMatching(NPC_NAMES.keySet(), builder))
						.then(CommandManager.argument("message", StringArgumentType.greedyString())
								.executes(ctx -> {
									ServerCommandSource source = ctx.getSource();
									ServerPlayerEntity player = source.getPlayerOrThrow();
									MinecraftServer server = source.getServer();

									String npcId = StringArgumentType.getString(ctx, "npc");
									String message = StringArgumentType.getString(ctx, "message");
									String npcName = NPC_NAMES.getOrDefault(npcId, npcId);

									ChatApi.send(player.getName().getString(), npcId, message)
											.whenComplete((reply, error) -> server.execute(() -> {
												if (error != null) {
													LanguageCraft.LOGGER.error("Chat API failed", error);
													player.sendMessage(Text.literal("The NPC can't answer right now.")
															.formatted(Formatting.RED));
												} else {
													player.sendMessage(Text.literal("<" + npcName + "> " + reply));
												}
											}));
									return 1;
								}))));
	}
}
