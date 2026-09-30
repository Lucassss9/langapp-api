import requests
from pydantic import BaseModel
from sqlalchemy.orm import Session
from app.database import SessionLocal
from app.models import Conversation


class ChatRequest(BaseModel):
    playerId: str
    npcId: str
    message: str


class ChatResponse(BaseModel):
    response: str


def chat(request: ChatRequest) -> ChatResponse:

    db: Session = SessionLocal()

    try:
        history = (
            db.query(Conversation)
            .filter(
                Conversation.player_id == request.playerId,
                Conversation.npc_id == request.npcId
            )
            .order_by(Conversation.id)
            .all()
        )

        db.add(
            Conversation(
                player_id=request.playerId,
                npc_id=request.npcId,
                role="user",
                message=request.message
            )
        )

        db.commit()

        conversation_history = ""

        for message in history:
            conversation_history += (
                f"{message.role}: {message.message}\n"
            )

        conversation_history += (
            f"user: {request.message}\n"
        )

        prompt = f"""
You are an NPC in Minecraft.

NPC: {request.npcId}
Player: {request.playerId}

The player is practicing English.
Speak naturally and keep your response relatively short.

Conversation history:
{conversation_history}

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

        db.add(
            Conversation(
                player_id=request.playerId,
                npc_id=request.npcId,
                role="npc",
                message=npc_response
            )
        )

        db.commit()

        return ChatResponse(
            response=npc_response
        )

    finally:
        db.close()