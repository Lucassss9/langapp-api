package com.dev.langapp.service;

import com.dev.langapp.dto.StudentRequest;
import com.dev.langapp.entity.Student;
import com.dev.langapp.exception.StudentAlreadyExistsException;
import com.dev.langapp.exception.StudentNotFoundException;
import com.dev.langapp.mapper.StudentMapper;
import com.dev.langapp.repository.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public Student findByMinecraftUuid(UUID minecraftUuid) {
        return studentRepository.findByMinecraftUuid(minecraftUuid)
                .orElseThrow(() -> new StudentNotFoundException(minecraftUuid.toString()));
    }

    @Transactional
    public Student register(UUID minecraftUuid, StudentRequest studentRequest) {
        Optional<Student> findByUuid = studentRepository.findByMinecraftUuid(minecraftUuid);

        if(findByUuid.isPresent()) {
            throw new StudentAlreadyExistsException(minecraftUuid.toString());
        }

        Student student = StudentMapper.toStudent(minecraftUuid, studentRequest);

        return studentRepository.save(student);
    }
}