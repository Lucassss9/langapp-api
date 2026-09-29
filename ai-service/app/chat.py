from pydantic import BaseModel

class ChatRequest(BaseModel):
    playerId: str
    npcId: str
    message: str

class ChatResponse(BaseModel):
    response: str

def chat(request: ChatRequest) -> ChatResponse:
    return ChatResponse(
        response=f"the NPC got your message: {request.message}"
    )