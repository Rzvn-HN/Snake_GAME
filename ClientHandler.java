import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;


    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            boardcastMessage("SERVER: " + clientUsername + "has entered in game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                boardcastMessage(messageFromClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void sendSnakeDetailToGroup(){}
    public void receiveSnakeDetailToGroup(){}

    public void boardcastMessage(String messageTOSend){
        for (ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageTOSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        boardcastMessage("SERVER: " + clientUsername + "has left the game!");
    }

    public void closeEverything( Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
    removeClientHandler();
    try{
        if (bufferedReader != null){
            bufferedReader.close();
        }
        if (bufferedWriter != null){
            bufferedWriter.close();
        }
        if (socket != null){
                socket.close();
        }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

}
