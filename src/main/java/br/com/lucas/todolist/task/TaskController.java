package br.com.lucas.todolist.task;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){

        //Recuperando idUser de User da Auth para setar no idUser da task
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        //Validando data para criação da task
        var currentDate = LocalDateTime.now();
        System.out.println(currentDate);
        if(currentDate.isAfter(taskModel.getStartAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser maior que a data atual");
        }

        var task = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);

    }

}
