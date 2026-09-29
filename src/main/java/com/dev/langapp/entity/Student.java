package com.dev.langapp.entity;

import com.dev.langapp.enums.CefrLevel;
import com.dev.langapp.enums.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  unique = true)
    private UUID minecraftUuid;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CefrLevel cefrLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language targetLanguage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language explanationLanguage;

}
