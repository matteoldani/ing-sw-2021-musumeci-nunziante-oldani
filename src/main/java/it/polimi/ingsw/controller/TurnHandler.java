package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.messages.sentByServer.ACKMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.NACKMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.*;
import it.polimi.ingsw.model.board.LeaderProductionZone;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Class that contains all turn and game management commands
 */
public abstract class TurnHandler {

    protected DoActionPlayer actionHandler;

    protected Game modelGame;

    /**
     * This attribute will be set true when one player previously ends the game
     *  and next active player would be the one with inkwell
     */
    protected boolean isTheEnd;


    /**
     * This attribute will be set true when one player ends the game
     *  but the game must go on until the player with the inkwell
     */
    protected boolean isTheLastTurn;

    /**
     * This attribute maintains the last popePosition that it's been reached by a player
     * We will use this attribute when one player reaches a pope position to control if he is the
     *      first one or if this section is already been discovered by an other player
     */
    protected int lastSection;

    /**
     * This attribute is needed because the game handler is the only one able to call the endGame method to
     * update the server.
     */
    public TurnHandler(Game modelGame){
        this.modelGame = modelGame;
        this.lastSection = 0;
        this.isTheEnd = false;
        this.isTheLastTurn = false;
        actionHandler= new DoActionPlayer(modelGame,this);
    }

    /**
     * check if start turn or end game
     */
    abstract void startTurn();

