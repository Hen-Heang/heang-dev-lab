package com.heang.springmybatistest.service;

import com.heang.springmybatistest.model.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    void createStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(Long id);
}
