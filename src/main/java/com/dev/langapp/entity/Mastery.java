package com.dev.langapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "skill_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Mastery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int correctStreak;

    @Column(nullable = false)
    private Instant lastPracticedAt;

    @Column(nullable = false)
    private LocalDate nextReviewAt;
}
