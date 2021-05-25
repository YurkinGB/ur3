package sample;

import java.sql.*;

public class Authentication {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized String auth(String log, String pass){

        String nick = null;

        try (Connection mySqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Client",
                "root", "123656987")){
            PreparedStatement preparedStatement =
                    mySqlConnection.prepareStatement("SELECT * FROM Client.AuthClient WHERE login = ? and pass = ?");
            preparedStatement.setString(1, log);
            preparedStatement.setString(2, pass);
            ResultSet clientResultSet = preparedStatement.executeQuery();

            if (clientResultSet.next()){
                nick = clientResultSet.getString("nikc");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
          return nick;
    }

    public static synchronized void update(String nick, String newNick){
        try(Connection mySqlConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Client",
                "root", "123656987")) {
            PreparedStatement preparedStatement =
                    mySqlConnection.prepareStatement("UPDATE Client.AuthClient SET nikc = ? WHERE nikc = ?");

            preparedStatement.setString(1, newNick);
            preparedStatement.setString(2, nick);

            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}
