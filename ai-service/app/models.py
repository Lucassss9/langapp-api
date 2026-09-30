from sqlalchemy import Column, Integer, String, Text
from app.database import Base


class Conversation(Base):
    __tablename__ = "conversations"

    id = Column(Integer, primary_key=True, index=True)
    player_id = Column(String, index=True)
    npc_id = Column(String, index=True)
    role = Column(String)
    message = Column(Text)