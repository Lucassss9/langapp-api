package com.dev.langapp.mapper;

import com.dev.langapp.dto.StudentRequest;
import com.dev.langapp.entity.Student;

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
}
