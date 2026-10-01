package languagecraft.npc;

import com.mojang.brigadier.arguments.StringArgumentType;
import languagecraft.LanguageCraft;
import languagecraft.api.ChatApi;
import languagecraft.command.TalkCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class NpcManager {
	private static final String TAG_PREFIX = "lc_";
	private static final Map<UUID, String> TALKING = new ConcurrentHashMap<>();

	private NpcManager() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(CommandManager.literal("npc")
						.then(CommandManager.literal("spawn")
								.then(CommandManager.argument("npc", StringArgumentType.word())
										.suggests((ctx, builder) ->
												CommandSource.suggestMatching(TalkCommand.NPC_NAMES.keySet(), builder))
										.executes(ctx -> spawn(
												ctx.getSource().getPlayerOrThrow(),
												StringArgumentType.getString(ctx, "npc")))))));

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (world.isClient() || hand != Hand.MAIN_HAND) {
				return ActionResult.PASS;
			}

			String npcId = npcIdOf(entity);
			if (npcId == null) {
				return ActionResult.PASS;
			}

			TALKING.put(player.getUuid(), npcId);
			String name = TalkCommand.NPC_NAMES.getOrDefault(npcId, npcId);
			player.sendMessage(
					Text.literal("You are talking to " + name + ". Type in chat. Say \"bye\" to leave.")
							.formatted(Formatting.GRAY),
					false);
			return ActionResult.SUCCESS;
		});

		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
			String npcId = TALKING.get(sender.getUuid());
			if (npcId == null) {
				return true;
			}

			String text = message.getSignedContent();
			String playerName = sender.getName().getString();

			if (text.trim().equalsIgnoreCase("bye")) {
				TALKING.remove(sender.getUuid());
				sender.sendMessage(Text.literal("Conversation ended.").formatted(Formatting.GRAY), false);
				return false;
			}

			String npcName = TalkCommand.NPC_NAMES.getOrDefault(npcId, npcId);
			MinecraftServer server = sender.server;

			sender.sendMessage(Text.literal("<" + playerName + "> " + text), false);

			ChatApi.send(playerName, npcId, text)
					.whenComplete((reply, error) -> server.execute(() -> {
						if (error != null) {
							LanguageCraft.LOGGER.error("Chat API failed", error);
							sender.sendMessage(Text.literal("The NPC can't answer right now.")
									.formatted(Formatting.RED), false);
						} else {
							sender.sendMessage(Text.literal("<" + npcName + "> " + reply), false);
						}
					}));

			return false;
		});
	}

	private static int spawn(ServerPlayerEntity player, String npcId) {
		String name = TalkCommand.NPC_NAMES.get(npcId);
		if (name == null) {
			player.sendMessage(Text.literal("Unknown NPC: " + npcId).formatted(Formatting.RED), false);
			return 0;
		}

		ServerWorld world = player.getServerWorld();
		VillagerEntity npc = EntityType.VILLAGER.create(world);
		if (npc == null) {
			return 0;
		}

		npc.refreshPositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getYaw(), 0f);
		npc.setCustomName(Text.literal(name));
		npc.setCustomNameVisible(true);
		npc.setAiDisabled(true);
		npc.setInvulnerable(true);
		npc.setPersistent();
		npc.addCommandTag(TAG_PREFIX + npcId);

		world.spawnEntity(npc);
		return 1;
	}

	private static String npcIdOf(Entity entity) {
		for (String tag : entity.getCommandTags()) {
			if (tag.startsWith(TAG_PREFIX)) {
				return tag.substring(TAG_PREFIX.length());
			}
		}
		return null;
	}
}