package com.dev.langapp.repository;

import com.dev.langapp.entity.Mastery;
import com.dev.langapp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MasteryRepository extends JpaRepository<Mastery, Long> {

    List<Mastery> findByStudent(Student studentId);
    List<Mastery> findByNextReviewAtBefore(LocalDate date);
}
