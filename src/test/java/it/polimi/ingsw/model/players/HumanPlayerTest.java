package it.polimi.ingsw.model.players;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelEnum;
import it.polimi.ingsw.model.game.EvolutionSection;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.LeaderCardSet;
import it.polimi.ingsw.model.game.Resource;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HumanPlayerTest extends TestCase {

    public void testGetPossibleProductionZone() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players);
        boolean[] result = new boolean[player.getDashboard().getProductionZone().length];;
        boolean[] toCheck;

        try {
            EvolutionCard[][] eSection = game.getEvolutionSection().canBuy();

            assertEquals(LevelEnum.FIRST , eSection[2][2].getLevel());
            assertEquals(LevelEnum.SECOND , eSection[1][1].getLevel());
            assertEquals(true , player.getPossibleProductionZone(eSection[2][2])[0]);
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(2, 2));//FIRST
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(2, 2));//FIRST
            player.getDashboard().getProductionZone()[2].addCard(game.getEvolutionSection().buy(1, 1));//SECOND

            result[0] = true;
            result[1] = false;
            result[2] = false;

            eSection = game.getEvolutionSection().canBuy();

            toCheck = player.getPossibleProductionZone(eSection[2][0]);//FIRST
            for (int i = 0; i < player.getDashboard().getProductionZone().length; i++) {
                assertEquals(result[i], toCheck[i]);
            }

            result[0] = false;
            result[1] = false;
            result[2] = true;
            toCheck = player.getPossibleProductionZone(eSection[0][0]);//THIRD
            for(int i = 0; i < player.getDashboard().getProductionZone().length; i++){
                assertEquals(result[i] , toCheck[i]);
            }

            result[0] = false;
            result[1] = true;
            result[2] = false;
            toCheck = player.getPossibleProductionZone(eSection[1][1]);//SECOND
            for(int i = 0; i < player.getDashboard().getProductionZone().length; i++){
                assertEquals(result[i] , toCheck[i]);
            }
        }catch(InvalidPlaceException e){
            fail();
        }catch ( ExcessOfPositionException e){
            fail();
        }
        try {
            player.getDashboard().getProductionZone()[0].addCard(game.getEvolutionSection().buy(2 ,2));//FIRST
            EvolutionCard[][] eSection = game.getEvolutionSection().canBuy();
            result[0] = false;
            result[1] = false;
            result[2] = false;
            toCheck = player.getPossibleProductionZone(eSection[2][1]);//FIRST
            for(int i = 0; i < player.getDashboard().getProductionZone().length; i++){
                assertEquals(result[i] , toCheck[i]);
            }
        }catch(InvalidPlaceException e){
            fail();
        }catch ( ExcessOfPositionException e){
            fail();
        }

    }

    public void testGetPossibleEvolutionCard() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players);
        player.setGame(game);
        player2.setGame(game);

        EvolutionCard[][] eCards = game.getEvolutionSection().canBuy();
        EvolutionCard eCard = eCards[2][2];//Required 1 servant , cost 2 rocks
        System.out.println("Level: " + eCard.getLevel());
        System.out.println("Requires: ");
        for(Resource resource : eCard.getRequires().keySet()){
            System.out.println("Resource: " + resource + " , quantity: " + eCard.getRequires().get(resource));
        }
        System.out.println("Cost:");
        for(Resource resource : eCard.getRequires().keySet()){
            System.out.println("Resource: " + resource + " , quantity: " + eCard.getCost().get(resource));
        }

        //now the player doesn't have resources
        assertEquals(false , player.getPossibleEvolutionCard()[2][2]);

        try {
            player.getDashboard().getLockBox().setAmountOf(Resource.ROCK , 2);
            assertEquals(true , player.getPossibleEvolutionCard()[2][2]);
        }catch(NotEnoughResourcesException e){
            fail();
        }
    }

    /**
     * For now I won't consider leader card production zone
     */
    /*public void testGetPossibleActiveProductionZone() {
        HumanPlayer player = new HumanPlayer("Matteo" , true);
        HumanPlayer player2 = new HumanPlayer("Loser" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        players.add(player2);
        Game game = new Game(players , 1234);

        boolean[] result = new boolean[player.getDashboard().getProductionZone().length];
        boolean[] toCheck =  new boolean[player.getDashboard().getProductionZone().length];

        //With no resources and no cards player can activate nothing
        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            result[i] = false;
        toCheck = player.getPossibleActiveProductionZone();

        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            assertEquals(result[i] , toCheck[i]);

        //Add 2 coins -> now the player can activate the basix production
        try {
            player.getDashboard().getLockBox().setAmountOf(Resource.COIN , 2);
        }catch(Exception e){
            fail();
        }
        result[0] = true;
        for(int i = 1; i < player.getDashboard().getProductionZone().length ; i++)
            result[i] = false;
        toCheck = player.getPossibleActiveProductionZone();
        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            assertEquals(result[i] , toCheck[i]);

        //Add a card in the production zone
        try {
            player.getDashboard().getProductionZone()[1].addCard(game.getEvolutionSection().buy(2, 2));//FIRST , requires = 1 servant
            HashMap<Resource , Integer> requires = player.getDashboard().getProductionZone()[1].getCard().getRequires();
            for(Resource res : requires.keySet()){
                System.out.println("Resource: " + res + ", number: " + requires.get(res));
            }
        }catch (Exception | InvalidPlaceException e){
            fail();
        }
        try {
            player.getDashboard().getStock().addResources(0 , 1 , Resource.SERVANT);

        }catch (Exception e){
            fail();
        }
        result[0] = true;
        result[1] = true;//production zone 0
        for(int i = 1; i < player.getDashboard().getProductionZone().length ; i++)
            result[i] = false;
        toCheck = player.getPossibleActiveProductionZone();
        for(int i = 0; i < player.getDashboard().getProductionZone().length ; i++)
            System.out.println(toCheck[i]);
        //assertEquals(result[i] , toCheck[i]);

    }*/

    public void testActiveLeaderCard() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> cardsSet = leaderCardSet.getLeaderCardSet();
        ArrayList<LeaderCard> playerCards = new ArrayList<LeaderCard>();
        int i = 0;
        //Take the card with no STOCK PLUS ability
        while(cardsSet.get(i).getAbilityType().equals(LeaderAbility.STOCKPLUS))
            i++;
        playerCards.add(cardsSet.get(i));
        i = 0;
        //Take the card with STOCK PLUS ability
        while(!cardsSet.get(i).getAbilityType().equals(LeaderAbility.STOCKPLUS))
            i++;
        playerCards.add(cardsSet.get(i));

        HumanPlayer player = new HumanPlayer("Matteo" , false);
        player.getDashboard().setLeaderCards(playerCards);

        try {
            assertEquals(player.getDashboard().getLeaderCards().size() , 2);
            player.activeLeaderCard(3);
            fail();
        }catch(OutOfBandException e){
            //It's right
        }catch (LeaderCardAlreadyUsedException | ActiveLeaderCardException e){
            fail();
        }

        /*
        try{
            player.activeLeaderCard(0);
        }catch(Exception e){
            fail();
        }

        try{
            player.activeLeaderCard(0);
            fail();
        }catch(LeaderCardAlreadyUsedException e){
            //It's true
        }catch (Exception e){
            fail();
        }

        try {
            assertEquals(player.getDashboard().getStock().getNumberOfBoxes() , 3);
            player.activeLeaderCard(1);
            assertEquals(player.getDashboard().getStock().getNumberOfBoxes() , 4);
        }catch (Exception e){
            fail();
        }

         */
    }

    public void testDiscardLeaderCard() {
        LeaderCardSet leaderCardSet = new LeaderCardSet();
        ArrayList<LeaderCard> cardsSet = leaderCardSet.getLeaderCardSet();
        ArrayList<LeaderCard> playerCards = new ArrayList<LeaderCard>();
        playerCards.add(cardsSet.get(0));
        playerCards.add(cardsSet.get(1));

        HumanPlayer player = new HumanPlayer("Matteo" , false);
        player.getDashboard().setLeaderCards(playerCards);

        try {
            assertEquals(player.getDashboard().getLeaderCards().size() , 2);
            player.discardLeaderCard(3);
            fail();
        }catch(OutOfBandException e){
            //It's right
        }catch (LeaderCardAlreadyUsedException e){
            fail();
        }

        player.getDashboard().getLeaderCards().get(0).setActive(true);
        try {
            player.discardLeaderCard(0);
            fail();
        }catch(OutOfBandException e){
            fail();
        }catch(LeaderCardAlreadyUsedException e){
            //It's true
            assertEquals(player.getDashboard().getLeaderCards().size() , 2);
        }

        try{
            assertEquals(player.getDashboard().getPopeTrack().getGamerPosition().getIndex(), 0);
            player.discardLeaderCard(1);
            assertEquals(player.getDashboard().getLeaderCards().size() , 1);
            assertEquals(player.getDashboard().getPopeTrack().getGamerPosition().getIndex(), 1);
        }catch(Exception e){
            fail();
        }


    }

    public void testAddResources() {
        HumanPlayer player = new HumanPlayer("Matteo" , false);
        assertEquals(player.getResources().size() , 0);

        player.addResources(Resource.COIN);
        player.addResources(Resource.SHIELD);
        assertEquals(player.getResources().size() , 2);
        assertEquals(player.getResources().contains(Resource.COIN) , true);
        assertEquals(player.getResources().contains(Resource.SHIELD) , true);
        assertEquals(player.getResources().contains(Resource.ROCK) , false);
    }

    public void testRemoveResources() {
        HumanPlayer player = new HumanPlayer("Matteo" , false);

        player.addResources(Resource.COIN);
        player.addResources(Resource.SHIELD);
        assertEquals(player.getResources().size() , 2);

        try {
            player.removeResources(Resource.COIN);
            assertEquals(player.getResources().size() , 1);
            assertEquals(player.getResources().contains(Resource.COIN) , false);
            assertEquals(player.getResources().contains(Resource.SHIELD) , true);
            assertEquals(player.getResources().contains(Resource.ROCK) , false);
        }catch (NonCompatibleResourceException e){
            fail();
        }
        try {
            player.removeResources(Resource.ROCK);
            fail();
        }catch (NonCompatibleResourceException e){
            //It's right
        }
        try {
            player.addResources(Resource.COIN);
            player.addResources(Resource.COIN);
            player.addResources(Resource.COIN);
            assertEquals(player.getResources().size() , 4);

            player.removeResources(Resource.COIN);
            assertEquals(player.getResources().contains(Resource.COIN) , true);
            assertEquals(player.getResources().size() , 3);
        }catch (NonCompatibleResourceException e){
            fail();
        }
    }

    public void testSetGame() {
        Player player = new HumanPlayer("Matteo" , false);
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player);
        Game game = new Game(players);

        ((HumanPlayer) player).setGame(game);

    }

    public void testSetActionChose() {
        HumanPlayer player = new HumanPlayer("Matteo" , false);

        player.setActionChose(Action.NOTHING);
        assertEquals(Action.NOTHING , player.getActionChose());
        player.setActionChose(Action.BUY_CARD);
        assertEquals(Action.BUY_CARD , player.getActionChose());
        player.setActionChose(Action.BUY_FROM_MARKET);
        assertEquals(Action.BUY_FROM_MARKET , player.getActionChose());
        player.setActionChose(Action.ACTIVE_PRODUCTION);
        assertEquals(Action.ACTIVE_PRODUCTION , player.getActionChose());
    }
}