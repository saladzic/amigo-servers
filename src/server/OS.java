package server;

public abstract class OS {
    private final String name;
    private final String path;
    private final String username;
    private final String password;

    protected OS(String name, String path, String username, String password) {
        this.name = name;
        this.path = path;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
