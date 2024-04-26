package br.com.lucas.todolist.task;

import br.com.lucas.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
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

    //127.0.0.1:8080/tasks/ab20ecc7-3844-4d16-b130-63205a75215a
    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskModel taskModel,  @PathVariable UUID id, HttpServletRequest request){
        //Buscando task pela id
        var task = this.taskRepository.findById(id).orElse(null);

        //Verificando se a tarefa existe
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        //Buscando id do usuario da task e id do usuario da autenticacao
        var userRequest = request.getAttribute("idUser");

        //Criando validação de usuário dono da task para atualização de tasks
        if(!userRequest.equals(task.getIdUser())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não autorizado");
        }

        //Copiando os atributos não-nulos passados na requisição para o objeto task da repository
        Utils.copyNonNullProperties(taskModel, task);
        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(task));
    }

}
