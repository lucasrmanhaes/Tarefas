package br.com.lucas.todolist.user;

import jakarta.persistence.Column;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name="TB_USERS")
public class UserModel {

    @GeneratedValue(generator = "UUID")
    @Id private UUID id;
    private String name;
    @Column(unique = true) private String userName;
    private String password;
    @CreationTimestamp private LocalDateTime createdAt;

    public UserModel(){}
    public UserModel(String name, String email, String password){
        this.name = name;
        this.userName = email;
        this.password = password;
    }

}
