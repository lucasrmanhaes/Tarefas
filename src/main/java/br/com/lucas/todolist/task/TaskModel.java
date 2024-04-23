package br.com.lucas.todolist.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "TB_TASKS")
public class TaskModel {

    @GeneratedValue(generator = "UUID") @Id private UUID id;
    private UUID idUser;
    @Column(length = 50) private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    @CreationTimestamp private LocalDateTime createdAt;

}
