package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exception.InvalidPlaceException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.EvolutionCard;
import it.polimi.ingsw.model.cards.LevelEnum;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Place of the dashboard where are stored Evolution Cards
 */
public class NormalProductionZone extends ProductionZone implements Serializable {

    /**
     * List of cards bought
     */
    private ArrayList<EvolutionCard> cards;

    public NormalProductionZone() {
        cards = new ArrayList<>();
    }

    /**
     * @return the most recent card bought
     */
    public Card getCard() {
        return cards.isEmpty() ? null : cards.get(0);
    }

    /**
     * Check if there is at least a free space in the slot
     *
     * @return true if the slot isn't full
     */
    public boolean isFull() {
        return cards.size() > 2;
    }

    /**
     * Add new card to production zone
     * @param card bought from Evolution Section
     */
    public void addCard(Card card) throws InvalidPlaceException {
        EvolutionCard evolutionCard = (EvolutionCard) card;
        if (isFull() ||
                (!(cards.isEmpty()) && ( cards.get(0).getLevel().getValue() != evolutionCard.getLevel().getValue() - 1)) ||
                ((cards.isEmpty()) && (evolutionCard.getLevel()!= LevelEnum.FIRST))) {
            throw new InvalidPlaceException("Invalid Position for adding the card");
        }
        cards.add(0, (EvolutionCard) card);
        notifyProductionZoneListener(this);
    }

    /**
     * @return array of Evolution Cards present in the slot
     */
    public ArrayList<Card> getCardList() {
        return cards.isEmpty() ? null : (ArrayList<Card>) cards.clone();
    }

    /**
     *
     * @return level of the last card
     */
    public LevelEnum getLevel() {
        return cards.isEmpty() ? null : cards.get(0).getLevel();
    }

    public void setCards(ArrayList<EvolutionCard> cards) {
        this.cards = cards;
    }
}
