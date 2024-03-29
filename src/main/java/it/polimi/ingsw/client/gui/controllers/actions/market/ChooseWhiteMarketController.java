package it.polimi.ingsw.client.gui.controllers.actions.market;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.controllers.Controller;
import it.polimi.ingsw.client.gui.controllers.utils.Print;
import it.polimi.ingsw.client.gui.GameFxml;
import it.polimi.ingsw.client.gui.GamePhases;
import it.polimi.ingsw.model.game.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class that allows the user to choose with which resources to transform the white marbles obtained
 */
public class ChooseWhiteMarketController implements Controller {

    private GUI gui;
    private final Print printer = new Print();
    ArrayList<Resource> resource;
    //variable integer that store how many white marbles are still to be transformed
    int numberWhite;
    //variable integer that keeps in memory how many white marbles have been taken
    int numberWhiteFixed;
    ArrayList<Circle> whiteBalls;
    //First active leader card with NOMOREWHITE power
    Resource resourceOne;
    //Second active leader card with NOMOREWHITE power
    Resource resourceTwo;

    @FXML
    private ImageView resource1;
    @FXML
    private ImageView resource2;
    @FXML
    private Circle white1;
    @FXML
    private Circle white2;
    @FXML
    private Circle white3;
    @FXML
    private Circle white4;
    @FXML
    private Button plus1;
    @FXML
    private Text error;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void init() {

        whiteBalls = new ArrayList<>(Arrays.asList(white1, white2, white3, white4));

        for(Circle circle: whiteBalls){
            circle.setFill(Color.WHITE);
            circle.setVisible(false);
        }

        resource = gui.getView().getResourcesBoughtFromMarker();

        int i = 0;
        for (Resource res : resource) {
            if (res.equals(Resource.NOTHING)) {
                whiteBalls.get(i).setVisible(true);
                i++;
            }
        }
        numberWhite = i;
        numberWhiteFixed = i;
        HashMap<Resource, Integer> resourcesWhite = gui.getView().getLeaderCards().get(0).getAbilityResource();

        //save the resource that will have to replace the white marbles
        Resource resourceWhite = null;

        for (Resource res : resourcesWhite.keySet()) {
            if (resourcesWhite.get(res) == 1) {
                resourceWhite = res;
            }
        }
        resourceOne = resourceWhite;
        String path = printer.pathFromResource(resourceWhite);
        resource1.setImage(printer.fromPathToImageResource(path));

        resourcesWhite = gui.getView().getLeaderCards().get(1).getAbilityResource();

        //save the resource that will have to replace the white marbles
            Resource resourceWhite2 = null;

        for (Resource res : resourcesWhite.keySet()) {
            if (resourcesWhite.get(res) == 1) {
                resourceWhite2 = res;
            }
        }
        resourceTwo = resourceWhite2;
        path = printer.pathFromResource(resourceWhite2);
        resource2.setImage(printer.fromPathToImageResource(path));
        if(numberWhite==0){
            error.setText("No white ball obtained, go ahead");
            error.setVisible(true);
        }
    }

    /**
     * Method that checks that the user has correctly replaced all the white marbles and sends the choice to the server
     */
    public void confirm() {
        try {

            if (numberWhite == 0) {
                gui.getView().setResourcesBoughtFromMarker(resource);
                gui.setCurrentScene(gui.getScene(GameFxml.STORE_RESOURCES.s));
                gui.setOldScene(gui.getScene(GameFxml.CHOOSE_WHITE_RESOURCES.s));
                gui.setGamePhase(GamePhases.STORERESOURCES);
                gui.changeScene();
            } else {
                error.setText("All white balls have to be replaced!");
                error.setVisible(true);
            }
        }catch(Exception e){
        e.printStackTrace();}
    }

    /**
     * Method that graphically transforms a white marble into the color of the chosen resource and store user choice
     * @param actionEvent button clicked
     */
    public void plus(ActionEvent actionEvent) {

        try{
        Button button = (Button) actionEvent.getSource();
        if (numberWhite != 0) {
            if (button.equals(plus1)) {
                whiteBalls.get(numberWhiteFixed - numberWhite).setFill(printer.colorFromResource(resourceOne));
                resource.remove(Resource.NOTHING);
                resource.add(resourceOne);
            } else {
                whiteBalls.get(numberWhiteFixed - numberWhite).setFill(printer.colorFromResource(resourceTwo));
                resource.remove(Resource.NOTHING);
                resource.add(resourceTwo);
            }
            numberWhite--;
        } else {
            error.setText("All white balls are replaced");
            error.setVisible(true);
        }
    }catch(Exception e){
        e.printStackTrace();}
    }
}
