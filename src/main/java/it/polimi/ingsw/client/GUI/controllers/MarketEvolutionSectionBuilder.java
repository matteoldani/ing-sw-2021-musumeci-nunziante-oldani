package it.polimi.ingsw.client.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MarketEvolutionSectionBuilder {

    @FXML protected Sphere zerozero;
    @FXML protected Sphere zerouno;
    @FXML protected Sphere zerodue;
    @FXML protected Sphere zerotre;
    //riga 1
    @FXML protected Sphere unozero;
    @FXML protected Sphere unouno;
    @FXML protected Sphere unodue;
    @FXML protected Sphere unotre;
    //riga 2
    @FXML protected Sphere duezero;
    @FXML protected Sphere dueuno;
    @FXML protected Sphere duedue;
    @FXML protected Sphere duetre;

    @FXML protected Sphere external;

    //Market legend
    @FXML protected Sphere coin;
    @FXML protected Sphere shield;
    @FXML protected Sphere rock;
    @FXML protected Sphere servant;
    @FXML protected Sphere faith;
    @FXML protected Sphere nothing;

    //Evolution Section
    @FXML protected ImageView eCard_00;//row 0 , column 0
    @FXML protected ImageView eCard_01;//row 0 , column 1
    @FXML protected ImageView eCard_02;//row 0 , column 2
    @FXML protected ImageView eCard_03;//row 0 , column 3
    @FXML protected ImageView eCard_10;//row 1 , column 0
    @FXML protected ImageView eCard_11;//row 1 , column 1
    @FXML protected ImageView eCard_12;//row 1 , column 2
    @FXML protected ImageView eCard_13;//row 1 , column 3
    @FXML protected ImageView eCard_20;//row 2 , column 0
    @FXML protected ImageView eCard_21;//row 2 , column 1
    @FXML protected ImageView eCard_22;//row 2 , column 2
    @FXML protected ImageView eCard_23;//row 2 , column 3

    @FXML
    protected ImageView eCardView_00;//row 0 , column 0
    @FXML
    protected ImageView eCardView_01;//row 0 , column 1
    @FXML
    protected ImageView eCardView_02;//row 0 , column 2
    @FXML
    protected ImageView eCardView_03;//row 0 , column 3
    @FXML
    protected ImageView eCardView_10;//row 1 , column 0
    @FXML
    protected ImageView eCardView_11;//row 1 , column 1
    @FXML
    protected ImageView eCardView_12;//row 1 , column 2
    @FXML
    protected ImageView eCardView_13;//row 1 , column 3
    @FXML
    protected ImageView eCardView_20;//row 2 , column 0
    @FXML
    protected ImageView eCardView_21;//row 2 , column 1
    @FXML
    protected ImageView eCardView_22;//row 2 , column 2
    @FXML
    protected ImageView eCardView_23;//row 2 , column 3

    protected void fillMarket(Sphere[][] market){

        market[0][0] = zerozero;
        market[0][1] = zerouno;
        market[0][2] = zerodue;
        market[0][3] = zerotre;
        market[1][0] = unozero;
        market[1][1] = unouno;
        market[1][2] = unodue;
        market[1][3] = unotre;
        market[2][0] = duezero;
        market[2][1] = dueuno;
        market[2][2] = duedue;
        market[2][3] = duetre;
    }

    protected void fillEvolutionSection(ArrayList<ArrayList<ImageView>> eCards) {

        ArrayList<ImageView> cards1 = new ArrayList<>(Arrays.asList(eCardView_00, eCardView_01, eCardView_02, eCardView_03));
        eCards.add(0, cards1);
        ArrayList<ImageView> cards2 = new ArrayList<>(Arrays.asList(eCardView_10, eCardView_11, eCardView_12, eCardView_13));
        eCards.add(1, cards2);
        ArrayList<ImageView> cards3 = new ArrayList<>(Arrays.asList(eCardView_20, eCardView_21, eCardView_22, eCardView_23));
        eCards.add(2, cards3);

    }

}

