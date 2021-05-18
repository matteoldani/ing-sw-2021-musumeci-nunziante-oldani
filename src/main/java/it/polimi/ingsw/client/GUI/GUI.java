package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.client.GUI.controllers.Controller;
import it.polimi.ingsw.client.GUI.controllers.InitialResourcesConfigurationController;
import it.polimi.ingsw.client.GUI.controllers.LeaderCardsConfigurationController;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI extends Application implements UI {

    //list of file .fxml
    public static final String IP_PORT = "ip_port_configuration.fxml";
    public static final String NICKNAME = "nickname_configuration.fxml";
    public static final String PLAYERS = "players_configuration.fxml";
    public static final String LEADER_CARD = "leader_cards_configuration.fxml";
    public static final String INITIAL_RESOURCES = "initial_resources_configuration.fxml";
    public static final String WAITING_ROOM = "waiting.fxml";
    public static final String START_GAME = "view.fxml";

    private Scene currentScene;
    private Scene oldScene;
    private Stage currentStage;
    private final HashMap<String, Scene> scenes;
    private final HashMap<String, Controller> controllers;
    private final HashMap<Scene, GamePhases> phases;
    private final HashMap<GamePhases,String> fxmls;

    private View view;
    private ClientSocket clientSocket;
    private Socket socket;
    private GamePhases gamePhase;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private ArrayList<Resource> resources;
    private String nickname;
    private boolean isAckArrived;
    private boolean isNackArrived;
    private String errorFromServer;

    public GUI() {
        gamePhase = GamePhases.IINITIALIZATION;
        scenes = new HashMap<>();
        controllers = new HashMap<>();
        phases = new HashMap<>();
        fxmls = new HashMap<>();
        errorFromServer="";
    }

    public void initializationFXMLParameter() {
        List<String> fxmlFiles = new ArrayList<>(Arrays.asList(START_GAME, PLAYERS, IP_PORT, LEADER_CARD, WAITING_ROOM, NICKNAME, INITIAL_RESOURCES));
        try {
            for (String path : fxmlFiles) {
                URL url = new File("src/main/resources/fxml/" + path).toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Scene scene = new Scene(loader.load());
                scenes.put(path, scene);
                Controller controller = loader.getController();
                controller.setGui(this);
                controllers.put(path, controller);
                phases.put(scene, linkFXMLPageToPhase(path));
                fxmls.put(linkFXMLPageToPhase(path),path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = scenes.get(IP_PORT);
    }

    public static void main(String[] args) {
        new GUI();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.currentStage = primaryStage;
        initializationFXMLParameter();
        initializationStage();
    }

    public void initializationStage() {
        currentStage.setTitle("Masters of Renaissance");
        currentStage.setScene(currentScene);
        currentStage.show();
    }

    public void changeScene() {

        Platform.runLater(()->{

                //ack e nack inutili
                isAckArrived=false;
                isNackArrived=false;
                currentStage.setScene(currentScene);
                Controller controller = controllers.get(fxmls.get(gamePhase));
                controller.init();
                errorFromServer="";
                currentStage.show();
        });
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    private GamePhases linkFXMLPageToPhase(String NAME) {
        switch (NAME) {
            case (NICKNAME):
                return GamePhases.NICKNAME;
            case (PLAYERS):
                return GamePhases.NUMBEROFPLAYERS;
            case (LEADER_CARD):
                return GamePhases.INITIALLEADERCARDSELECTION;
            case (INITIAL_RESOURCES):
                return GamePhases.INITIALRESOURCESELECTION;
            case (START_GAME):
                return GamePhases.STARTGAME;
            case (WAITING_ROOM):
                return GamePhases.WAITINGOTHERPLAYERS;
            default:
                return GamePhases.IINITIALIZATION;
        }
    }

    public Scene getOldScene(){
        return oldScene;
    }

    public void setOldScene(Scene scene) {
        oldScene=scene;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public GamePhases phase(Scene scene) {
        return phases.get(scene);
    }

    public String getErrorFromServer() {
        return errorFromServer;
    }

    public void setErrorFromServer(String errorFromServer) {
        this.errorFromServer = errorFromServer;
    }

    public boolean isAckArrived() {
        return isAckArrived;
    }

    public void setAckArrived(boolean ackArrived) {
        isAckArrived = ackArrived;
    }

    public boolean isNackArrived() {
        return isNackArrived;
    }

    public void setNackArrived(boolean nackArrived) {
        isNackArrived = nackArrived;
    }

    public void setSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
    }

    public void setClientSocket() {
        try {
            clientSocket = new ClientSocket(this, socket);
        } catch (IOException e) {
            clientSocket = null;
        }
    }

    public void setGamePhase(GamePhases phase) {
        gamePhase = phase;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Scene getScene(String NAME) {
        return scenes.get(NAME);
    }

    public Controller getController(String NAME) {
        return controllers.get(NAME);
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}