package com.feliciashang.spring.jpa.postgresql.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.feliciashang.spring.jpa.postgresql.model.ToDo;
import com.feliciashang.spring.jpa.postgresql.repository.ToDoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//cors which is the host that is representing the ui, enables app to accept the request from this host
@CrossOrigin(origins = "http://localhost:8091")
@RestController
@RequestMapping("/api")
public class ToDoController {
    @Autowired
    ToDoRepository todoRepository;

    @GetMapping("/todo")
    public ResponseEntity<List<ToDo>> getAllToDo(@RequestParam(required = false) String title) {
        try {

            List<ToDo> todo = new ArrayList<ToDo>();
            if (title == null) {
                todoRepository.findAll().forEach(todo::add);
            } else {
                todoRepository.findByTitleContaining(title).forEach(todo::add);
            }
            if (todo.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getTodoById(@PathVariable("id") long id) {
        Optional<ToDo> todoData = todoRepository.findById(id);

        if (todoData.isPresent()) {
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/todo")
    public ResponseEntity<ToDo> createTodo(@RequestBody ToDo todo) {
        try {
            ToDo _todo = todoRepository.save(new ToDo(todo.getTitle(), todo.getDescription(), false));
            return new ResponseEntity<>(_todo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/todo/{id}")
    public ResponseEntity<ToDo> updateTodo(@PathVariable("id") long id, @RequestBody ToDo todo) {
        Optional<ToDo> todoData = todoRepository.findById(id);

        if (todoData.isPresent()) {
            ToDo _todo = todoData.get();
            _todo.setTitle(todo.getTitle());
            _todo.setDescription(todo.getDescription());
            _todo.setCompleted(todo.isCompleted());
            return new ResponseEntity<>(todoRepository.save(_todo), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<HttpStatus> deleteToDo(@PathVariable("id") long id) {
        try {
            todoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/todo")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            todoRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todo/completed")
    public ResponseEntity<List<ToDo>> findByPublished() {
        try {
            List<ToDo> tutorials = todoRepository.findByCompleted(true);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