    /**
     * Do the action of the active player according to the message received
     * @param message contains which row/col user want to buy
     * @return ack or nack message
     */
    public Message doAction(BuyFromMarketMessage message){

        //Client asks to receive resources from market
        //Resources are stored in his dashboard not in his stock
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING)) {
            try {
                actionHandler.buyFromMarket(message.getPosition(), message.isRow());
                return new ACKMessage("OK");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Position not found");
            }
        }
        return new NACKMessage("Action has already been performed");
    }

    /**
     * Do the action of the active player according to the message received
     * @param message contains the position of the card the player wants to buy and where he wanna place it
     * @return an ACK message or a NACK message
     */
    public Message doAction(BuyEvolutionCardMessage message){
        //Client asks to buy a evolution card
        //one at a time
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING)) {
            try {
                actionHandler.buyEvolutionCard(message.getRow(),message.getCol(), message.getPosition());
                return new ACKMessage("OK");
            } catch (InvalidPlaceException e) {
                return new NACKMessage("Wrong position");
            } catch (BadParametersException e) {
                return new NACKMessage("Can't buy this card");
            } catch (NotEnoughResourcesException e) {
                return new NACKMessage("Not enough resources exception");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Wrong position 2");
            }
        }
        return new NACKMessage("Action has already been performed");
    }

    /**
     * Do the action of the active player according to the message received
     * @param message contains which production zone the player wants to activate, if he wants tp active
     *                the basic production , the resources for the basic production  and the ensures in case
     *                of activation of a leader production zone
     * @return an ACK message or a NACK message
     */
    public Message doAction(ActiveProductionMessage message){

        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.NOTHING)) {

            try {
                ArrayList<Integer> positions;
                if(message.getLeaderIds() != null && message.getLeaderIds().size() > 0){
                    positions = convertMessage(message.getPositions() , message.getLeaderIds());
                }
                else{
                    positions = message.getPositions();
                }
                actionHandler.activeProductionZones(positions,
                        message.isActiveBasic(),
                        message.getResourcesRequires(),
                        message.getResourcesEnsures(),
                        message.getLeaderResources());
                return new ACKMessage("OK");
            } catch (NonCompatibleResourceException e) {
                return new NACKMessage("Too many or too few resources for the activation of the basic production");
            } catch (ExcessOfPositionException e) {
                return new NACKMessage("Position not valid");
            } catch (NotEnoughResourcesException e) {
                return new NACKMessage("Resources are not enough");
            } catch (ActionAlreadyDoneException e) {
                return new NACKMessage("Cannot do an other action");
            } catch (BadParametersException e) {
                return new NACKMessage("Empty production zone / same production zone specified twice / " +
                        "requires or ensures not specified");
            }
        }
        return new NACKMessage("Action has already been performed");
    }

    /**
     * Do the action of the active player according to the message received
     * @param message contains an arrayList of resources the player wants to store
     * @return an ACK message or a NACK message
     */
    public Message doAction(StoreResourcesMessage message){
        if(((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.BUY_FROM_MARKET) || ((HumanPlayer) modelGame.getActivePlayer()).getActionChose().equals(Action.STORE_RESOURCE)  ) {
            return (actionHandler.storeResourcesBought((message).getSaveResources()));
        }
        return new NACKMessage("Before storing resources buying them");
    }

    /**
     * Do the action of the active player according to the message received
     * @param message contains the position of the leader card the player wants to activate
     * @return an ACK message or a NACK message
     */
    public Message doAction(ActiveLeaderCardMessage message){
        try {
            actionHandler.activeLeaderCard(message.getPosition());
            return new ACKMessage("OK");
        } catch (OutOfBandException e) {
            return new NACKMessage("Leader Card not present");
        } catch (LeaderCardAlreadyUsedException e) {
            return new NACKMessage("Leader Card has already used");
        } catch (ActiveLeaderCardException e){
            return new NACKMessage("Activation requires are not satisfied");
        }
    }

    /**
     * Do the action of the active player according to the message received
     * @param message contains the position of the leader card the player wants to discard
     * @return an ACK message or a NACK message
     */
    public Message doAction(DiscardLeaderCardMessage message){
        try {
            actionHandler.discardLeaderCard((message).getPosition());
            return new ACKMessage("OK");
        } catch (OutOfBandException e) {
            return new NACKMessage("Leader Card not present");
        } catch (LeaderCardAlreadyUsedException e) {
            return new NACKMessage("Leader Card has already discarded");
        }
    }

    /**
     * Method the checks the score/the number of resources of each player and create an arrayList with the winners
     * @return the arrayList winners
     */
    abstract ArrayList<Player> checkWinner();

    /**
     * Check if the activePlayer has reached the end of the game
     */
    abstract void checkEndGame();

    /**
     * Method called in the end of the turn
     * @return message that notifies the new activePlayer
     */
    public abstract Message endTurn();

    /**
     * Method that ends the game
     * @return message that notifies that the game is over
     */
    abstract Message endGame();

    /**
     * @return the last section reached
     */
    public int getLastSection() {
        return lastSection;
    }

    /**
     * Method that sets the last section reached
     * @param lastSection is the number of a section
     */
    public void setLastSection(int lastSection) {
        this.lastSection = lastSection;
    }

    /**
     * @return if the game is ended or no
     */
    public boolean isTheEnd() { return isTheEnd; }

    /**
     * Method that set isTheEnd
     * @param theEnd true if the game is over
     */
    public void setTheEnd(boolean theEnd) { isTheEnd = theEnd; }

    /**
     * @return if is the last turn
     */
    public boolean isTheLastTurn() {
        return isTheLastTurn;
    }

    /**
     * Method that set isTheLastTurn
     * @param theLastTurn is true if is the last turn
     */
    public void setTheLastTurn(boolean theLastTurn) {
        isTheLastTurn = theLastTurn;
    }

    /**
     * Method called when an activeProductionZone message is received: transform the leaderIds in the relative
     *                  position in the dashboard
     * @param positions is an arrayList containing the position with which cards the player wants to activate
     * @param leaderIds is an arrayList containing the leader id of the leader card the player wants to active the
     *                  production
     * @return is a message , the attribute if there is not to do , a new message if there was an exchange
     */
    private ArrayList<Integer> convertMessage(ArrayList<Integer> positions , ArrayList<Integer> leaderIds){

        if(leaderIds == null || leaderIds.size() == 0)
            return positions;

        if(positions == null)
            positions = new ArrayList<>();

        ArrayList<LeaderProductionZone> leaderCards = modelGame.getActivePlayer().getDashboard().getLeaderProductionZones();

        int zeroNumber = modelGame.getActivePlayer().getDashboard().getProductionZone().length;

        for(int i = 0 ; i < leaderCards.size() ; i++){
            for(Integer leaderCardId : leaderIds){
                if(((LeaderCard) leaderCards.get(i).getCard()).getId() == leaderCardId) {
                    positions.add(zeroNumber + i);
                }
            }
        }

        return positions;
    }
}
