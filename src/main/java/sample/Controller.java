package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {

    @FXML
    private TextArea fxTextArea;

    @FXML
    private TextField fxTextFild;

    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static String name;

    public Controller() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //prepareGUI();

    }

    private void openConnection() throws IOException{

        socket = new Socket(SERVER_ADDR, SERVER_PORT);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true){
                    String strServer = in.readUTF();
                    if (strServer.equals("/end")){
                        closeConnection();
                        break;
                    }
                    fxTextArea.appendText("server say-> " + strServer);
                    fxTextArea.appendText("\n");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();

    }

    public void closeConnection(){
        try {
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void action() {
        try {
            if (!fxTextFild.getText().isEmpty()) {
                //fxTextArea.appendText(fxTextFild.getText() + "\n");
                out.writeUTF(fxTextFild.getText());
                fxTextFild.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Controller.name = name;
    }
}
