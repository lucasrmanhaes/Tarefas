package br.com.lucas.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    //Buscar usuário
    UserModel findByUserName(String name);
}


