import requests
from pydantic import BaseModel


conversations = {}


class ChatRequest(BaseModel):
    playerId: str
    npcId: str
    message: str


class ChatResponse(BaseModel):
    response: str


def chat(request: ChatRequest) -> ChatResponse:

    conversation_id = f"{request.playerId}:{request.npcId}"

    if conversation_id not in conversations:
        conversations[conversation_id] = []

    conversations[conversation_id].append(
        {
            "role": "user",
            "content": request.message
        }
    )

    history = ""

    for message in conversations[conversation_id]:
        history += f"{message['role']}: {message['content']}\n"

    prompt = f"""
You are an NPC in Minecraft.

NPC: {request.npcId}
Player: {request.playerId}

The player is practicing English.
Speak naturally and keep your response relatively short.

Conversation history:
{history}

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

    npc_response = data["response"]

    conversations[conversation_id].append(
        {
            "role": "npc",
            "content": npc_response
        }
    )

    return ChatResponse(
        response=npc_response
    )