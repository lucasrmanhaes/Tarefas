package br.com.lucas.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        //Validando data para criação/finalização da task deve ser maior que a data do servidor
        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt())  || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio/termino deve ser maior que a data atual");
        }

        //Validando data da criação da task deve ser menor que a data da finalização da task
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio da tarefa deve ser maior que a data de término");
        }

        //Rota autorizada
        var task = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);

    }

    @GetMapping("/")
    public ResponseEntity listTasks(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.findByIdUser((UUID) idUser));
    }

}
