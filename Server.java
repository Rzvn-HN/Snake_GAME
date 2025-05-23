import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;

    public static int getClientNum() {
        return clientNum;
    }

    public static void setClientNum(int clientNum) {
        Server.clientNum = clientNum;
    }

    static int clientNum=0;
     static List<Object> clientList = new ArrayList<>();



    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try{

            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                setClientNum(getClientNum()+1);

                System.out.println("a new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);


                Thread thread = new Thread(clientHandler);
                thread.start();
                d();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void d() {
        if (getClientNum() >=2) {
            System.out.println("fuck");
        } else {
            System.out.println("still waiting...");
        }

    }
    public void closeServerSocket(){
        try{
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();

        //TODO


    }
}
