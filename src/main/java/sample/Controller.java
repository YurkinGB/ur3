package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


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
    private static final int HIS_BUF = 100;

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
                    if (strServer.startsWith("/ ")){
                        String[] n = strServer.split(" ");
                        name = n[1];
                        File file = new File(name + ".txt");
                        if (file.exists()){
                            String str;
                            int i = 0;
                            ReversedLinesFileReader reader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8);
                            List<String> buf = new ArrayList<>();
                            while ((str = reader.readLine()) != null && i < HIS_BUF){
                                buf.add(str);
                                i++;
                                //fxTextArea.appendText("HISTORY-> " + str + "\n");
                            } reader.close();
                            for (i = buf.size()-1; i >= 0; i--) {
                                fxTextArea.appendText("HISTORY-> " + buf.get(i) + "\n");
                            }
                        }
                    }else {
                        fxTextArea.appendText("server say-> " + strServer);
                        fxTextArea.appendText("\n");
                    }
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
                fxTextArea.appendText(name + "->" + fxTextFild.getText() + "\n");
                out.writeUTF(fxTextFild.getText());
                fxTextFild.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onStageClose(){
        String[] sArr = fxTextArea.getText().split("\n");
        try (FileWriter fileWriter = new FileWriter(name + ".txt", true)){
            if (sArr.length > HIS_BUF){
                for (int i = 0; i < sArr.length; i++) {
                    if (!sArr[i].startsWith("HISTORY-> ")) {
                        fileWriter.write(sArr[i] + "\n");
                    }
                }
            }else {
                for (int i = 0; i < sArr.length; i++) {
                    if (!sArr[i].startsWith("HISTORY-> ")) {
                        fileWriter.write(sArr[i] + "\n");
                    }
                }
                //fileWriter.write(fxTextArea.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeConnection();
    }


    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Controller.name = name;
    }

}
