package it.polimi.ingsw.client.cli.gamePhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.componentPrinter.LeaderCardsPrinter;
import it.polimi.ingsw.messages.sentByClient.configurationMessagesClient.LeaderCardChoiceMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class able to handle the initialization phase in which the user have to
 * choose the leader card that he will use through the game
 */
public class InitialLeaderCardSelectionPhase extends Phase{
    /**
     * method that handle the initial leader card selection
     * @param cli is the client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        cli.printTemporaryEvolutionSection();
        cli.printTemporaryMarket();

        System.out.println(Constants.ANSI_GREEN + "Before making your choice you should have a look to the market and the evolution section" + Constants.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);
        while(cli.getLeaderCards() == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int index;
        ArrayList<Integer> lCards = new ArrayList<>();


        LeaderCardsPrinter.print(cli.getLeaderCards());
        System.out.println();
        System.out.println(Constants.ANSI_CYAN + "Choose 2 cards" + Constants.ANSI_RESET);

        for(int i = 0; i < 2; i++){
            System.out.print(Constants.ANSI_CYAN + "> " +Constants.ANSI_RESET);
            try{
                index = scanner.nextInt();
            }catch (InputMismatchException e){
                index = 6;
                scanner.nextLine();
            }
            if(index < cli.getLeaderCards().size() && index >= 0 && !lCards.contains(index)){
                lCards.add(index);
            }
            else{
                i--;
                System.out.println(Constants.ANSI_RED + "Error! Card selected is not valid" + Constants.ANSI_RESET);
            }
        }

        cli.getClientSocket().send(new LeaderCardChoiceMessage("Leader card chosen" , lCards));

        try {
            System.out.println(Constants.ANSI_GREEN + "Wait for other players to choose their leader cards" + Constants.ANSI_RESET);
            synchronized (this){
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(cli.isAckArrived()){
            cli.setGamePhase(new InitialResourcesSelection());
            cli.setIsAckArrived(false);
            new Thread(cli).start();
        }else{
            System.err.println(Constants.ANSI_RED + "Error while setting the initial leader card, retry" + Constants.ANSI_RESET);
        }
    }
}
