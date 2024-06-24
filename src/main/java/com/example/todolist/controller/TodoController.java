package com.example.todolist.controller;

import com.example.todolist.model.Todo;
import com.example.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getTodosByUserId(@RequestParam String userId) {
        return todoService.getTodosByUserId(userId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        todo.setIndate(LocalDateTime.now());
        
        if (todo.isCompleted()) {
            todo.setDueDate(LocalDateTime.now());
        }
        return todoService.saveOrUpdate(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Optional<Todo> existingTodo = todoService.getTodoById(id);
        if (!existingTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        todo.setIndate(existingTodo.get().getIndate());

        System.out.println("수정할 일정 ID: " + id);
        
        if (todo.isCompleted() && !existingTodo.get().isCompleted()) {
            todo.setDueDate(LocalDateTime.now());
        } else {
            todo.setDueDate(existingTodo.get().getDueDate());
        }

        System.out.println("수정된 제목: " + todo.getTitle() + "(" + id + ")");
        System.out.println("수정된 설명: " + todo.getDescription() + "(" + id + ")");
        
        Todo updatedTodo = todoService.saveOrUpdate(todo);
        
        System.out.println("수정완료(" + id + ")");
        
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
        if (!todoService.getTodoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("삭제할 일정 ID: " + id);
        
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }
}
