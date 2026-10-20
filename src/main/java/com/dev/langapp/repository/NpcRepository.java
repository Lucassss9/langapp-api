package com.dev.langapp.repository;

import com.dev.langapp.entity.Npc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NpcRepository extends JpaRepository<Npc, Long> {
}
