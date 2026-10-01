package languagecraft.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class ChatApi {
	private static final String URL = "http://localhost:8000/api/ai/chat";

	private static final HttpClient HTTP = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(5))
			.build();

	private ChatApi() {
	}

	public static CompletableFuture<String> send(String playerId, String npcId, String message) {
		JsonObject body = new JsonObject();
		body.addProperty("playerId", playerId);
		body.addProperty("npcId", npcId);
		body.addProperty("message", message);

		HttpRequest request = HttpRequest.newBuilder(URI.create(URL))
				.timeout(Duration.ofSeconds(60))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
				.build();

		return HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
				.thenApply(res -> {
					if (res.statusCode() != 200) {
						throw new RuntimeException("HTTP " + res.statusCode());
					}
					return JsonParser.parseString(res.body())
							.getAsJsonObject()
							.get("response")
							.getAsString();
				});
	}
}
