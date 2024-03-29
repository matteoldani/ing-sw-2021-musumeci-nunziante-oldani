package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.MessageHandler;

import java.io.Serializable;

/**
 * Generic structure of messages
 */
public abstract class Message implements Serializable {

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage(){ return message;}


}
