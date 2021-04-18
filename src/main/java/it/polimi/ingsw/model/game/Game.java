package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.players.HumanPlayer;

import java.util.ArrayList;

public class Game {

    private ArrayList<HumanPlayer> players;
    private Market market;
    private EvolutionSection evolutionSection;
    private HumanPlayer activePlayer;
    private int idGame;

    public Game(ArrayList<HumanPlayer> players, int idGame){
        this.players = players;
        this.market= Market.getInstanceOfMarket();
        this.evolutionSection = EvolutionSection.getInstanceOfEvolutionSection();
        this.activePlayer = players.get(0);
        this.idGame = idGame;
    }

    /**
     * method that updates the current active player
     * @return the new active player
     */
    public HumanPlayer updateActivePlayer(){

       for(int i=0; i<players.size(); i++){
           if(activePlayer.equals(players.get(i))){
               i++;
               if(i>= players.size()){
                   i = 0;
               }
               activePlayer = players.get(i);
           }
        }

       return activePlayer;
    }

    public ArrayList<HumanPlayer> getPlayers() {
        return players;
    }

    public Market getMarket() {
        return market;
    }

    public EvolutionSection getEvolutionSection() {
        return evolutionSection;
    }

    public HumanPlayer getActivePlayer() {
        return activePlayer;
    }

    /*
    LorenzoPlayer x;
    HumanPlayer y;

    isInstanceOf(game.getAcriceplater) == LorenzoPlater
        true --> x = (LorenPlayer) game.getActiveplayer()
                 x.play
                game.upd
*/
}