package praktikum.dto;

public class Credentials {
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    private final String email;
    private final String password;
    private final String name;


    public Credentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static Credentials fromUser(User user) {
        return new Credentials(user.getEmail(), user.getPassword(), user.getName());
    }
}
