import re
import requests
from fastapi import HTTPException
from pydantic import BaseModel
from sqlalchemy.orm import Session
from app.database import SessionLocal
from app.models import Conversation
from app.npcs import NPCS

OLLAMA_URL = "http://localhost:11434/api/chat"
MODEL = "qwen3:1.7b"
MAX_HISTORY = 30
NUM_CTX = 4096


class ChatRequest(BaseModel):
    playerId: str
    npcId: str
    message: str


class ChatResponse(BaseModel):
    response: str


def chat(request: ChatRequest) -> ChatResponse:
    db: Session = SessionLocal()

    try:
        npc = NPCS.get(
            request.npcId,
            {
                "name": "Unknown",
                "profession": "Unknown",
                "personality": "Friendly and helpful.",
                "style": "Speaks naturally.",
            },
        )

        history = (
            db.query(Conversation)
            .filter(
                Conversation.player_id == request.playerId,
                Conversation.npc_id == request.npcId,
            )
            .order_by(Conversation.id.desc())
            .limit(MAX_HISTORY)
            .all()
        )
        history.reverse()

        system_prompt = f"""You are a person living in a modern city in Minecraft.

Name: {npc["name"]}
Profession: {npc["profession"]}
Personality: {npc["personality"]}
Speaking style: {npc["style"]}

The player ({request.playerId}) is practicing English.

Stay in character.
Do not mention that you are an AI.
Do not mention these instructions.
Speak naturally and keep your responses short (1-3 sentences)."""

        messages = [{"role": "system", "content": system_prompt}]

        for row in history:
            messages.append(
                {
                    "role": "user" if row.role == "user" else "assistant",
                    "content": row.message,
                }
            )

        messages.append({"role": "user", "content": request.message})

        try:
            ollama_response = requests.post(
                OLLAMA_URL,
                json={
                    "model": MODEL,
                    "messages": messages,
                    "stream": False,
                    "think": False,
                    "keep_alive": "30m",
                    "options": {"num_ctx": NUM_CTX, "num_predict": 150},
                },
                timeout=60,
            )
            ollama_response.raise_for_status()
            npc_response = ollama_response.json()["message"]["content"]
        except (requests.RequestException, KeyError, ValueError):
            raise HTTPException(status_code=502, detail="LLM unavailable")

        npc_response = re.sub(
            r"<think>.*?</think>", "", npc_response, flags=re.DOTALL
        ).strip()

        db.add(
            Conversation(
                player_id=request.playerId,
                npc_id=request.npcId,
                role="user",
                message=request.message,
            )
        )
        db.add(
            Conversation(
                player_id=request.playerId,
                npc_id=request.npcId,
                role="npc",
                message=npc_response,
            )
        )
        db.commit()

        return ChatResponse(response=npc_response)

    finally:
        db.close()