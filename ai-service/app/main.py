from fastapi import FastAPI
from app.chat import ChatResponse, ChatRequest, chat

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