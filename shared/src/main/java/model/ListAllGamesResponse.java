package model;

import java.util.Collection;

public class ListAllGamesResponse {
    private Collection<GameData> games;

    public ListAllGamesResponse(Collection<GameData> games) {
        this.games = games;
    }

    public Collection<GameData> getGames() {
        return games;
    }
}
