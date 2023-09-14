package com.feliciashang.spring.jpa.postgresql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
 import com.feliciashang.spring.jpa.postgresql.model.ToDo;

import java.util.List;
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByCompleted(boolean completed);
    List<ToDo> findByTitleContaining(String title);
}
