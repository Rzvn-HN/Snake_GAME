import java.io.*;
import java.net.Socket;
import java.util.Scanner;



public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;

    private Main game;



    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);

                //TODO SEND ETELAT MAR
//                bufferedWriter.write(username + ": " + messageToSend); // pos x
//                bufferedWriter.write(username + ": " + messageToSend); // pos y
//                bufferedWriter.write(username + ": " + messageToSend); // direct
//                bufferedWriter.write(username + ": " + messageToSend); // alive
//                bufferedWriter.write(username + ": " + messageToSend); // colour
//                bufferedWriter.write(username + ": " + messageToSend); // stepsize

                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while (socket.isConnected()){
                    try{
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);



                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }





    public void closeEverything( Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter You name: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();

    }
}
