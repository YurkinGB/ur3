package sample;

import java.io.File;
import java.io.IOException;

public class User {
    private String login;
    private String password;
    private String nick;

    public User(String login, String password, String nick) {
        this.login = login;
        this.password = password;
        this.nick = nick;

        File logFile = new File(nick + ".txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
