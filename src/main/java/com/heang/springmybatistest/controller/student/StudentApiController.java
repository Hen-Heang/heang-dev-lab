package com.heang.springmybatistest.controller.student;

import com.heang.springmybatistest.model.Student;
import com.heang.springmybatistest.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentApiController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> list() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student detail(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public void create(@RequestBody Student student) {
        studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
