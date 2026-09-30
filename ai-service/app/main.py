from fastapi import FastAPI
from app.chat import ChatResponse, ChatRequest, chat
from app.database import Base, engine
from app.models import Conversation

Base.metadata.create_all(bind=engine)

app = FastAPI(title='LanguageCraft')

@app.get("/")
def root():
    return {
        "status": "ok",
        "service": "ai-service"
    }

@app.post("/api/ai/chat", response_model=ChatResponse)
def ai_chat(request: ChatRequest):
    return chat(request)