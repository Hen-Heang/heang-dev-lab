package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.StudentMapper;
import com.heang.springmybatistest.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper studentMapper;

    @Override
    public List<Student> getAllStudents() {
        return studentMapper.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentMapper.findById(id);
    }

    @Override
    public void createStudent(Student student) {
        studentMapper.insert(student);
    }

    @Override
    public void updateStudent(Student student) {
        studentMapper.update(student);
    }

    @Override
    public void deleteStudent(Long id) {
        studentMapper.delete(id);
    }
}
