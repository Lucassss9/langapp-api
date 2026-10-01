package com.dev.langapp.repository;

import com.dev.langapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMinecraftUuid(UUID minecraftUuid);
}
