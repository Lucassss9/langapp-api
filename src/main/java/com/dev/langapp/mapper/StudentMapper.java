package com.dev.langapp.mapper;

import com.dev.langapp.dto.StudentRequest;
import com.dev.langapp.dto.StudentResponse;
import com.dev.langapp.entity.Student;
import com.dev.langapp.repository.StudentRepository;

import java.util.UUID;

public class StudentMapper {
    public static Student toStudent(UUID minecraftUuid, StudentRequest studentRequest) {
        Student student = new Student();

        student.setName(studentRequest.name());
        student.setMinecraftUuid(minecraftUuid);
        student.setCefrLevel(studentRequest.cefrLevel());
        student.setTargetLanguage(studentRequest.targetLanguage());
        student.setExplanationLanguage(studentRequest.explanationLanguage());

        return student;
    }

    public static StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getMinecraftUuid(), student.getName(),
                student.getCefrLevel(), student.getTargetLanguage(), student.getExplanationLanguage());
    }

    public static Student applyTo(Student student, StudentRequest studentRequest) {
        student.setName(studentRequest.name());
        student.setCefrLevel(studentRequest.cefrLevel());
        student.setTargetLanguage(studentRequest.targetLanguage());
        student.setExplanationLanguage(studentRequest.explanationLanguage());

        return student;
    }
}