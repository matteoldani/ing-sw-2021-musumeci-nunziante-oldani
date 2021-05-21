package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.GUI.controllers.utils.Print;
import it.polimi.ingsw.client.GamePhases;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.ActiveProductionMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.DiscardLeaderCardMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.UseLeaderCardMessage;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.serializableModel.SerializableProductionZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ViewController implements Controller{

    private GUI gui;
    private Print printer;
    private ArrayList<Integer> stockLeaderCardInUse;
    private ArrayList<ImageView> popeTrackPositions;
    private int leaderWaitForAck;
    private ArrayList<ImageView>[] productionZones;
    private boolean[] marketLeaderActive;
    private boolean activeBasic;
    private ArrayList<Integer> productionPositions;

    //market
    //riga 0
    @FXML private Circle zerozero;
    @FXML private Circle zerouno;
    @FXML private Circle zerodue;
    @FXML private Circle zerotre;
    //riga 1
    @FXML private Circle unozero;
    @FXML private Circle unouno;
    @FXML private Circle unodue;
    @FXML private Circle unotre;
    //riga 2
    @FXML private Circle duezero;
    @FXML private Circle dueuno;
    @FXML private Circle duedue;
    @FXML private Circle duetre;

    @FXML private VBox col0;
    @FXML private VBox col1;
    @FXML private VBox col2;
    @FXML private VBox col3;

    @FXML private HBox Market;
    @FXML private Circle external;


    //leader card
    @FXML private ImageView leader1;
    @FXML private ImageView leader2;
    @FXML private VBox LeaderBox1;
    @FXML private VBox LeaderBox2;
    @FXML private Button active1;
    @FXML private Button active2;
    @FXML private Button use1;
    @FXML private Button use2;
    @FXML private Button discard1;
    @FXML private Button discard2;
    @FXML private Button market;
    @FXML private ImageView inkwell;

    //popeTrack
    @FXML private ImageView pos0;
    @FXML private ImageView pos1;
    @FXML private ImageView pos2;
    @FXML private ImageView pos3;
    @FXML private ImageView pos4;
    @FXML private ImageView pos5;
    @FXML private ImageView pos6;
    @FXML private ImageView pos7;
    @FXML private ImageView pos8;
    @FXML private ImageView pos9;
    @FXML private ImageView pos10;
    @FXML private ImageView pos11;
    @FXML private ImageView pos12;
    @FXML private ImageView pos13;
    @FXML private ImageView pos14;
    @FXML private ImageView pos15;
    @FXML private ImageView pos16;
    @FXML private ImageView pos17;
    @FXML private ImageView pos18;
    @FXML private ImageView pos19;
    @FXML private ImageView pos20;
    @FXML private ImageView pos21;
    @FXML private ImageView pos22;
    @FXML private ImageView pos23;
    @FXML private ImageView pos24;
    @FXML private ImageView popeCard1;
    @FXML private ImageView popeCard2;
    @FXML private ImageView popeCard3;

    //stock
    @FXML private ImageView stockBox1;
    @FXML private ImageView stockBox21;
    @FXML private ImageView stockBox22;
    @FXML private ImageView stockBox31;
    @FXML private ImageView stockBox32;
    @FXML private ImageView stockBox33;
    @FXML private ImageView stockPlus11;
    @FXML private ImageView stockPlus12;
    @FXML private ImageView stockPlus21;
    @FXML private ImageView stockPlus22;

    //lockbox
    @FXML private Text coinQuantity;
    @FXML private Text shieldQuantity;
    @FXML private Text rockQuantity;
    @FXML private Text servantQuantity;

    //produczion zones
    @FXML private ImageView production1;
    @FXML private ImageView production2;
    @FXML private ImageView production3;
    @FXML private ImageView production11;
    @FXML private ImageView production21;
    @FXML private ImageView production31;
    @FXML private ImageView production12;
    @FXML private ImageView production22;
    @FXML private ImageView production32;
    @FXML private CheckBox activeProduction1;
    @FXML private CheckBox activeProduction2;
    @FXML private CheckBox activeProduction3;
    @FXML private Button activeProductions;
    @FXML private Button basicProduction;


    public ViewController(){
        this.printer = new Print();
        stockLeaderCardInUse= new ArrayList<>();
        marketLeaderActive = new boolean[2];
        marketLeaderActive[0]=false;
        marketLeaderActive[1]=false;
        activeBasic=false;
        productionPositions=new ArrayList<>();
    }

    @FXML
    private void showMarket(){
        gui.setCurrentScene(gui.getScene(GUI.MARKET));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.BUYFROMMARKET);
        gui.changeScene();
    }

    private void initLeaderCards(){

        //set leader card
        ArrayList<SerializableLeaderCard> leaderCards = gui.getView().getLeaderCards();
        if(leaderCards!= null && leaderCards.size()>0){

            String path = String.valueOf(leaderCards.get(0).getId());
            leader1.setImage(printer.fromPathToImageLeader(path));
            leader1.setCache(true);

            if(leaderCards.get(0).isActive()){
                discard1.setVisible(false);
                active1.setVisible(false);
                use1.setVisible(true);
            }

            if(leaderCards.size()>1) {
                path = String.valueOf(leaderCards.get(1).getId());
                leader2.setImage(printer.fromPathToImageLeader(path));
                leader2.setCache(true);

                if (leaderCards.get(1).isActive()) {
                    discard1.setVisible(false);
                    active1.setVisible(false);
                    use1.setVisible(true);
                }
            }
        }
        if(gui.getGamePhase()==GamePhases.ASKACTIVELEADER && gui.isAckArrived()){
            activeLeaderACK();
            gui.setGamePhase(GamePhases.STARTGAME);
        }
        //NOTIFICO SE è ARRIVATO IL NACK?
        /*
        gui.getGamePhase()==GamePhases.ASKACTIVELEADER && gui.isAckArrived()
         */
    }

    private void initMarket(){

        //initialize market balls

        Resource[][] market = gui.getView().getMarket().getMarket();
        zerozero.setFill(printer.colorFromResource(market[0][0]));
        zerouno.setFill(printer.colorFromResource(market[0][1]));
        zerodue.setFill(printer.colorFromResource(market[0][2]));
        zerotre.setFill(printer.colorFromResource(market[0][3]));

        unozero.setFill(printer.colorFromResource(market[1][0]));
        unouno.setFill(printer.colorFromResource(market[1][1]));
        unodue.setFill(printer.colorFromResource(market[1][2]));
        unotre.setFill(printer.colorFromResource(market[1][3]));

        duezero.setFill(printer.colorFromResource(market[2][0]));
        dueuno.setFill(printer.colorFromResource(market[2][1]));
        duedue.setFill(printer.colorFromResource(market[2][2]));
        duetre.setFill(printer.colorFromResource(market[2][3]));

        external.setFill(printer.colorFromResource(gui.getView().getMarket().getExternalResource()));
    }

    private void initInkwell(){
        if(gui.getView().getDashboard().isInkwell()){
            inkwell.setVisible(true);
        }
    }

    /**
     * clear position, set gamer position and if is a solo game set also lorenzo position
     */
    private void initPopeTrack(){

        popeTrackPositions = new ArrayList<>(Arrays.asList(pos0,pos1,pos2,pos3,pos4,pos5,pos6,pos7,pos8,pos9,pos10,pos11,pos12,pos13,pos14,pos15,pos16,pos17,pos18,pos19,pos20,pos21,pos22,pos23,pos24));

        for (ImageView image: popeTrackPositions) {
            image.setImage(null);
        }

        int position = gui.getView().getDashboard().getSerializablePopeTack().getPosition();

        if(gui.getPlayers()==1){
            int lorenzoPosition = gui.getView().getDashboard().getSerializablePopeTack().getLorenzoPosition();
            if(position==lorenzoPosition){
                popeTrackPositions.get(lorenzoPosition).setImage(printer.togetherPopePosition());
            }
            popeTrackPositions.get(lorenzoPosition).setImage(printer.lorenzoPopePosition());
        }
        popeTrackPositions.get(position).setImage(printer.popePosition());
    }

    /**
     * if stock boxes are not empty fill them with the correct type of resource
     */
    private void initStock() {

        Resource[] box1 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(0);

        if (box1[0] != null) {
            String path = printer.pathFromResource(box1[0]);
            stockBox1.setImage(printer.fromPathToImageResource(path));
        }

        Resource[] box2 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(1);

        if (box2[0] != null) {
            String path = printer.pathFromResource(box2[0]);
            stockBox21.setImage(printer.fromPathToImageResource(path));
        }
        if (box2[1] != null) {
            String path = printer.pathFromResource(box2[1]);
            stockBox22.setImage(printer.fromPathToImageResource(path));
        }

        Resource[] box3 = gui.getView().getDashboard().getSerializableStock().getBoxes().get(2);

        if (box3[0] != null) {
            String path = printer.pathFromResource(box3[0]);
            stockBox31.setImage(printer.fromPathToImageResource(path));
        }
        if (box3[1] != null) {
            String path = printer.pathFromResource(box3[1]);
            stockBox32.setImage(printer.fromPathToImageResource(path));
        }
        if (box3[2] != null) {
            String path = printer.pathFromResource(box3[3]);
            stockBox33.setImage(printer.fromPathToImageResource(path));
        }

        if (stockLeaderCardInUse != null && stockLeaderCardInUse.size() != 0) {
            int leaderPosition = stockLeaderCardInUse.get(0);

            //Resource resource = gui.getView().getDashboard().getSerializableStock().getResourcesPlus().get(0);

            //devo controllare che in una data carta leader posso mettere effettivamente la risorsa ?

            if (leaderPosition == 1) {
                //devo mettere le risorse dello stockbox plus sulla prima leader card
                if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                        String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                        stockPlus11.setImage(printer.fromPathToImageResource(path));
                    }
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                        String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                        stockPlus21.setImage(printer.fromPathToImageResource(path));
                    }
                } else {
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus11.setImage(printer.fromPathToImageResource(path));
                        }
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus22.setImage(printer.fromPathToImageResource(path));
                        }
                    }
                }
            }
            if (stockLeaderCardInUse.size() > 1) {
                leaderPosition = stockLeaderCardInUse.get(1);

                if (leaderPosition == 1) {
                    //devo mettere le risorse dello stockbox plus sulla prima leader card
                    if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus11.setImage(printer.fromPathToImageResource(path));
                        }
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                            String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                            stockPlus21.setImage(printer.fromPathToImageResource(path));
                        }
                    } else {
                        if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0).length != 0) {
                            if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0] != null) {
                                String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                                stockPlus11.setImage(printer.fromPathToImageResource(path));
                            }
                            if (gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[1] != null) {
                                String path = printer.pathFromResource(gui.getClientSocket().getView().getDashboard().getSerializableStock().getBoxPlus().get(0)[0]);
                                stockPlus22.setImage(printer.fromPathToImageResource(path));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * set the amount of each resource contained in lockbox
     */
    private void initLockBox(){
        HashMap<Resource,Integer> lockbox = gui.getView().getDashboard().getSerializableLockBox().getResources();
        coinQuantity.setText(String.valueOf(lockbox.get(Resource.COIN)));
        shieldQuantity.setText(String.valueOf(lockbox.get(Resource.SHIELD)));
        servantQuantity.setText(String.valueOf(lockbox.get(Resource.SERVANT)));
        rockQuantity.setText(String.valueOf(lockbox.get(Resource.ROCK)));
    }

    public void useLeader(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        //stockbox plus --> always in use
        //sale--> set in use and modify evolution section --> maybe show your sale?
        //production zone --> if ypu click in use, include that position in active production
        //market--> if in use modify white balls

        if(button.equals(use1)){
            gui.getClientSocket().send(new UseLeaderCardMessage("Use leader card", 0));
            if(gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.NOMOREWHITE)){
                marketLeaderActive[0]=true;
            }
            if(gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                productionPositions.add(3);
            }

        }else{
            //se ho scartato la prima leader card, la seconda slitta in prima posizione
            if (gui.getView().getLeaderCards().size()==1){
                gui.getClientSocket().send(new UseLeaderCardMessage("Use leader card", 0));

                if(gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.NOMOREWHITE)){
                    marketLeaderActive[0]=true;
                }
                if(gui.getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                   productionPositions.add(3);
                }
            }else {
                gui.getClientSocket().send(new UseLeaderCardMessage("Use leader card", 1));
                if(gui.getView().getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.NOMOREWHITE)){
                    marketLeaderActive[1]=true;
                }
                if(gui.getView().getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.PRODUCTIONPOWER)){
                    productionPositions.add(4);
                }
            }
        }
    }

    /**
     * user ask to active a leader card.
     * If server answer with ACK means that user effectively have necessary resources for the activation and so
     * parameter could be update.
     */
    public void activeLeaderACK() {
        if (leaderWaitForAck==1) {
            active1.setVisible(false);
            discard1.setVisible(false);
            use1.setVisible(true);
            if (gui.getClientSocket().getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                stockLeaderCardInUse.add(1);
                use1.setVisible(false);
            }

        } else {
            active2.setVisible(false);
            discard2.setVisible(false);
            use2.setVisible(false);
            if (gui.getView().getLeaderCards().size() == 1) {
                if (gui.getClientSocket().getView().getLeaderCards().get(0).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                    stockLeaderCardInUse.add(2);
                    use2.setVisible(false);
                }
            } else {
                if (gui.getClientSocket().getView().getLeaderCards().get(1).getAbilityType().equals(LeaderAbility.STOCKPLUS)) {
                    stockLeaderCardInUse.add(2);
                    use1.setVisible(false);
                }
            }
        }
    }

    public void activeLeader(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        if(button.equals(active1)){
            leaderWaitForAck=1;
            gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 0));
        }else{
            leaderWaitForAck=2;
            if (gui.getView().getLeaderCards().size()==1){
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 0));
            }else {
                gui.getClientSocket().send(new ActiveLeaderCardMessage("active leader card", 1));
            }
        }

        gui.setGamePhase(GamePhases.ASKACTIVELEADER);
    }

    public void discardLeader(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        if(button.equals(discard1)){
            active1.setVisible(false);
            discard1.setVisible(false);
            leader1.setVisible(false);
            gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));

        }else{
            active2.setVisible(false);
            discard2.setVisible(false);
            leader2.setVisible(false);
            if (gui.getView().getLeaderCards().size()==1){
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 0));
            }else {
                gui.getClientSocket().send(new DiscardLeaderCardMessage("discard leader card", 1));
            }
        }
        initPopeTrack();
    }

    private void initProductionZone() {

        productionZones = new ArrayList[3];
        for (int i = 0; i < productionZones.length; i++) {
            productionZones[i] = new ArrayList<>();
        }
        productionZones[0].addAll(Arrays.asList(production1, production2, production3));
        productionZones[1].addAll(Arrays.asList(production11, production21, production31));
        productionZones[2].addAll(Arrays.asList(production12, production22, production32));

        SerializableProductionZone[] serializableProductionZones = gui.getView().getDashboard().getSerializableProductionZones();

        for (int i = 0; i < serializableProductionZones.length; i++) {
            SerializableProductionZone serializableProductionZone = serializableProductionZones[i];
            if (serializableProductionZone.getCards() != null && serializableProductionZone.getCards().size() > 0) {
                for (EvolutionCard evolutionCard : serializableProductionZone.getCards()) {
                    if (evolutionCard.getLevel().getValue() == 1) {
                        productionZones[i].get(0).setImage(printer.fromPathToImageEvolution(evolutionCard.getId()));
                    } else if (evolutionCard.getLevel().getValue() == 2) {
                        productionZones[i].get(1).setImage(printer.fromPathToImageEvolution(evolutionCard.getId()));
                    } else {
                        productionZones[i].get(2).setImage(printer.fromPathToImageEvolution(evolutionCard.getId()));
                    }
                }
            }
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void init() {

        initMarket();
        initLeaderCards();
        initInkwell();
        initPopeTrack();
        initLockBox();
        initStock();
        initProductionZone();
    }

    public void activeProduction(ActionEvent actionEvent) {

        if(activeProduction1.isSelected())productionPositions.add(0);
        if(activeProduction2.isSelected())productionPositions.add(1);
        if(activeProduction3.isSelected())productionPositions.add(2);

        if(productionPositions.size()!=0 || activeBasic) {
            gui.getClientSocket().send(new ActiveProductionMessage("Active production zones", productionPositions, activeBasic, gui.getBasicRequires(), gui.getBasicEnsures()));
        }
        //gui.setGamePhase(GamePhases.ASKACTIVEPRODUCTION);
        //SE SONO IN QUESTA FASE E ARRIVA IL NACK FACCIO VEDERE UN MESSAGGIO??

        activeBasic=false;
        productionPositions.clear();
        gui.setBasicRequires(null);
        gui.setBasicEnsures(null);
        gui.setLeaderEnsure(null);
    }

    public void chooseBasicProduction(ActionEvent actionEvent) {
        activeBasic=true;
        gui.setCurrentScene(gui.getScene(GUI.BASIC_PRODUCTION));
        gui.setOldScene(gui.getScene(GUI.START_GAME));
        gui.setGamePhase(GamePhases.STARTGAME);
        gui.changeScene();
    }
}
