package it.polimi.ingsw.client.cli.gamePhases.myTurnPhases;


import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.componentPrinter.ResourcesBoughtPrinter;
import it.polimi.ingsw.client.cli.gamePhases.Phase;
import it.polimi.ingsw.messages.sentByClient.actionMessages.BuyFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.RequestResourcesBoughtFromMarketMessage;
import it.polimi.ingsw.messages.sentByClient.actionMessages.StoreResourcesMessage;
import it.polimi.ingsw.model.cards.LeaderAbility;
import it.polimi.ingsw.model.game.Resource;
import it.polimi.ingsw.serializableModel.SerializableLeaderCard;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * class able to handle the action of buying from market
 */
public class BuyFromMarketPhase extends Phase {
    /**
     * method able to handle the action of buying from market
     * @param cli is client's cli
     */
    @Override
    public void makeAction(CLI cli) {
        Scanner scanner = new Scanner(System.in);
        cli.printMarket();
        int rowChoice;
        do {
            System.out.print(Constants.ANSI_CYAN + "Type 1 to pick a row or 2 for a column: " + Constants.ANSI_RESET);
            try{
                rowChoice = scanner.nextInt();
            }catch (InputMismatchException e){
                rowChoice = 6;
                scanner.nextLine();
            }


            if(rowChoice == 1){
                int row;
                do{
                    System.out.print(Constants.ANSI_CYAN + "Insert the row that you want to buy (0,1,2): " + Constants.ANSI_RESET);
                    try{
                        row = scanner.nextInt();
                    }catch (InputMismatchException e){
                        row = 6;
                        scanner.nextLine();
                    }

                }while (row <0 || row > 2);

                cli.getClientSocket().send(new BuyFromMarketMessage("buy from market", row, true));

            }
            if(rowChoice == 2){
                int col;
                do{
                    System.out.print(Constants.ANSI_CYAN + "Insert the columns that you want to buy (0,1,2,3): " + Constants.ANSI_RESET);
                    try{
                        col = scanner.nextInt();
                    }catch (InputMismatchException e){
                        col = 6;
                        scanner.nextLine();
                    }
                }while (col <0 || col > 3);

                cli.getClientSocket().send(new BuyFromMarketMessage("buy from market", col, false));
            }
        }while (rowChoice != 1 && rowChoice != 2);


        //Resources bought, waiting for ack/nack
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(cli.isAckArrived()){

            cli.setIsAckArrived(false);
            cli.setActionBeenDone(true);
        }else{
            System.out.println(Constants.ANSI_RED + "Error while buying the resources from market!" + Constants.ANSI_RESET);

        }

        //ask the resources
        cli.getClientSocket().send(new RequestResourcesBoughtFromMarketMessage("Bought resources"));

        synchronized (this){
            try {

                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //if i have two "no more white" leader cards active
        if(cli.getClientSocket().getView().getLeaderCards().size() >0 && cli.getClientSocket().getView().getLeaderCards().get(0).getAbilityType() == LeaderAbility.NOMOREWHITE &&
                cli.getClientSocket().getView().getLeaderCards().get(0).isActive() &&
                cli.getClientSocket().getView().getLeaderCards().get(1).getAbilityType() == LeaderAbility.NOMOREWHITE &&
                cli.getClientSocket().getView().getLeaderCards().get(1).isActive()){
            //get every white resource
            ArrayList<Resource> noMoreWhite = new ArrayList<>();
            for (SerializableLeaderCard serializableLeaderCard: cli.getClientSocket().getView().getLeaderCards()){
                for (Resource r : serializableLeaderCard.getAbilityResource().keySet()){
                    if(serializableLeaderCard.getAbilityResource().get(r) != 0){
                        noMoreWhite.add(r);
                    }
                }
            }
            //print the conversion request
            ArrayList<Resource> newRes = new ArrayList<>();

            for (Resource resource: cli.getClientSocket().getView().getResourcesBoughtFromMarker()){
                if(resource == Resource.NOTHING){
                    int choice;
                    do{

                        System.out.println("Convert "+Resource.NOTHING.label+" in: ");
                        System.out.println("1) "+noMoreWhite.get(0).label+"\t 2) " + noMoreWhite.get(1).label);

                        try{
                            choice = scanner.nextInt();
                        }catch (InputMismatchException e){
                            choice = 6;
                            System.out.println(Constants.ANSI_RED +"Error! Cannot find you resources" + Constants.ANSI_RESET);
                        }

                    }while (choice!= 2 && choice!=1);
                    newRes.add(noMoreWhite.get(choice-1));
                }else{
                    newRes.add(resource);
                }
            }

            //update resource bought from market
            cli.getClientSocket().getView().setResourcesBoughtFromMarker(newRes);
        }
        //check if there are only white resources
        int countNotnull = 0;
        for (Resource resource: cli.getClientSocket().getView().getResourcesBoughtFromMarker()){
            if(resource != Resource.NOTHING){
                countNotnull++;
            }
        }

        //if they all were white
        if(countNotnull == 0){
            cli.getClientSocket().send(new StoreResourcesMessage("save resources", cli.getClientSocket().getView().getResourcesBoughtFromMarker()));
            System.out.println(Constants.ANSI_GREEN + "You have bought only white resources" + Constants.ANSI_RESET);
            cli.setGamePhase(new MyTurnPhase());
            new Thread(cli).start();
            return;
        }


        do{
            //print the resources
            ResourcesBoughtPrinter.print(cli.getClientSocket().getView().getResourcesBoughtFromMarker(), 0);
            System.out.println(Constants.ANSI_CYAN + "Choose the resources that you want to save in your stock. " +
                    "You can type 5 to save all the resources or -1 when you have finished." + Constants.ANSI_RESET);

            ArrayList<Integer> positions = new ArrayList<>();
            int positionSelected;

            do{
                System.out.print(Constants.ANSI_CYAN + "> ");
                try{
                    positionSelected = scanner.nextInt();
                }catch (InputMismatchException e){
                    positionSelected = 6;
                    scanner.nextLine();
                }
                if(positionSelected >= 0 &&
                        positionSelected < cli.getClientSocket().getView().getResourcesBoughtFromMarker().size() &&
                        !positions.contains(positionSelected)){
                    positions.add(positionSelected);
                }else{
                    if(positionSelected == 5){
                        positions = new ArrayList<>();
                        for (int j=0; j<cli.getClientSocket().getView().getResourcesBoughtFromMarker().size(); j++){
                            positions.add(j);
                        }
                        break;
                    }else{
                        if(positionSelected != -1)
                            System.out.println(Constants.ANSI_RED + "Error! The resources that you have chosen doesn't exist!" + Constants.ANSI_RESET);
                    }
                }
            }while(positionSelected != -1);

            ArrayList<Resource> resourcesToSend = new ArrayList<>();
            for(Integer integer: positions){
                resourcesToSend.add(cli.getClientSocket().getView().getResourcesBoughtFromMarker().get(integer));
            }
            cli.getClientSocket().send(new StoreResourcesMessage("save selected resources", resourcesToSend));

            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(!cli.isAckArrived());

        System.out.println(Constants.ANSI_GREEN + "Resources correctly saved in stock! \n\n" + Constants.ANSI_RESET);

        cli.setIsAckArrived(false);

        cli.setGamePhase(new MyTurnPhase());
        new Thread(cli).start();

    }
}
