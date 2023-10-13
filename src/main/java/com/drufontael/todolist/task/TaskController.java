package com.drufontael.todolist.task;

import com.drufontael.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository repository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        taskModel.setUserId((UUID) request.getAttribute("userId"));
        var currentTime= LocalDateTime.now();
        if(currentTime.isAfter(taskModel.getStartAt())||currentTime.isAfter(taskModel.getEndAt())||
                taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datas invalidas!");
        }
        var taskcreated=repository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskcreated);
    }
    @GetMapping("/list")
    public ResponseEntity listByUserId(HttpServletRequest request){
        UUID userId=(UUID) request.getAttribute("userId");
        List<TaskModel> list=repository.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel,@PathVariable UUID id,HttpServletRequest request){
        TaskModel task=repository.findById(id).orElse(null);
        if(task==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada!");
        }
        UUID userId=(UUID) request.getAttribute("userId");
        if(!task.getUserId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não autorizado");
        }
        Utils.copyNonNullProperties(taskModel,task);
        repository.save(task);
        return ResponseEntity.ok(task);
    }
}
