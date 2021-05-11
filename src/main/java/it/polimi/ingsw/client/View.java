package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.serializableModel.*;

import java.util.ArrayList;

public class View {

    private String activePlayer;
    private String nickname;
    private SerializableDashboard dashboard;
    private ArrayList<SerializableDashboard> enemiesDashboard;
    private SerializableMarket market;
    private SerializableEvolutionSection evolutionSection;
    private ArrayList<SerializableLeaderCard> leaderCards;
    private int score;

    public View(String activePlayer, String nickname, SerializableDashboard dashboard, ArrayList<SerializableDashboard> enemiesDashboard,
                SerializableMarket market, SerializableEvolutionSection evolutionSection, ArrayList<SerializableLeaderCard> leaderCards, int score) {
        this.activePlayer=activePlayer;
        this.nickname = nickname;
        this.dashboard = dashboard;
        this.enemiesDashboard = enemiesDashboard;
        this.market = market;
        this.evolutionSection = evolutionSection;
        this.leaderCards = leaderCards;
        this.score = score;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public String getNickname() {
        return nickname;
    }

    public SerializableDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(SerializableDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public SerializableMarket getMarket() {
        return market;
    }

    public void setMarket(SerializableMarket market) {
        this.market = market;
    }

    public SerializableEvolutionSection getEvolutionSection() {
        return evolutionSection;
    }

    public void setEvolutionSection(SerializableEvolutionSection evolutionSection) {
        this.evolutionSection = evolutionSection;
    }

    public ArrayList<SerializableLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<SerializableLeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<SerializableDashboard> getEnemiesDashboard() {
        return enemiesDashboard;
    }

    public void setEnemiesDashboard(ArrayList<SerializableDashboard> enemiesDashboard) {
        this.enemiesDashboard = enemiesDashboard;
    }
}
