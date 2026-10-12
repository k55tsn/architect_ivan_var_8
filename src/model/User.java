package model;

public class User {
    private String login;
    private String password;
    private String fullName;

    public User(String login, String password, String fullName) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
}
