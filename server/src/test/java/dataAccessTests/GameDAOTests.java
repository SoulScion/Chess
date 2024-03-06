package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameDAOTests {

    @Test
    public void createGamePos() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            gameSQL.createGameData("game");
            Assertions.assertNotNull(gameSQL.listGameData());

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void createGameNeg() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            Assertions.assertThrows(DataAccessException.class, () -> gameSQL.createGameData(null));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void updateGamePos() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            int gameID = gameSQL.createGameData("game");
            GameData game = gameSQL.getGameData(gameID);

            gameSQL.updateGameData(1, "white", "black", "game", new ChessGame());

            GameData changedGame = gameSQL.getGameData(gameID);

            Assertions.assertNotEquals(game.whiteUsername(), changedGame.whiteUsername());
            Assertions.assertNotEquals(game.blackUsername(), changedGame.blackUsername());

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void updateGameNeg() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            int gameID = gameSQL.createGameData("game");
            GameData game = gameSQL.getGameData(gameID);

            gameSQL.updateGameData(1, null, null, "game", new ChessGame());

            GameData changedGame = gameSQL.getGameData(gameID);

            Assertions.assertEquals(game.whiteUsername(), changedGame.whiteUsername());
            Assertions.assertEquals(game.blackUsername(), changedGame.blackUsername());

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }




    @Test
    public void getGamePos() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            gameSQL.createGameData("game");
            gameSQL.createGameData("game-1");
            gameSQL.createGameData("game-2");
            Assertions.assertNotNull(gameSQL.getGameData(1));
            Assertions.assertNotNull(gameSQL.getGameData(2));
            Assertions.assertNotNull(gameSQL.getGameData(3));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void getGameNeg() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            gameSQL.createGameData("game");
            gameSQL.createGameData("game-1");
            gameSQL.createGameData("game-2");
            Assertions.assertNull(gameSQL.getGameData(6));
            Assertions.assertNull(gameSQL.getGameData(20));
            Assertions.assertNull(gameSQL.getGameData(9));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void listGamePos() throws DataAccessException {

        try {
            GameDAO gameSQL = new SQLGameDAO();
            gameSQL.createGameData("game");
            gameSQL.createGameData("game-1");
            gameSQL.createGameData("game-2");
            gameSQL.createGameData("game-3");
            Assertions.assertEquals(gameSQL.listGameData().size(), 4);

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void listGameNeg() throws DataAccessException {

        GameDAO gameSQL = null;
        try {
            gameSQL = new SQLGameDAO();
            gameSQL.createGameData("game");
            gameSQL.createGameData("game-1");
            gameSQL.createGameData("game-2");
            gameSQL.createGameData("game-3");
            gameSQL.createGameData(null);

        } catch (Exception e) {
            Assertions.assertEquals(gameSQL.listGameData().size(), 4);

        }
    }

    @Test
    public void deleteAllGamesPos() throws DataAccessException {


        try {
            GameDAO gameSQL = new SQLGameDAO();
            gameSQL.createGameData("game");
            gameSQL.createGameData("game-1");
            gameSQL.createGameData("game-2");
            gameSQL.createGameData("game-3");
            gameSQL.deleteAllGameData();

            Assertions.assertEquals(gameSQL.listGameData().size(), 0);

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
    }





}
