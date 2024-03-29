package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;

import it.polimi.ingsw.messages.Message;

import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.LorenzoPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.*;

/**
 * Class that contains the methods to update the model sequentially according to the action performed by the player.
 */
public class DoActionPlayer {

    private final Game modelGame;
    /**
     * Resources bought from market
     */
    private ArrayList<Resource> resourceList;
    TurnHandler turnHandler;

    public DoActionPlayer(Game modelGame, TurnHandler turnHandler) {
        this.modelGame = modelGame;
        this.turnHandler = turnHandler;
    }

    /**
     * This method manages the purchase of resources from market, updates the market board and the player's stock
     *
     * @param position indicates the row or column to be purchased
     * @param isRow    true if player chose a row, false otherwise
     */
    public void buyFromMarket(int position, boolean isRow) throws ExcessOfPositionException {

            //Purchase resources from market and updates the market board
            Resource[] resources = modelGame.getMarket().updateBoard(position, isRow);

            //check if leader card is in use and refactor array to arraylist
            resourceList = new ArrayList<>();
            Collections.addAll(resourceList,resources);

            //save resources in model section
            HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();
            //humanPlayer.setResources(resourceList);

            //Increase PopeTrackPosition if player got a faith ball
            if (resourceList.contains(Resource.FAITH)) {
                moveCross(1, new ArrayList<>() {{
                    add(modelGame.getActivePlayer());
                }});
                resourceList.remove(Resource.FAITH);
            }

            //remove white ball if NO_MORE_WHITE leader card is active
            int leaderCardActivated = 0;
            Resource resource = null;

            for (int i = 0; i < humanPlayer.getDashboard().getLeaderCards().size(); i++) {

                //For each leader card check if it has the vary power required and if it is active
                if (humanPlayer.getDashboard().getLeaderCards().get(i).getAbilityType().equals(LeaderAbility.NOMOREWHITE) &&
                        humanPlayer.getDashboard().getLeaderCards().get(i).isActive()) {

                    leaderCardActivated++;
                    HashMap<Resource, Integer> resourcesWhite = humanPlayer.getDashboard().getLeaderCards().get(i).getAbilityResource();

                    //If the leader card is active, save the resource type in which the white ball has to be transformed
                    for (Resource res : resourcesWhite.keySet()) {
                        if (resourcesWhite.get(res) == 1) {
                            resource = res;
                        }
                    }
                }
            }

            if (leaderCardActivated == 1) {
                ArrayList<Resource> resourcesCopy = new ArrayList<>(resourceList);

                for (Resource res : resourcesCopy) {
                    if (res.equals(Resource.NOTHING)) {
                        resourceList.remove(Resource.NOTHING);
                        resourceList.add(resource);
                    }
                }
            }

            humanPlayer.setResources(resourceList);
            ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_FROM_MARKET);
    }

    /**
     * Ask client which resources received from market he wants to store and after it's called this method
     */
    public Message storeResourcesBought(ArrayList<Resource> saveResources) {

        try{
            HumanPlayer humanPlayer = (HumanPlayer) modelGame.getActivePlayer();

            HashMap<Resource, Integer> oldResources = new HashMap<>();
            HashMap<Resource, Integer> newResources = new HashMap<>();

            oldResources.put(Resource.COIN, 0);
            oldResources.put(Resource.SERVANT, 0);
            oldResources.put(Resource.SHIELD, 0);
            oldResources.put(Resource.ROCK, 0);
            oldResources.put(Resource.NOTHING, 0);

            newResources.put(Resource.COIN, 0);
            newResources.put(Resource.SERVANT, 0);
            newResources.put(Resource.SHIELD, 0);
            newResources.put(Resource.ROCK, 0);
            newResources.put(Resource.NOTHING, 0);

            for (Resource r : saveResources) {
                newResources.put(r, newResources.get(r) + 1);
            }
            for (Resource r : resourceList) {
                oldResources.put(r, oldResources.get(r) + 1); //era new
            }

            //if two leader cards are activated
            int leaderCardActivated = 0;
            Resource resourceOne = null;
            Resource resourceTwo = null;

            for (int i = 0; i < humanPlayer.getDashboard().getLeaderCards().size(); i++) {

                //for each leader card I check if it has the required power and if it is active
                if (humanPlayer.getDashboard().getLeaderCards().get(i).getAbilityType().equals(LeaderAbility.NOMOREWHITE) &&
                        humanPlayer.getDashboard().getLeaderCards().get(i).isActive()) {
                    leaderCardActivated++;
                }
            }
            if (leaderCardActivated == 2) {

                HashMap<Resource, Integer> resourcesWhite = humanPlayer.getDashboard().getLeaderCards().get(0).getAbilityResource();

                //save the resource into which I have to transform it
                for (Resource res : resourcesWhite.keySet()) {
                    if (resourcesWhite.get(res) == 1) {
                        resourceOne = res;
                    }
                }

                HashMap<Resource, Integer>  resourcesWhite2 = humanPlayer.getDashboard().getLeaderCards().get(1).getAbilityResource();

                //save the resource into which I have to transform it
                for (Resource res2 : resourcesWhite2.keySet()) {
                    if (resourcesWhite2.get(res2) == 1) {
                        resourceTwo = res2;
                    }
                }

                try {
                    if (!(oldResources.get(resourceOne) + oldResources.get(Resource.NOTHING) >= newResources.get(resourceOne) &&
                            oldResources.get(resourceTwo) + oldResources.get(Resource.NOTHING) >= newResources.get(resourceTwo) &&
                            oldResources.get(resourceOne) + oldResources.get(resourceTwo) + oldResources.get(Resource.NOTHING) >= newResources.get(resourceOne) + newResources.get(resourceTwo))) {
                        return new NACKMessage("White resources conversion is wrong");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                //check that old things that cannot be changed have not been changed
                if ((!oldResources.get(Resource.COIN).equals(newResources.get(Resource.COIN)) || resourceOne == Resource.COIN || resourceTwo == Resource.COIN) &&
                        (!oldResources.get(Resource.ROCK).equals(newResources.get(Resource.ROCK)) || resourceOne == Resource.ROCK || resourceTwo == Resource.ROCK) &&
                        (!oldResources.get(Resource.SHIELD).equals(newResources.get(Resource.SHIELD)) || resourceOne == Resource.SHIELD || resourceTwo == Resource.SHIELD) &&
                        (!oldResources.get(Resource.SERVANT).equals(newResources.get(Resource.SERVANT)) || resourceOne == Resource.SERVANT || resourceTwo == Resource.SERVANT)) {

                    return new NACKMessage("They are not purchased resources");
                    //notify error
                }

            } else {

                //only if the player chooses resources from the ones he receives
                if (!(oldResources.get(Resource.COIN) >= newResources.get(Resource.COIN) &&
                        oldResources.get(Resource.ROCK) >= newResources.get(Resource.ROCK) &&
                        oldResources.get(Resource.SHIELD) >= newResources.get(Resource.SHIELD) &&
                        oldResources.get(Resource.SERVANT) >= oldResources.get(Resource.SERVANT))) {

                    return new NACKMessage("They are not purchased resources");
                    //notify error
                }
            }

            //only if the player chooses a correct number of resources to insert
            if (!modelGame.getActivePlayer().getDashboard().getStock().manageStock(saveResources)) {

                //reestablish original resources
                humanPlayer.setResources(resourceList);

                //notify error
                return new NACKMessage("Not enough space for store resources - try again");
            }

            ArrayList<Resource> discardResource = (ArrayList<Resource>) resourceList.clone();

            for (Resource saveResource : saveResources) {
                discardResource.remove(saveResource);
            }

            if (discardResource != null && discardResource.size() != 0) {
                //just for prevent errors
                ArrayList<Resource> resourcesCopy = new ArrayList<>(discardResource);

                for (Resource resource : resourcesCopy) {
                    if (resource.equals(Resource.FAITH)) discardResource.remove(Resource.FAITH);
                    if (resource.equals(Resource.WISH)) discardResource.remove(Resource.WISH);
                    if (resource.equals(Resource.NOTHING)) discardResource.remove(Resource.NOTHING);
                }
            }
            //Increase popeTracks of players of as many positions as the number of resources discarded by activePlayer
            ArrayList<Player> players = new ArrayList<>();

            if (Objects.requireNonNull(discardResource).size() > 0) {
                for (Player player : modelGame.getPlayers()) {
                    if (!player.equals(modelGame.getActivePlayer())) {
                        players.add(player);
                    }
                }
                moveCross(discardResource.size(), players);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //action done
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.STORE_RESOURCE);

        return new ACKMessage("OK");
    }

    /**
     * Method that active a leader card invoking activeLeaderCard in activePlayer object
     * @param position is the index of the leader card the activePlayer wants to active
     */
    public void activeLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException,ActiveLeaderCardException {
        ((HumanPlayer) modelGame.getActivePlayer()).activeLeaderCard(position);
    }

    /**
     * Method that discard a leader card invoking discardLeaderCard in activePlayer object
     * @param position is the index of the leader card the activePlayer wants to discard
     */
    public void discardLeaderCard(int position) throws OutOfBandException, LeaderCardAlreadyUsedException{

        ((HumanPlayer) modelGame.getActivePlayer()).discardLeaderCard(position);

        for(Player player : modelGame.getPlayers()){
            if(player.equals(modelGame.getActivePlayer())) {
                moveCross(1, new ArrayList<>() {{
                    add(player);
                }});
                return;
            }
        }
    }

    /**
     * Method that check if a series of production zone can be activated and activate it if possible
     * @param positions is an array list that contains the position of the production zone the player wants to activate.
     *                  It contains: 0,1 and 2 for the standard production zone
     *                               3 and 4 for the leader production zone
     * @param activeBasic is true if the player wants to activate the basic production zone
     * @param resourcesRequires is an array list that contains the resources to use in the basic production
     * @param resourcesEnsures is an array list that contains the new resources the player wants after the basic production
     * @param leaderResources is an array list that contains the production of the leader card zones the player wants
     * @throws NotEnoughResourcesException if the player cannot activate all the production zone specified
     * @throws ExcessOfPositionException if a position specified doesn't exist
     * @throws BadParametersException if there is a repetition of the same production zone in positions
     * @throws NonCompatibleResourceException if the resources specified for the basic production are wrong
     * @throws ActionAlreadyDoneException if the player has already done an action in this turn
     */
    public void activeProductionZones(ArrayList<Integer> positions , boolean activeBasic ,
                                      ArrayList<Resource> resourcesRequires , ArrayList<Resource> resourcesEnsures ,
                                      ArrayList<Resource> leaderResources)
            throws NotEnoughResourcesException , ExcessOfPositionException , BadParametersException ,
            NonCompatibleResourceException , ActionAlreadyDoneException{

        //Check if the player can do this action
        if(!((HumanPlayer)modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING) &&
                !((HumanPlayer)modelGame.getActivePlayer()).getActionChose().equals(Action.ACTIVE_PRODUCTION))
            throw new ActionAlreadyDoneException("Cannot do an other action");

        if(positions == null && !activeBasic)
            throw new BadParametersException("No production zone specified");
        if(positions != null && positions.size() == 0 && !activeBasic)
            throw new BadParametersException("No production zone specified");

        int numOfProductionZones = modelGame.getActivePlayer().getDashboard().getProductionZone().length
                + modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().size();

        int numOfLeaderProductionActivated = 0;

        if(positions != null){
            //Check if the position are valid and if there isn't equal position
            for(int i = 0 ; i < positions.size() ; i++){
                if(positions.get(i) < 0 || positions.get(i) >= numOfProductionZones)
                    throw new ExcessOfPositionException("Position not valid");
                for(int j = 0; j < positions.size() ; j++){
                    if(i != j && positions.get(i).equals(positions.get(j)))
                        throw new BadParametersException("Cannot activate the same production zone twice");
                }
                if(positions.get(i) >=  modelGame.getActivePlayer().getDashboard().getProductionZone().length){
                    numOfLeaderProductionActivated++;
                }
            }
        }

        if(numOfLeaderProductionActivated > 0 && leaderResources == null)
            throw new BadParametersException("Error in association between leader production resources and active leader production");

        if(leaderResources != null){
            if(numOfLeaderProductionActivated > 0 && leaderResources.size() == 0)
                throw new BadParametersException("Error in association between leader production resources and active leader production");
            if(numOfLeaderProductionActivated == 0 && leaderResources.size() > 0)
                throw new BadParametersException("Error in association between leader production resources and active leader production");
            if(numOfLeaderProductionActivated != leaderResources.size()){
                throw new BadParametersException("Error in association between leader production resources and active leader production");
            }
        }

        if(activeBasic){
            if(resourcesRequires == null || resourcesEnsures == null)
                throw new BadParametersException("Requires and ensures not specified");
            if(resourcesRequires.size() != 2 || resourcesEnsures.size() != 1)
                throw new NonCompatibleResourceException("Too many or too few resources for the activation of the basic production");
        }

        //Check if the resources the player has are enough
        //I'll sum all the requires and I'll verify if they are present
        HashMap<Resource , Integer> totalRequires = new HashMap<>();
        //Initialize the hashMap
        totalRequires.put(Resource.COIN , 0);
        totalRequires.put(Resource.ROCK , 0);
        totalRequires.put(Resource.SHIELD , 0);
        totalRequires.put(Resource.SERVANT , 0);

        //If the player wants to activate the basic production zone
        if(activeBasic){
            for(Resource resource : resourcesRequires){
                totalRequires.put(resource , totalRequires.get(resource) + 1);
            }
        }

        Card card;

        if(positions != null){
            //Sum all the requires
            for (Integer position : positions) {
                //If a standard production zone
                if (position < modelGame.getActivePlayer().getDashboard().getProductionZone().length) {
                    if (modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard() == null)
                        throw new BadParametersException("This production zone is empty");

                    card = modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard();
                }
                //If a leader production zone
                else {
                    //Theoretically the next if will never be true
                    if (modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().get(numOfProductionZones - position - 1) == null)
                        throw new BadParametersException("This production zone is empty");
                    card = modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().get(numOfProductionZones - position - 1).getCard();
                }

                HashMap<Resource, Integer> cardRequires;
                if (card instanceof EvolutionCard)
                    cardRequires = card.getRequires();
                else
                    cardRequires = ((LeaderCard) card).getAbilityResource();

                for (Resource resource : cardRequires.keySet()) {
                    totalRequires.put(resource, (totalRequires.get(resource) + cardRequires.get(resource)));
                }
            }
        }

        //Check if the resources are enough
        for(Resource resource : totalRequires.keySet()){
            if(totalRequires.get(resource) > (modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource)
                    + modelGame.getActivePlayer().getDashboard().getLockBox().getAmountOf(resource)))
                throw new NotEnoughResourcesException("Resources are not enough");
        }

        //Here the player can activate all the production he specified

        //Set the action at the player
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.ACTIVE_PRODUCTION);

        //Active the base production -> can I do this operation here without call a method?
        if(activeBasic){
            modelGame.getActivePlayer().getDashboard().activeBasicProduction(resourcesRequires.get(0) , resourcesRequires.get(1) , resourcesEnsures.get(0));
        }

        //If the player activated only the basic production zone
        if(positions == null){
            return;
        }

        //Active the production zone
        for (Integer position : positions) {
            if (position < modelGame.getActivePlayer().getDashboard().getProductionZone().length)
                card = modelGame.getActivePlayer().getDashboard().getProductionZone()[position].getCard();
            else
                card = modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().get(numOfProductionZones - position - 1).getCard();

            HashMap<Resource, Integer> cardRequires;
            HashMap<Resource, Integer> cardProduction;

            if (card instanceof EvolutionCard) {
                cardRequires = card.getRequires();
            } else {
                cardRequires = ((LeaderCard) card).getAbilityResource();
            }

            //Take resources from Stock and LockBox
            takeResources(cardRequires);

            if (card instanceof EvolutionCard) {

                cardProduction = card.getProduction();
                //Add the resources produced by the activation of this production zone in LockBox
                for (Resource resource : cardProduction.keySet()) {
                    try {
                        if (!resource.equals(Resource.FAITH))
                            //Add the resources
                            modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource, cardProduction.get(resource));
                        else {
                            //Increment the pope track position
                            ArrayList<Player> player = new ArrayList<>();
                            player.add(modelGame.getActivePlayer());
                            moveCross(cardProduction.get(Resource.FAITH), player);
                        }
                    } catch (NotEnoughResourcesException e) {
                        //Impossible be here
                    }
                }
                card.setActive(true);
            }
            card.setActive(true);
        }

        //If a leader production is been activated
        if(numOfLeaderProductionActivated > 0){
            //Increment the pope track position
            ArrayList<Player> player = new ArrayList<>();
            player.add(modelGame.getActivePlayer());
            moveCross(numOfLeaderProductionActivated , player);

            for(Resource resource : leaderResources){
                modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , 1);
            }
        }
    }

    /**
     *
     * Method that buy a card, use the resources and place the card
     * @param row row of the evolutionSection
     * @param col column of the evolutionSection
     * @param position is in which productionZone the player wants to place the card
     * @throws InvalidPlaceException if the player wants to place the card in a wrong position
     * @throws BadParametersException if the row/col are out of range; if the player can't buy the card
     * @throws NotEnoughResourcesException if resources are not enough
     * @throws ExcessOfPositionException if the player is buying in a wrong position
     */
    public void buyEvolutionCard(int row, int col , int position) throws InvalidPlaceException , BadParametersException ,
            NotEnoughResourcesException , ExcessOfPositionException{

        if(row < 0 || col < 0 || row >= 3 || col >= 4){
            throw new BadParametersException("Invalid row/col");
        }

        //Check if the player can buy this card
        if(!(((HumanPlayer) modelGame.getActivePlayer()).getPossibleEvolutionCard()[row][col])) {
            throw new BadParametersException("Cannot buy this card");
        }

        //Check if the position is valid
        if(position < 0 || position >=
                (modelGame.getActivePlayer().getDashboard().getProductionZone().length
                        + modelGame.getActivePlayer().getDashboard().getLeaderProductionZones().size())){
            throw new InvalidPlaceException("Invalid position");
        }

        //Read the card the player wants to buy
        EvolutionCard eCard = modelGame.getEvolutionSection().canBuy()[row][col];

        if (!((HumanPlayer) modelGame.getActivePlayer()).getPossibleProductionZone(eCard)[position]) {
            //The card cannot be placed in position
            //Ask again the user where place the card
            throw new InvalidPlaceException("Invalid position");
        }

        //Read the cost of  n  eCard
        HashMap<Resource, Integer> cost = eCard.getCost();

        //Reduce resources if Leader card sales is active
        ArrayList<LeaderCard> leaderCards = modelGame.getActivePlayer().getDashboard().getLeaderCards();

        if(leaderCards.size() > 0){
            for(LeaderCard leaderCard : leaderCards){
                if(leaderCard.isActive() && leaderCard.getAbilityType() == LeaderAbility.SALES){
                    HashMap<Resource , Integer> sales = leaderCard.getAbilityResource();
                    for(Resource resource : sales.keySet()){
                        int numResource  = cost.get(resource);//resource required by the evolution card
                        if(numResource > 0){
                            int numSale = sales.get(resource);//num of resources of sale (it's a negative number)
                            if(numSale < 0){
                                cost.put(resource, Math.max(numResource + numSale, 0));
                            }
                        }
                    }
                }
            }
        }

        //Take resources from Stock and then from LockBox
        takeResources(cost);

        //Buy the card and place it

        EvolutionCard cardBought = modelGame.getEvolutionSection().buy(row, col);
        modelGame.getActivePlayer().getDashboard().getProductionZone()[position].addCard(cardBought);
        modelGame.getActivePlayer().getDashboard().setEvolutionCardNumber(modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber() + 1);

        //Set the action done in player
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.BUY_CARD);
    }

    /**
     * Method that increment pope track position of the specified player and check if someone has arrived in a Pope Meeting position.
     * In that case, will be activated or de-activated pope cards.
     * @param positions number of steps for each player to take
     * @param players player whose pope track should be increased
     */
    private void moveCross(int positions, ArrayList<Player> players) {

        //Increment Pope Track
        for (int i = 0; i < positions; i++) {
            for (Player player : players) {

                if (player instanceof LorenzoPlayer) {
                    player.getPopeTrack().updateLorenzoPosition(1);
                } else {
                    player.getPopeTrack().updateGamerPosition(1);
                }
            }

            //Check Pope section
            boolean popeMeeting = false;

            for (Player player : players) {

                //check that a player arrived in a PopeMeeting position
                //check that no one has ever arrived in that position
                if (player instanceof LorenzoPlayer) {
                    if (player.getPopeTrack().getLorenzoPosition().getPopePosition() &&
                            turnHandler.getLastSection() < player.getPopeTrack().getLorenzoPosition().getNumPopeSection()) {

                        turnHandler.setLastSection(player.getPopeTrack().getLorenzoPosition().getNumPopeSection());
                        //player.getPopeTrack().getPopeCard().get(turnHandler.getLastSection() - 1).setIsUsed();
                        popeMeeting = true;
                    }
                }

                if (player instanceof HumanPlayer && !popeMeeting) {
                    if (player.getPopeTrack().getGamerPosition().getPopePosition() &&
                            turnHandler.getLastSection() < player.getPopeTrack().getGamerPosition().getNumPopeSection()) {

                        turnHandler.setLastSection(player.getPopeTrack().getGamerPosition().getNumPopeSection());
                        popeMeeting = true;
                    }
                }

                if (popeMeeting) {
                    for (Player player2 : modelGame.getPlayers()) {
                        if (player2 instanceof HumanPlayer) {
                            if (player2.getPopeTrack().getGamerPosition().getPopeSection() &&
                                    player2.getPopeTrack().getGamerPosition().getNumPopeSection() == turnHandler.getLastSection()) {
                                player2.getPopeTrack().getPopeCard().get(turnHandler.getLastSection() - 1).setIsUsed();
                            } else {
                                player2.getPopeTrack().getPopeCard().get(turnHandler.getLastSection() - 1).setIsDiscard();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Private method that take the resources from Stock before and then from Stock automatically
     * The controller calls this method only after checking if the resources required are enough
     * @param requires is an HashMap that contains the resources to remove
     */
    private void takeResources(HashMap<Resource , Integer> requires) throws NotEnoughResourcesException {
        //Take resources from Stock and LockBox
        for(Resource resource : requires.keySet()){
            int resourceInStock = modelGame.getActivePlayer().getDashboard().getStock().getTotalQuantitiesOf(resource);
            int totalResource = requires.get(resource);
            //Take resources from Stock and LockBox
            if(resourceInStock > 0){
                if(resourceInStock - totalResource >= 0){
                    modelGame.getActivePlayer().getDashboard().getStock().useResources(totalResource , resource);
                    totalResource = 0;
                }
                else{
                    modelGame.getActivePlayer().getDashboard().getStock().useResources(resourceInStock , resource);
                    totalResource -= resourceInStock;
                }
            }
            //If necessary take resources from LockBox
            if(totalResource > 0){
                try {
                    modelGame.getActivePlayer().getDashboard().getLockBox().setAmountOf(resource , -totalResource);
                }catch (NotEnoughResourcesException e){
                    //Impossible be here
                }
            }
        }

    }

}