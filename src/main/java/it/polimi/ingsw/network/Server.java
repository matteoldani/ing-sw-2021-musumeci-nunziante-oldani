package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will handle the correlation between the ServerSoocket and the list of clients created
 */
public class Server {

    private SocketServer socketServer;
    private static int port = 2222; //default value for debugging mode, port should be retrieved from args
    //in the future some hashmap containing the connection from socket ad the game has to be provided

    //The add method should be synchronized in order to avoid errors while adding participants
    private List<ServerClientConnection> lobby4players;
    private List<ServerClientConnection> lobby3players;
    private List<ServerClientConnection> lobby2players;


    public Server(){
        this.socketServer = new SocketServer(this);
        lobby4players = new ArrayList<>();
        lobby3players = new ArrayList<>();
        lobby2players = new ArrayList<>();
    }

    public int getPort(){ return port; }

    public SocketServer getSocketServer(){
        return socketServer;
    }

    public List<ServerClientConnection> getLobby4players() {
        return lobby4players;
    }

    public List<ServerClientConnection> getLobby3players() {
        return lobby3players;
    }

    public List<ServerClientConnection> getLobby2players() {
        return lobby2players;
    }

    public static void main(String[] args){
        System.out.println(Constants.ANSI_CYAN + "Maestri del Rinascimento | Server" + Constants.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println(">Insert the port which server will listen on.");
            System.out.print(">");
            try {
                port = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Numeric format requested, retry...");
            }
            if(port < 1025 || port > 65535){
                System.err.println("Error: ports available only from 1025 to 65534");
            }
        }while(port < 1025 || port > 65534);

        System.out.println(Constants.ANSI_GREEN + "Starting the server" + Constants.ANSI_RESET);
        Server server = new Server();

        ExecutorService ex = Executors.newCachedThreadPool();
        ex.submit(server.socketServer);

    }




}
