package com.dev.langapp.repository;

import com.dev.langapp.entity.Mistake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MistakeRepository extends JpaRepository<Mistake, Long> {
}
