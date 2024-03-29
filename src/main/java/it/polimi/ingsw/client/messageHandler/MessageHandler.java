package it.polimi.ingsw.client.messageHandler;

import it.polimi.ingsw.client.ClientSocket;
import it.polimi.ingsw.messages.sentByServer.*;
import it.polimi.ingsw.messages.sentByServer.configurationMessagesServer.*;
import it.polimi.ingsw.messages.sentByServer.updateMessages.*;

public abstract class MessageHandler {

    protected ClientSocket clientSocket;


    public abstract void handleMessage(ACKMessage message);
    public abstract void handleMessage(NACKMessage message);
    public abstract void handleMessage(ReconnectionMessage message);
    public abstract void handleMessage(StartGameMessage message);
    public abstract void handleMessage(FourLeaderCardsMessage message);
    public abstract void handleMessage(InitialResourcesMessage message);
    public abstract void handleMessage(SendViewMessage message);
    public abstract void handleMessage(SendResourcesBoughtFromMarket message);
    public abstract void handleMessage(EndGameMessage message);
    public abstract void handleMessage(PersistenceMessage message);

    public abstract void handleUpdateMessage(UpdateLeaderCardsMessage message);
    public abstract void handleUpdateMessage(UpdateDashBoardMessage message);
    public abstract void handleUpdateMessage(UpdateActivePlayerMessage message);
    public abstract void handleUpdateMessage(UpdateEvolutionSectionMessage message);
    public abstract void handleUpdateMessage(UpdateMarketMessage message);
    public abstract void handleUpdateMessage(UpdateOtherPlayerViewMessage message);

    public abstract void handleMessage(AbortGameMessage abortGameMessage);

    public abstract void handleMessage(MarketAndEvolutionSectionMessage marketMessage);
}

