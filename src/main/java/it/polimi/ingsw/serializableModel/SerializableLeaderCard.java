package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Serializable class that contains the information needed by the view.
 * Light copy of the LeaderCards.
 *
 */
public class SerializableLeaderCard implements Serializable {
    private int id;
    private LeaderCardRequires requiresForActiveLeaderCards;
    private CardColor[] requiresColor;
    private LevelEnum[] requiresLevel;
    private HashMap<Resource, Integer> requires;
    private LeaderAbility abilityType;
    private int point;
    private HashMap<Resource, Integer> abilityResource;
    private boolean isActive;

    public SerializableLeaderCard(LeaderCard leaderCard) {
        this.requiresForActiveLeaderCards = leaderCard.getRequiresForActiveLeaderCards();
        this.requiresColor = leaderCard.getRequiresColor();
        this.requiresLevel = leaderCard.getRequiresLevel();
        this.requires = leaderCard.getRequires();
        this.abilityType = leaderCard.getAbilityType();
        this.point = leaderCard.getPoint();
        this.abilityResource = leaderCard.getAbilityResource();
        this.isActive = leaderCard.isActive();
        this.id = leaderCard.getId();
    }

    public LeaderCardRequires getRequiresForActiveLeaderCards() {
        return requiresForActiveLeaderCards;
    }

    public CardColor[] getRequiresColor() {
        return requiresColor;
    }

    public LevelEnum[] getRequiresLevel() {
        return requiresLevel;
    }

    public HashMap<Resource, Integer> getRequires() {
        return requires;
    }

    public LeaderAbility getAbilityType() {
        return abilityType;
    }

    public int getPoint() {
        return point;
    }

    public HashMap<Resource, Integer> getAbilityResource() {
        return abilityResource;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getId() {
        return id;
    }
}
