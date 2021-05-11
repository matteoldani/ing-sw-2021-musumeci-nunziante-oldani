package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.messages.Message;

/**
 * Generic structure of update messages
 */
public abstract class UpdateMessage extends Message {

    public UpdateMessage(String message) {
        super(message);
    }
}
