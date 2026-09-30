import requests
from pydantic import BaseModel


class ChatRequest(BaseModel):
    playerId: str
    npcId: str
    message: str


class ChatResponse(BaseModel):
    response: str


def chat(request: ChatRequest) -> ChatResponse:
    prompt = f"""
You are an NPC in Minecraft.

NPC: {request.npcId}
Player: {request.playerId}

The player is practicing English.
Speak naturally and keep your response relatively short.

Player says:
{request.message}

Respond as the NPC.
"""

    ollama_response = requests.post(
        "http://localhost:11434/api/generate",
        json={
            "model": "qwen3:1.7b",
            "prompt": prompt,
            "stream": False,
        },
    )

    ollama_response.raise_for_status()

    data = ollama_response.json()

    return ChatResponse(
        response=data["response"]
    )