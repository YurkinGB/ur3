package Server;

import sample.Authentication;
import sample.Controller;
import sample.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MyServer {

    private static ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        MyServer.users = users;
    }

    public static void main(String[] args) {

        Socket socket;
        Scanner scanner = new Scanner(System.in);

        ServerSocket serverSocket;
        {
            try {
                serverSocket = new ServerSocket(8189);
                System.out.println("Server started");
                socket = serverSocket.accept();
                System.out.println("Client connected");

                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                new Thread(() -> {
                    while (true) {
                        try {
                            String str = scanner.nextLine();
                            out.writeUTF(str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                while (true){
                    String strClient = in.readUTF();
                    if (strClient.startsWith("/auth")){
                        String[] part = strClient.split(" ");
                        if (part.length < 3){
                            out.writeUTF("Логин и пароль не могут быть пустыми!!!");
                        }else {
                            String nick = Authentication.auth(part[1], part[2]);
                            if (nick != null) {
                                users.add(new User(part[1], part[2], nick));
                                out.writeUTF("/ " + nick);
                                out.writeUTF("Аутентификация пройдена, вы вошли под ником " + nick);
                                out.writeUTF("Чтобы изменить ник введите команду \'/upd новый_ник\' ");
                                break;
                            } else {
                                out.writeUTF("Неверный логин или пароль");
                            }
                        }
                    }else {
                        out.writeUTF("Пожалуйста авторизуйтесь, \'/auth логин пароль\' \n");
                    }
                }

                while (true) {
                    String strClient = in.readUTF();

                    if (strClient.startsWith("/up")){
                        String[] part = strClient.split(" ");
                        if (part.length < 2){
                            out.writeUTF("Ник не может быть пустым!!!");
                        }else {
                            Authentication.update(Controller.getName(), part[1]);
                            out.writeUTF("Ник успешно изменен!!!");
                        }
                    }
                    System.out.println("client say-> " + strClient);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
