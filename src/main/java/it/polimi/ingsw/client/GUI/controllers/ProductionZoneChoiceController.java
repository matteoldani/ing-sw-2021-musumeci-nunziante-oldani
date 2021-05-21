package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductionZoneChoiceController implements Controller{

    private GUI  gui;
    private Print printer;

    @FXML private ImageView evolutionCard_0;//evolutionCard on the production zone 0
    @FXML private ImageView evolutionCard_1;//evolutionCard on the production zone 1
    @FXML private ImageView evolutionCard_2;//evolutionCard on the production zone 2

    private ArrayList<ImageView> eCards;

    @FXML private Button confirmPosition;//position chose -> send the message to the server
    @FXML private Button cancelPosition;//turn back to startGame

    @FXML private RadioButton production0;//select the production zone 0
    @FXML private RadioButton production1;//select the production zone 1
    @FXML private RadioButton production2;//select the production zone 2

    private ArrayList<RadioButton> buttons;

    @FXML private Label error;


    public ProductionZoneChoiceController(){
        this.printer = new Print();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    private void initProductionZones(){
        SerializableProductionZone[] productionZones = gui.getView().getDashboard().getSerializableProductionZones();

        //Take the top cards not null and fill the view with the images
        for(int i = 0 ; i < productionZones.length ; i++){
            if(productionZones[i] != null && productionZones[i].getCards().get(i) != null){
                eCards.get(i).setImage(printer.fromPathToImageEvolution(productionZones[i].getCards().get(i).getId()));
            }
            else{
                //Don't show the card
                eCards.get(i).setVisible(false);
            }
        }
    }

    @Override
    public void init() {
        //Show the error if present
        if(gui.getErrorFromServer() != null && !gui.getErrorFromServer().equals("")){
            error.setText(gui.getErrorFromServer());
        }

        eCards = new ArrayList<ImageView>(Arrays.asList(evolutionCard_0 , evolutionCard_1 , evolutionCard_2));
        buttons = new ArrayList<RadioButton>(Arrays.asList(production0 , production1 , production2));

        //Show buttons
        for(RadioButton button : buttons)
            button.setVisible(true);

        confirmPosition.setVisible(true);
        cancelPosition.setVisible(true);

        initProductionZones();
    }


    public void confirmPositionChoice(){
        int position = -1;

        //Check which production zone the player chose
        for(int i = 0 ; i < buttons.size() ; i++){
            if(buttons.get(i).isSelected()){
                position = i;
                break;
            }
        }

        //Check if a position has been chosen
        if(position == -1){
            error.setText("Choose a position or turn back with Cancel button");
            return;
        }

        //Send the message -> need to add 2 attributes in gui
        //gui.getClientSocket().send(new BuyEvolutionCardMessage("Card bought" , gui.getRow() , gui.getColumn()));
        //If ack -> set that the action has been chosen

        //Turn back to the start game scene
        gui.setCurrentScene(gui.getScene(GUI.START_GAME));
        gui.setOldScene(gui.getScene(GUI.START_GAME));//Maybe this will be change -> now there isn't this scene
        gui.setGamePhase(GamePhases.STARTGAME);
    }

    /**
     * Turn back to START_GAME scene
     */
    public void cancel() {
        gui.setCurrentScene(gui.getScene(GUI.START_GAME));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }
}