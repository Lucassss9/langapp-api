package com.dev.langapp.controller;

import com.dev.langapp.dto.StudentRequest;
import com.dev.langapp.dto.StudentResponse;
import com.dev.langapp.entity.Student;
import com.dev.langapp.mapper.StudentMapper;
import com.dev.langapp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{minecraftUuid}")
    public StudentResponse findStudentByUuid(@PathVariable UUID minecraftUuid) {
        Student student = studentService.findByMinecraftUuid(minecraftUuid);

        return StudentMapper.toResponse(student);
    }

    @PostMapping("/register/{minecraftUuid}")
    public StudentResponse registerStudent(@Valid @RequestBody StudentRequest studentRequest, @PathVariable UUID minecraftUuid) {
        Student student = studentService.register(minecraftUuid, studentRequest);

        return StudentMapper.toResponse(student);
    }
}
