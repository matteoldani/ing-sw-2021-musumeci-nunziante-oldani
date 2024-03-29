package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.sentByServer.EndGameMessage;
import it.polimi.ingsw.messages.sentByServer.updateMessages.UpdateActivePlayerMessage;
import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.players.HumanPlayer;
import it.polimi.ingsw.model.players.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that contains all turn and game management commands for multiplayer match
 */
public class TurnHandlerMultiPlayer extends TurnHandler {

    public TurnHandlerMultiPlayer(Game modelGame) {
        super(modelGame);
    }

    /**
     * Check if start turn or end game
     */
    @Override
    public void startTurn() {
        if (isTheLastTurn && modelGame.getActivePlayer().getDashboard().getInkwell()) {
            isTheEnd = true;
            endGame();
        }
    }

    /**
     * Method that check who is the winner
     */
    @Override
    protected ArrayList<Player> checkWinner(){
        //winners is an arrayList that contains the winner or the winners in case of same score and same amount of resources
        ArrayList<Player> winners = new ArrayList<>();
        //winner is initialized with a default value
        Player winner = modelGame.getActivePlayer();

        //Take last player with the highest score
        for(Player player : modelGame.getPlayers()){
            if(player.getDashboard().getScore() >= winner.getDashboard().getScore()){
                winner = player;
            }
        }
        //Take the last player with the highest score and the highest number of resources
        for(Player player : modelGame.getPlayers()){
            if(player.getDashboard().getScore() == winner.getDashboard().getScore()){
                if(player.getDashboard().getStock().getTotalNumberOfResources() + player.getDashboard().getLockBox().getTotalAmountOfResources()
                        >= winner.getDashboard().getLockBox().getTotalAmountOfResources() + winner.getDashboard().getStock().getTotalNumberOfResources())  {
                    winner = player;
                }
            }
        }
        //Add to winner the players with the highest score and number of resources
        for(Player player : modelGame.getPlayers()){
            if(player.getDashboard().getScore() == winner.getDashboard().getScore() &&
                    (player.getDashboard().getStock().getTotalNumberOfResources() + player.getDashboard().getLockBox().getTotalAmountOfResources()) ==
                            (winner.getDashboard().getLockBox().getTotalAmountOfResources() + winner.getDashboard().getStock().getTotalNumberOfResources())){
                player.setWinner();
                winners.add(player);
            }
        }
        return winners;
    }

    /**
     * Check if the activePlayer has reached the end of the game
     * Called only if isTheLastTurn == false
     */
    @Override
    public void checkEndGame(){

        try{
            //someone reached the end of the track
            for (Player player :modelGame.getPlayers()) {
                if(player.getPopeTrack().getGamerPosition().getIndex()==24){
                    isTheLastTurn=true;
                    break;
                }
            }

            //active player bought 7 Evolution Cards
            if(!isTheLastTurn && modelGame.getActivePlayer().getDashboard().getEvolutionCardNumber()>6){
                isTheLastTurn=true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Method called in the end of the turn
     * @return a message informing all players that the turn has changed or the game is over
     */
    @Override
    public Message endTurn() {
        //Set NOTHING the action chose by the player
        ((HumanPlayer) modelGame.getActivePlayer()).setActionChose(Action.NOTHING);
        //Set isActive = false in the eCards on the top of each productionZone
        for(ProductionZone pZone : modelGame.getActivePlayer().getDashboard().getProductionZone()){
            if(pZone.getCard() != null)
                pZone.getCard().setActive(false);
        }

        if(!isTheLastTurn) checkEndGame();

        //Update the active player for the next turn
        modelGame.updateActivePlayer();

        if (isTheLastTurn && modelGame.getActivePlayer().getDashboard().getInkwell()) {
            isTheEnd = true;
            return endGame();
        }

        return new UpdateActivePlayerMessage(modelGame.getActivePlayer().getNickName());
    }

    /**
     * Method that collects information about players and their scores and notifies them to all users.
     * @return a message that contains all information about final results
     */
    @Override
    public Message endGame(){
        ArrayList<String> winners = new ArrayList<>();

        HashMap<String, Integer> scores = new HashMap<>();
        try{
            checkWinner();

            for (Player player : modelGame.getPlayers()){
                if(player.isWinner()){
                    winners.add(player.getNickName());
                }
                scores.put(player.getNickName(), player.getDashboard().getScore());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return new EndGameMessage("The game is ended", winners, scores);
    }
}
