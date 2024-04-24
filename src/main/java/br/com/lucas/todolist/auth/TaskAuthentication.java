package br.com.lucas.todolist.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.lucas.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class TaskAuthentication extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        //Criando o contexto da autenticação apenas a task com o servletPath
        var servletPath = request.getServletPath();
        //Se a rota for para tasks fazer autenticação
        if (servletPath.equals("/tasks/")) {

            //Capturando usuário e senha
            var authorization = request.getHeader("Authorization");
            //Separando Base64
            var authEncoded = authorization.substring("Basic".length()).trim();
            //Convertendo base64 para um array de byte
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            //convertendo array de byte para String
            var authString = new String(authDecode);
            //Separando a String username:password
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            //Validando usuário usando o UserRepository
            var user = this.userRepository.findByUserName(username);
            if (user == null) {
                response.sendError(401); //Solicitação recusada (credenciais)
            }
            else {
                //Validando senha usando o Bcrypt
                var passwordVerifier = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerifier.verified) {
                    //Recuperando idUser de User para setar no idUser da task
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }

                else {
                    response.sendError(401); //Solicitação recusada
                }
            }
        }
        //Senão a rota segue normalmente
        else{
            filterChain.doFilter(request, response);
        }

    }
}