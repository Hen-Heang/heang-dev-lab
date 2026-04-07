package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.model.Student;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> findAll();
    Student findById(Long id);
    void insert(Student student);
    void update(Student student);
    void delete(Long id);
}