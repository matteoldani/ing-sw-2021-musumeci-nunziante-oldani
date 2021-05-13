package it.polimi.ingsw.messages.updateMessages;

import it.polimi.ingsw.serializableModel.SerializableEvolutionSection;

import java.io.Serializable;

/**
 * Message sent by server for notify that someone bought by evolution section and its view must be update
 */
public class UpdateEvolutionSectionMessage extends UpdateMessage implements Serializable {

    private SerializableEvolutionSection evolutionSection;

    public UpdateEvolutionSectionMessage(String message, SerializableEvolutionSection serializableEvolutionSection) {
        super(message);
        this.evolutionSection = serializableEvolutionSection;
    }

    public SerializableEvolutionSection getEvolutionSection() {
        return evolutionSection;
    }
}
