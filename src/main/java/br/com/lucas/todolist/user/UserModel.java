package br.com.lucas.todolist.user;

public class UserModel {

    private String name;
    private String userName;
    private String password;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UserModel(){}
    public UserModel(String name, String email, String password){
        this.name = name;
        this.userName = email;
        this.password = password;
    }
}
