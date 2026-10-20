package com.dev.langapp.entity;

import com.dev.langapp.enums.MistakeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Mistake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private Attempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MistakeType type;

    @Column(nullable = false, length = 1000)
    private String whatWasSaid;

    @Column(nullable = false, length = 1000)
    private String correction;

    @Column(nullable = false, length = 1000)
    private String explanation;
}