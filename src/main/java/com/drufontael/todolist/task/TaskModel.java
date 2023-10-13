package com.drufontael.todolist.task;

import com.drufontael.todolist.exceptions.NumberCharExceededException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_task")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(length = 50)
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private UUID userId;

    public void setTitle(String title) {
        if(title.length()>50){
            throw new NumberCharExceededException("Tamanho do titulo excedido, máx=50!");
        }
        this.title = title;
    }
}
