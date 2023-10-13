package com.drufontael.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    public List<TaskModel> findByUserId(UUID userID);
    public Optional<TaskModel> findById(UUID id);

}
