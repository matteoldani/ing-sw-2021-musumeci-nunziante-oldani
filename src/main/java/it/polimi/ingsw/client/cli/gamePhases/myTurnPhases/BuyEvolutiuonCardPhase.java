package it.polimi.ingsw.client.cli.gamePhases.myTurnPhases;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyEvolutionCardMessage;
import it.polimi.ingsw.utils.Constants;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class able to handle the action of buying an evolution card
 */
public class BuyEvolutiuonCardPhase extends Phase {
    /**
     * method able to handle the action of buying an evolution card
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        cli.printEvolutionSection();

        int row, col, pos;

        do{
            System.out.println(Constants.ANSI_CYAN + "Insert the row and the column of the Evolution Card that you want to buy:"+ Constants.ANSI_RESET);
            System.out.print(Constants.ANSI_CYAN + "Row (0,1,2)> " + Constants.ANSI_RESET);
            try{
                row = scanner.nextInt();
            }catch (InputMismatchException e){
                row = 6;
                scanner.nextLine();
            }
            System.out.print(Constants.ANSI_CYAN + "Column (0,1,2,3)> " + Constants.ANSI_RESET);
            try{
                col = scanner.nextInt();
            }catch (InputMismatchException e){
                col = 6;
                scanner.nextLine();
            }
            System.out.println(Constants.ANSI_CYAN + "Choose in which production zone you want to place the card (0,1,2): " + Constants.ANSI_RESET);
            try{
                pos = scanner.nextInt();
            }catch (InputMismatchException e){
                pos = 6;
                scanner.nextLine();
            }
        }while ((row<0 || row > 2) || (col <0 || col>3) || (pos != 0 && pos != 1 && pos != 2));

        cli.getClientSocket().send(new BuyEvolutionCardMessage("buy evolution card", row, col, pos));
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.isAckArrived()){
            System.out.println(Constants.ANSI_GREEN + "The card has been correctly bought and inserted" + Constants.ANSI_RESET);
            cli.setIsAckArrived(false);
            cli.setActionBeenDone(true);
        }

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();
    }
}
