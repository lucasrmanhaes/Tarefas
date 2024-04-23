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
        //Capturando usuário e senha
        String authorization = request.getHeader("Authorization");
        //Separando Base64
        String authEncoded = authorization.substring("Basic".length()).trim();
        //Convertendo base64 para um array de byte
        byte[] authDecode = Base64.getDecoder().decode(authEncoded);
        //convertendo array de byte para String
        String authString = new String(authDecode);
        //Separando a String username:password
        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];
        //Validando usuário usando o UserRepository
        var User = this.userRepository.findByUserName(username);
        if (User == null) {
            response.sendError(401); //Solicitação recusada (credenciais)
        } else {
            //Validando senha usando o Bcrypt
            var passwordVerifier = BCrypt.verifyer().verify(password.toCharArray(), User.getPassword());
            if (passwordVerifier.verified) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(403); //Solicitação recusada
            }
        }
    }
}