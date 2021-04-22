package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.Player;

import java.util.HashMap;

public class TemporaryDoActionPlayer extends DoAction {

    public TemporaryDoActionPlayer(Game modelGame) {
        this.modelGame = modelGame;
    }

    /**
     * Method that discard a leader card invoking discardLeaderCard in activePlayer object
     * @param position is the leader card the activePlayer wants to discard
     */
    public void discardLeaderCard(int position){
        try {
            modelGame.getActivePlayer().discardLeaderCard(position);
            //I'n not sure moveCross() is useful -> but it will be because every time the position is increased
            //                                      we need to check if the player arrived in a popePosition or
            //                                      in new point position in order to increase the score
            //To do so, moveCross should have as parameters: player , increment
            for(Player player : modelGame.getPlayers()){
                if(!player.equals(modelGame.getActivePlayer()))
                    player.getDashboard().getPopeTrack().updateGamerPosition(1);
            }
        }catch(OutOfBandException | LeaderCardAlreadyUsedException | ExcessOfPositionException e){
            e.getLocalizedMessage();
        }
    }

    /**
     * Method that activate the production zone specified
     * @param position is which production zone the user wants to activate
     */
    public void activeProductionZone(int position){
        //When the turn ends it's necessary to set false the attribute isActive in every evolutionCard in the top of each zone
        //and set NOTHING the attribute isActionChose in Player
        if(modelGame.getActivePlayer().getActionChose() != Action.NOTHING &&
                modelGame.getActivePlayer().getActionChose() != Action.ACTIVE_PRODUCTION){
            //I should do this control in the method that decide which action the player chose
            //---> method doAction() in TurnHandler
        }
        if(modelGame.getActivePlayer().getPossibleActiveProductionZone()[position]){//if can be activated

            //active the production of the last evolutionCard in the productionZone specified by position
            modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard().setActive(true);

            //take the card activated by the player
            EvolutionCard eCard = modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard();

            //take the requires and the products
            HashMap<Resource , Integer> requires = eCard.getRequires();
            HashMap<Resource , Integer> products = eCard.getProduction();

            //Remove resource from the stock
            int numOfBox = modelGame.getActivePlayer().getDashboard().getStock().getNumberOfBoxes();
            for(Resource resource : requires.keySet()){
                int numOfResources = requires.get(resource);//number of resources to use of type resource
                if(numOfResources > 0){
                    if(modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource) >= numOfResources){
                        try{
                            modelGame.getActivePlayer().getDashboard().getStock().useResources(numOfResources , resource);
                        }catch(NotEnoughResourcesException e){
                            //It's impossible be here
                            e.getLocalizedMessage();
                        }
                    }
                    //this for is necessary in case of boxPlus because the player can have 2 resources in a standard box
                    //and 2 resources in a plus box
                    /*for(int i = 0; i < numOfBox && numOfResources > 0; i++){
                        if(modelGame.getActivePlayer().getDashboard().getStock().getResourceType(i) == resource){
                            if(modelGame.getActivePlayer().getDashboard().getStock().getQuantities(i) > numOfResources){
                                try {
                                    modelGame.getActivePlayer().getDashboard().getStock().useResources(i , numOfResources);
                                    numOfResources = 0;
                                }catch(NotEnoughResourcesException | OutOfBandException e){
                                    e.getLocalizedMessage();
                                }
                            }
                            else
                            {
                                numOfResources -= modelGame.getActivePlayer().getDashboard().getStock().getQuantities(i);
                                try {
                                    modelGame.getActivePlayer().getDashboard().getStock().useResources(i , modelGame.getActivePlayer().getDashboard().getStock().getQuantities(i));
                                }catch(NotEnoughResourcesException | OutOfBandException e){
                                    e.getLocalizedMessage();
                                }
                            }
                        }
                    }*/
                }
            }
            //Add resources to the lockBox
            for(Resource resource : products.keySet()){
                try {
                    if(!resource.equals(Resource.FAITH))
                        modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , products.get(resource));
                    else
                        //Here with moveCross there will be a control of the position
                        modelGame.getActivePlayer().getDashboard().getPopeTrack().updateGamerPosition(products.get(resource));
                }catch(NotEnoughResourcesException e){
                    //theoretically it's impossible be here -> only increase the number of resources here
                    e.getLocalizedMessage();
                }catch(ExcessOfPositionException e){
                    e.getLocalizedMessage();
                }
            }
            //Set which action the player chose only if the action is been completed
            modelGame.getActivePlayer().setActionChose(Action.ACTIVE_PRODUCTION);
        }
    }

    /**
     * Method that read the required of the card the player wants to buy and ask him from here take these resources
     * @param row of the evolutionSection
     * @param col od the evolutionSection
     */
    public void prepareBuyEvolutionCard(int row , int col){
        //Check if the player can buy this card
        if(modelGame.getActivePlayer().getPossibleEvolutionCard()[row][col]){

            //Read the card the player wants to buy
            EvolutionCard eCard = modelGame.getEvolutionSection().canBuy()[row][col];

            //Read the cost of eCard
            HashMap<Resource , Integer> cost = eCard.getCost();

            //Call a method that ask the player from where and how many resources he wants to take from
            //  LockBox and Stock
        }
    }

    /**
     * Method that buy a card, use the resources and place the card
     * @param row row of the evolutionSection
     * @param col column of the evolutionSection
     * @param position is in which productionZone the player wants to place the card
     * @param howManyFromLockBox how many resources take from the LockBox
     * @param howManyFromStock how many resources  take from the Stock
     */
    public void buyEvolutionCard(int row, int col , int position ,
                                 HashMap<Resource , Integer> howManyFromLockBox , HashMap<Resource , Integer> howManyFromStock) {

        //Check if the player can buy this card
        if(modelGame.getActivePlayer().getPossibleEvolutionCard()[row][col]){

            //Read the card the player wants to buy
            EvolutionCard eCard = modelGame.getEvolutionSection().canBuy()[row][col];

            if(modelGame.getActivePlayer().getPossibleProductionZone(eCard)[position]){
                //The card cannot be placed in position
                //Ask again the user where place the card
            }

            //Read the cost of eCard
            HashMap<Resource , Integer> cost = eCard.getCost();

            //Control if the amount od resources to take are right
            //I know that the player has enough resources to buy the card, otherwise the if above give false
            //->I can control only one of the 2 hashMap
            //Control the LockBox -> it's the easier
            for(Resource res : howManyFromLockBox.keySet()){
                int difference = modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(res) - howManyFromLockBox.get(res);
                if(difference < 0){
                    //Increase the amount of resources to take from the Stock
                    //difference is negative
                    howManyFromStock.put(res , howManyFromStock.get(res) - difference);

                    //Check if the number of resources are not more than the required
                    if(howManyFromStock.get(res) > cost.get(res))
                        howManyFromStock.put(res , cost.get(res));
                }
                //Check id the total resources from Stock and LockBox are equal to the cost
                if(howManyFromStock.get(res) + howManyFromLockBox.get(res) == cost.get(res))
                    break;

                //Send an error and ask the player to say again the amount od resources
            }

            //Remove resources from LockBox and Stock
            for(Resource resource : howManyFromLockBox.keySet()){
                    //From LockBox
                    try{
                        //To check if the amount of resources in the hashMap are positive or negative
                        modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , -howManyFromLockBox.get(resource));
                    }catch(NotEnoughResourcesException e){
                        //Theoretically it's impossible be here
                        e.getLocalizedMessage();
                    }
                    //From Stock
                    try{
                        modelGame.getActivePlayer().getDashboard().getStock().useResources(howManyFromStock.get(resource) , resource);
                    }catch(NotEnoughResourcesException e){
                        //Theoretically it's impossible be here
                        e.getLocalizedMessage();
                    }
            }
            //Buy the card and place it
            try{
                EvolutionCard cardBought = modelGame.getEvolutionSection().buy(row , col);
                modelGame.getActivePlayer().getDashboard().getProductionZone()[position].addCard(cardBought);
            }catch(InvalidPlaceException | ExcessOfPositionException e){
                //Theoretically it's impossible be here
                e.getLocalizedMessage();
            }

            //Set the action done in player
            modelGame.getActivePlayer().setActionChose(Action.BUY_CARD);
        }

    }
}
