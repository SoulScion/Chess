package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.DataAccessException;
import errorExceptions.ServerResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.CreateGameService;
import service.LoginService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotifyMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final WebConnectionManager webConnections = new WebConnectionManager();

    private final CreateGameService createGameService;
    private final LoginService loginService;

    public WebSocketHandler(CreateGameService createNewGameService, LoginService loginService) {
        this.createGameService = createNewGameService;
        this.loginService = loginService;
    }

    @OnWebSocketMessage
    public void OnWebMessage(Session connectionSession, String message) throws IOException {
        try {
            JsonObject object = JsonParser.parseString(message).getAsJsonObject();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(object.get("commandType").getAsString());
            switch (commandType) {
                case JOIN_PLAYER -> PlayerJoin(new Gson().fromJson(message, PlayerJoinCommand.class), connectionSession);
                case JOIN_OBSERVER -> ObserverJoin(new Gson().fromJson(message, ObserverJoinCommand.class), connectionSession);
                case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMoveCommand.class));
                case LEAVE -> playerLeaveGame(new Gson().fromJson(message, LeaveCommand.class));
                case RESIGN -> playerResignGame(new Gson().fromJson(message, ResignCommand.class));
                case GET_GAME -> getGame(new Gson().fromJson(message, GetGameCommand.class));
            }
        } catch (Exception error) {
            System.out.printf("Error occurred: %s%n", error.getMessage());
            var errorNotif = new ErrorMessage(error.getMessage());
            connectionSession.getRemote().sendString(new Gson().toJson(errorNotif));
        }
    }

    private void getGame(GetGameCommand getGameCommand) throws DataAccessException, IOException {
        var gameData = this.createGameService.getGameInCreateService(getGameCommand.getRetrieveGameID());
        String username = loginService.getUser(getGameCommand.getAuthString());

        var loadGame = new LoadGameMessage(gameData);
        webConnections.sendMessageConnection(username, new Gson().toJson(loadGame));
    }

    private void playerResignGame(ResignCommand resignCommand) throws DataAccessException, IOException, ServerResponseException {
        String username = loginService.getUser(resignCommand.getAuthString());
        var gameData = this.createGameService.getGameInCreateService(resignCommand.getResignGameID());

        if (!Objects.equals(gameData.whiteUsername(), username) && !Objects.equals(gameData.blackUsername(), username)) {
            throw new ServerResponseException(401, "Can't resign as an observer");
        }
        if (gameData.game().getTeamTurn() == ChessGame.TeamColor.NONE) {
            throw new ServerResponseException(400, "Already resigned");
        }

        gameData.game().setTeamTurn(ChessGame.TeamColor.NONE);
        createGameService.updateGameInCreateService(gameData);

        var ResignNotif = new NotifyMessage(String.format("Player %s has resigned the game.", username));
        webConnections.generalBroadcast(null, new Gson().toJson(ResignNotif));
    }

    private void playerLeaveGame(LeaveCommand leaveCommand) throws DataAccessException, IOException {
        String loginUsername = loginService.getUser(leaveCommand.getAuthString());
        webConnections.removeConnection(loginUsername);
        var LeaveNotif = new NotifyMessage(String.format("Player %s has left the game.", loginUsername));
        webConnections.generalBroadcast(loginUsername, new Gson().toJson(LeaveNotif));
    }

    private void makeMove(MakeMoveCommand moveCommand) throws DataAccessException, InvalidMoveException, IOException, ServerResponseException {
        String username = loginService.getUser(moveCommand.getAuthString());
        var gameData = this.createGameService.getGameInCreateService(moveCommand.getMoveGameID());
        ChessGame.TeamColor turnColor = gameData.game().getTeamTurn();
        String turn = switch (turnColor) {
            case WHITE -> gameData.whiteUsername();
            case BLACK -> gameData.blackUsername();
            case NONE -> throw new InvalidMoveException("Game is over, can't make moves");
        };

        if (!Objects.equals(turn, username)) {
            throw new ServerResponseException(400, "Not your turn");
        }

        gameData.game().makeMove(moveCommand.getMove());
        this.createGameService.updateGameInCreateService(gameData);
        webConnections.broadcastChessGame(new LoadGameMessage(gameData));
        var moveNotif = new NotifyMessage("Move " + moveCommand.getMove().toString() + " made by " + username);
        webConnections.generalBroadcast(username, new Gson().toJson(moveNotif));
    }

    private void ObserverJoin(ObserverJoinCommand observeCommand, Session session) throws DataAccessException, IOException, ServerResponseException {
        String loginUsername = loginService.getUser(observeCommand.getAuthString());
        webConnections.addConnection(loginUsername, session);
        var gameServiceData = this.createGameService.getGameInCreateService(observeCommand.getObserverGameID());
        if (gameServiceData == null) {
            throw new ServerResponseException(400, "Invalid game id");
        }
        var text = String.format("Player %s has joined as observer", loginUsername);
        var joinNotif = new NotifyMessage(text);
        webConnections.generalBroadcast(loginUsername, new Gson().toJson(joinNotif));
        sendGame(gameServiceData, ChessGame.TeamColor.WHITE, loginUsername);
    }

    public void PlayerJoin(PlayerJoinCommand command, Session session) throws DataAccessException, IOException {
        ChessGame.TeamColor playerColor = command.getPlayerColor();
        String loginUsername = loginService.getUser(command.getAuthString());
        webConnections.addConnection(loginUsername, session);
        var gameData = this.createGameService.getGameInCreateService(command.getPlayerGameID());
        if (!loginUsername.equals(getUsername(gameData, playerColor))) {
            var joinErrorMessage = new ErrorMessage("Can't join as " + command.getPlayerColor().toString());
            webConnections.sendMessageConnection(loginUsername, new Gson().toJson(joinErrorMessage));
        } else {
            var text = String.format("Player %s has joined as %s%n", loginUsername, command.getPlayerColor());
            var joinNotif = new NotifyMessage(text);
            webConnections.generalBroadcast(loginUsername, new Gson().toJson(joinNotif));
            sendGame(gameData, playerColor, loginUsername);
        }
    }

    public void sendGame(GameData game, ChessGame.TeamColor color, String player) throws IOException {
        var gameMessage = new LoadGameMessage(game);
        gameMessage.setNewColor(color);
        webConnections.sendMessageConnection(player, new Gson().toJson(gameMessage));
    }

    private String getUsername(GameData gameData, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return gameData.whiteUsername();
        } else {
            return gameData.blackUsername();
        }
    }







}
