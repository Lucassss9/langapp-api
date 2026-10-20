package com.dev.langapp.repository;


import com.dev.langapp.entity.Mastery;
import com.dev.langapp.entity.Skill;
import com.dev.langapp.entity.Student;
import com.dev.langapp.enums.CefrLevel;
import com.dev.langapp.enums.Language;
import com.dev.langapp.enums.SkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
public class MasteryRepositoryTest {

    @Autowired
    private MasteryRepository masteryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void saveMasteryAndFindByStudent() {
        Mastery mastery = new Mastery();
        Student student = new Student();
        Skill skill = new Skill();

        student.setMinecraftUuid(UUID.randomUUID());
        student.setName("test");
        student.setTargetLanguage(Language.EN);
        student.setExplanationLanguage(Language.EN);
        student.setCefrLevel(CefrLevel.A1);

        skill.setName("test skill");
        skill.setType(SkillType.GRAMMAR);
        skill.setCefrLevel(CefrLevel.A1);

        mastery.setStudent(student);
        mastery.setSkill(skill);
        mastery.setScore(10);
        mastery.setCorrectStreak(5);
        mastery.setLastPracticedAt(Instant.now());
        mastery.setNextReviewAt(LocalDate.now());

        studentRepository.save(student);
        skillRepository.save(skill);
        masteryRepository.save(mastery);

        List<Mastery> find = masteryRepository.findByStudent(student);

        assertThat(find).hasSize(1);
    }

    @Test
    void minecraftUuidDuplicate() {
        Student student1 = new Student();
        Student student2 = new Student();

        UUID random = UUID.randomUUID();

        student1.setMinecraftUuid(random);
        student1.setName("test1");
        student1.setTargetLanguage(Language.EN);
        student1.setExplanationLanguage(Language.EN);
        student1.setCefrLevel(CefrLevel.A1);

        studentRepository.save(student1);

        student2.setMinecraftUuid(random);
        student2.setName("test");
        student2.setTargetLanguage(Language.EN);
        student2.setExplanationLanguage(Language.EN);
        student2.setCefrLevel(CefrLevel.A1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            studentRepository.save(student2);
        });
    }
}
