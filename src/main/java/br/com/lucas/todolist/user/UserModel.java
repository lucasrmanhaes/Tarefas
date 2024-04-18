package br.com.lucas.todolist.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

    @Data
    public class UserModel {

        @Getter @Setter
        private String name;
        private String userName;
        private String password;

        public UserModel(){}
        public UserModel(String name, String email, String password){
            this.name = name;
            this.userName = email;
            this.password = password;
        }

    }
