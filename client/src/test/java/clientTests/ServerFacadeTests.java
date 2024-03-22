package clientTests;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ClientAccessException;
import ui.ServerFacade;

import java.util.ArrayList;
import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facadeServer;
    static GameID chessGameID;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var testURL = "http://localhost:" + port;
        facadeServer = new ServerFacade(testURL);
    }

    @BeforeEach
    public void setupTestUsers() {
        try {
            facadeServer.clear();
            facadeServer.register(new UserData("Davy", "Jones", "Locker"));
            chessGameID = facadeServer.createGame(new GameName("ChessGame"));
        } catch (ClientAccessException error) {
            throw new RuntimeException();
        }
    }

    @AfterEach
    public void clear() {
        try {
            facadeServer.clear();
        } catch (ClientAccessException error) {
            System.out.println(error.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() throws ClientAccessException {
        AuthData auth = facadeServer.register(new UserData("User-1", "pass-1", "email-1"));
        Assertions.assertTrue(auth.authToken().length() < 12);

    }

    @Test
    public void registerNegative() throws ClientAccessException {
        facadeServer.register(new UserData("User-1", "pass-1", "email-1"));
        Assertions.assertThrows(ClientAccessException.class, () -> facadeServer.register(new UserData("User-1", "pass-1", "email-1")));

    }

    @Test
    public void loginPositive() throws ClientAccessException {
        Assertions.assertDoesNotThrow(() -> facadeServer.login(new UserData("Davy", "Jones", null)));

    }

    @Test
    public void loginNegative() throws ClientAccessException {
        Assertions.assertThrows(ClientAccessException.class, () -> facadeServer.login(new UserData("notAUser", "WrongPass", null)));

    }

    @Test
    public void logoutPositive() throws ClientAccessException {
        Assertions.assertDoesNotThrow(() -> facadeServer.logout());

    }

    @Test
    public void logoutNegative() throws ClientAccessException {
        Assertions.assertDoesNotThrow(() -> facadeServer.logout());
        Assertions.assertThrows(ClientAccessException.class, () -> facadeServer.logout());

    }

    @Test
    public void createGamePositive() throws ClientAccessException {
        Assertions.assertNotNull(chessGameID);
        Assertions.assertDoesNotThrow(() -> facadeServer.createGame(new GameName("TestGame")));

    }

    @Test
    public void createGameNegative() throws ClientAccessException {
        try {
            facadeServer.logout();
        } catch (ClientAccessException error) {
            throw new RuntimeException();
        }
        Assertions.assertThrows(ClientAccessException.class, () -> facadeServer.createGame(new GameName("TestGame")));

    }

    @Test
    public void listAllGamesPositive() throws ClientAccessException {
        Collection<GameDataResponse> testList = null;

        try {
            testList = facadeServer.listGames();
        } catch (ClientAccessException error) {
            Assertions.fail();
        }
        Collection<GameDataResponse> correctResult = new ArrayList<>();
        correctResult.add(new GameDataResponse(chessGameID.gameID(), null,  null, "ChessGame"));
        Assertions.assertEquals(correctResult, testList);

    }

    @Test
    public void listAllGamesNegative() throws ClientAccessException {
        try {
            facadeServer.logout();
        } catch (ClientAccessException error) {
            Assertions.fail();
        }
        Assertions.assertThrows(ClientAccessException.class, () -> facadeServer.listGames());

    }

    @Test
    public void joinGamePositive() {
        Assertions.assertDoesNotThrow(() -> facadeServer.joinGame(new JoinRequest(chessGameID.gameID(), ChessGame.TeamColor.WHITE)));
        try {
            Collection<GameDataResponse> gameList = facadeServer.listGames();
            Collection<GameDataResponse> correctResult = new ArrayList<>();
            correctResult.add(new GameDataResponse(chessGameID.gameID(), "Davy", null, "ChessGame"));
            Assertions.assertEquals(correctResult, gameList);
        } catch (ClientAccessException error) {
            Assertions.fail();
        }
    }

    @Test
    public void joinGameNegative() {
        Assertions.assertDoesNotThrow(() -> facadeServer.joinGame(new JoinRequest(chessGameID.gameID(), ChessGame.TeamColor.WHITE)));
        Assertions.assertThrows(ClientAccessException.class, () -> facadeServer.joinGame(new JoinRequest(chessGameID.gameID(), ChessGame.TeamColor.WHITE)));

    }

    @Test
    public void clearPositive() {
        try {
            facadeServer.register(new UserData("user-1", "pass-1", "email-1"));
            facadeServer.register(new UserData("user-2", "pass-2", "email-2"));
            facadeServer.register(new UserData("user-3", "pass-3", "email-3"));

            facadeServer.createGame(new GameName("ChessGame-2"));
            facadeServer.createGame(new GameName("ChessGame-3"));
            facadeServer.createGame(new GameName("ChessGame-4"));
        } catch(ClientAccessException error) {
            Assertions.fail();
        }
        Assertions.assertDoesNotThrow(() -> facadeServer.clear());
    }













}
