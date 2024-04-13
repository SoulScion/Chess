package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import errorExceptions.ServerResponseException;
import model.*;

import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.MakeMoveCommand;
import websocket.NotifyHandler;
import websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import static ui.EscapeSequences.*;

public class ClientUI {
    public static ClientState userState = ClientState.LOGGED_OUT;

    private ServerFacade facadeServer;
    private ArrayList<GameDataResponse> allGames = new ArrayList<>();
    private String serverURL;
    private NotifyHandler repl;
    private ConcurrentHashMap<Integer, GameData> gameObjects;
    private final WebSocketFacade webFacade;
    private GameData overallGameData;
    private ChessGame.TeamColor teamColor;
    private AuthData overallAuthData;

    public ClientUI(String currentURL, NotifyHandler currentRepl) {
        facadeServer = new ServerFacade(currentURL);
        this.serverURL = currentURL;
        this.repl = currentRepl;
        this.webFacade = new WebSocketFacade(this.serverURL, this.repl);

    }

    public String evaluateLine(String inputLine) {
        try {
            var lineTokens = inputLine.split(" ");
            var cmd = (lineTokens.length > 0) ? lineTokens[0] : "help";
            var inputParamaters = Arrays.copyOfRange(lineTokens, 1, lineTokens.length);
            return switch (cmd) {
                case "Register" -> register(inputParamaters);
                case "Login" -> login(inputParamaters);
                case "Logout" -> logout(inputParamaters);
                case "Create" -> createGame(inputParamaters);
                case "List" -> displayAllGames();
                case "Join" -> joinGame(inputParamaters);
                case "Observe" -> observeGame(inputParamaters);
                case "clearDatabase" -> clearData();
                case "Quit" -> "Quit";
                case "Move" -> makeMove(inputParamaters);
                case "Redraw" -> redrawChessBoard();
                case "Highlight" -> highlightMoves(inputParamaters);
                case "Resign" -> resignGame();
                case "Leave" -> leaveGame();
                default -> displayHelp();

            };
        } catch (ServerResponseException error) {
            return "ERROR: 500, Server";
        } catch (ClientAccessException error) {
            return "ERROR: 500, Client";
        }


    }

    public String displayHelp() {
        String[] options = {"Create {NAME}", "List", "Join <ID> [WHITE|BLACK|{empty}]", "Observe {ID}", "Logout", "Quit", "Help"};
        String[] smallDescriptions = {"Allows a user to create a game.",
                "Lists all games for a user.",
                "Allows a user to join a game.",
                "Allows a user to observe a game.",
                "Logs a user out.",
                "Tells the server when a user is finished playing.",
                "Lists all possible options and longer descriptions (No need to use this command."};
        if (userState == ClientState.LOGGED_OUT) {
            options = new String[]{"Register {USERNAME} {PASSWORD} {EMAIL}", "Login {USERNAME} {PASSWORD}", "Quit", "Help"};
            smallDescriptions = new String[]{"create an account", "login and play", "stop playing", "list possible options"};
        } else if (userState == ClientState.IN_GAME) {
            options = new String[]{"Redraw", "Move {START} {END} [{PIECE}|{empty}]", "Highlight {START}", "Resign", "Leave", "Help"};
            smallDescriptions = new String[]{"Allows a user to redraw the chessboard.", "Allows a user to move a piece from {START} position to {END} position with {PIECE} as possible pawn promotion.", "Allows a user to highlight all legal moves from {START}.", "Allows a user to resign the game.", "Allows a user to leave the game.", "Displays options and their details."};
        } else if (userState == ClientState.RESIGNED) {
            options = new String[]{"Redraw", "Leave", "Help"};
            smallDescriptions = new String[]{"Allows a user to redraw the chessboard.", "Allows a user to eaves the game.", "Displays options and their details."};
        }

        StringBuilder displayOutput = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            displayOutput.append(SET_TEXT_COLOR_BLUE);
            displayOutput.append(" - ");
            displayOutput.append(options[i]);
            displayOutput.append(SET_TEXT_COLOR_WHITE);
            displayOutput.append(" - ");
            displayOutput.append(smallDescriptions[i]);
            displayOutput.append('\n');
        }
        return displayOutput.toString();

    }

    private String leaveGame() throws ServerResponseException {
        if (userState != ClientState.IN_GAME && userState != ClientState.RESIGNED) {
            throw new ServerResponseException(400, "Only available in game");
        }
        webFacade.leave(overallAuthData.authToken(), overallGameData.gameID());
        userState = ClientState.LOGGED_IN;
        return "";
    }

    private String resignGame() throws ServerResponseException {
        if (userState != ClientState.IN_GAME) {
            throw new ServerResponseException(400, "Only available in game");
        }
        webFacade.resign(overallAuthData.authToken(), overallGameData.gameID());
        userState = ClientState.RESIGNED;
        return "";
    }

    private String highlightMoves(String[] params) throws ServerResponseException {
        if (userState != ClientState.IN_GAME) {
            throw new ServerResponseException(400, "Only available in game");
        }
        var startPos = parsePosition(params[0]);
        addNewGame();
        ChessBoardUI.highlight(overallGameData.game(), teamColor, startPos);
        return "";
    }

    private String redrawChessBoard() throws ServerResponseException {
        if (userState != ClientState.IN_GAME) {
            throw new ServerResponseException(400, "Only available in game");
        }
        webFacade.getGame(overallAuthData.authToken(), overallGameData.gameID());
        return "";
    }

    private String makeMove(String[] inputParameters) throws ServerResponseException {
        if (inputParameters.length < 2) {
            throw new ServerResponseException(400, "Invalid move");
        }
        var start = inputParameters[0];
        var end = inputParameters[1];
        ChessPosition startPosition = parsePosition(start.toLowerCase());
        ChessPosition endPosition = parsePosition(end.toLowerCase());

        ChessPiece.PieceType promotionPiece = null;
        if (inputParameters.length == 3) {
            promotionPiece = ChessPiece.PieceType.valueOf(inputParameters[2].toUpperCase());
        }

        ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
        webFacade.makeMove(overallAuthData.authToken(), overallGameData.gameID(), move);

        return "";
    }

    private ChessPosition parsePosition(String pos) throws ServerResponseException {
        if (pos.length() != 2) {
            throw new ServerResponseException(400, "Invalid position: " + pos);
        }
        var col = pos.charAt(0) - 96;
        var row = pos.charAt(1) - 48;
        return new ChessPosition(row, col);
    }

    private String register(String[] inputParameters) {
        if (userState == ClientState.LOGGED_IN) {
            return "In order to register a new user, you must be logged out.";
        }

        if (inputParameters.length == 3) {
            UserData newUser = new UserData(inputParameters[0], inputParameters[1], inputParameters[2]);


            try {
                overallAuthData = facadeServer.register(newUser);
            } catch (ClientAccessException error) {
                return "Invalid Authorization";
            }

            userState = ClientState.LOGGED_IN;
            return "You are now logged in as " + overallAuthData.username();
        }
        return "Invalid Authorization";

    }

    private String login(String[] inputParameters) {
        if (userState == ClientState.LOGGED_IN) {
            return "In order to login a new user, you must be logged out.";
        }

        if (inputParameters.length == 2) {
            UserData newUser = new UserData(inputParameters[0], inputParameters[1], null);


            try {
                overallAuthData = facadeServer.login(newUser);
            } catch (ClientAccessException error) {
                return "Invalid Authorization";
            }

            userState = ClientState.LOGGED_IN;
            return "You are now logged in as " + overallAuthData.username();
        }
        return "Invalid Authorization";

    }

    private String logout(String[] inputParameters) {
        if (userState == ClientState.LOGGED_OUT) {
            return "In order to logout an existing user, you must be logged in.";
        }

        userState = ClientState.LOGGED_OUT;

        try {
            facadeServer.logout();
        } catch (ClientAccessException error) {
            return "Problem Occurred: Logout Failed";
        }
        return "Successfully logged out user.";

    }

    private String createGame(String[] inputParameters) {
        if (userState == ClientState.LOGGED_OUT) {
            return "Must login first";
        }
        String name = String.join(" ", inputParameters);
        GameID gameID;
        try {
            gameID = facadeServer.createGame(new GameName(name));
            addNewGame();
            return "You created a new game and it has a name: " + name + " and game ID: " + gameID;
//            for (int idx = 0; idx < allGames.size(); idx ++) {
//                if (allGames.get(idx).gameID() == gameID.gameID()) {
//                    return "Game " + name + " created with ID " + (idx + 1);
//                }
//            }
            //return "Error creating game, please try again";
        } catch (ClientAccessException e) {
            return "Couldn't create game with that name, try again.";
        }
    }

    private String displayAllGames() {
        if (userState == ClientState.LOGGED_OUT) {
            return "In order to list all chess games, you must be logged in.";
        }
        try {
            allGames = facadeServer.listGames();
            gameObjects = facadeServer.getObjectsGame();
        } catch (ClientAccessException e) {
            return "Couldn't list game with that name, try again.";
        }

        return createListOfGames();

    }

    private String joinGame(String[] inputParameters) {
        if (userState == ClientState.LOGGED_OUT) {
            return "In order to join a game, you must be logged in.";
        }

        try {
            addNewGame();
            int idx = Integer.parseInt(inputParameters[0]);
            int gameID = allGames.get(idx - 1).gameID();
            overallGameData = gameObjects.get(gameID);

            ChessGame.TeamColor color = parseColor(inputParameters.length == 2 ? inputParameters[1].toLowerCase() : null);
            if (color == null) {
                return "Invalid Color Chosen.";
            }

            if (allGames.get(idx - 1).spaceOccupied(color)) {
                return String.format("%s is already taken.", color.name().toLowerCase());
            }

            facadeServer.joinGame(new JoinRequest(overallGameData.gameID(), color));
            webFacade.joinPlayer(overallAuthData.authToken(), overallGameData.gameID(), color);
            userState = ClientState.IN_GAME;
            teamColor = color;
            return "";
        } catch (IndexOutOfBoundsException error) {
            return "Game requested doesn't exist";
        } catch (ServerResponseException error) {
            return "Failed join and observe game, try again.";
        }catch (ClientAccessException error) {
            return "Failed to join and observe game, try again.";
        } catch (NumberFormatException error) {
            return "Invalid Input";
        }
    }

    private String observeGame(String[] inputParameters) {
        if (userState == ClientState.LOGGED_OUT) {
            return "In order to join a game, you must be logged in.";
        }

        try {
            addNewGame();
            int idx = Integer.parseInt(inputParameters[0]);
            int gameID = allGames.get(idx - 1).gameID();
            overallGameData = gameObjects.get(gameID);

            facadeServer.joinGame(new JoinRequest(overallGameData.gameID(), null));
            webFacade.joinPlayer(overallAuthData.authToken(), overallGameData.gameID(), null);
            userState = ClientState.IN_GAME;
            return "";
        } catch (IndexOutOfBoundsException error) {
            return "Game requested doesn't exist";
        } catch (ServerResponseException error) {
            return "Failed join and observe game, try again.";
        }catch (ClientAccessException error) {
            return "Failed to join and observe game, try again.";
        } catch (NumberFormatException error) {
            return "Invalid Input";
        }
    }

    private String clearData() throws ClientAccessException {
        facadeServer.clear();
        return "Database Deleted";
    }

    private ChessGame.TeamColor parseColor(String color) {
        if (color != null && (color.equals("white") || color.equals("black"))) {
            return ChessGame.TeamColor.valueOf(color.toUpperCase());
        }
        return null;
    }


    private void addNewGame() {
        try {
            allGames = facadeServer.listGames();
            gameObjects = facadeServer.getObjectsGame();
        } catch (ClientAccessException e) {
            System.out.print("Couldn't list game with that name, try again.");
        }

    }


//        try {
//            var newGames = facadeServer.listGames();
//            gameObjects = facadeServer.getObjectsGame();
//            ArrayList<GameDataResponse> tempGames = new ArrayList<>();
//
//            if (newGames == null) {
//                return;
//            }

//            if (allGames != null) {
//                for (var currGame : allGames) {
//                    for (var newGame : newGames) {
//                        if (Objects.equals(newGame.gameID(), currGame.gameID())) {
//                            tempGames.add(newGame);
//                        }
//                    }
//                }
//                for (var newGame : newGames) {
//                    boolean found = false;
//                    for (var currGame : allGames) {
//                        if (Objects.equals(newGame.gameID(), currGame.gameID())) {
//                            found = true;
//                            break;
//                        }
//                    }
//
//                    if (!found) {
//                        tempGames.add(newGame);
//                    }
//                }
//            } else {
//                tempGames = newGames;
//            }
//            allGames = tempGames;
//
//            if (overallGameData != null) {
//                overallGameData = gameObjects.get(overallGameData.gameID());
//            }
//        } catch (ClientAccessException e) {
//            System.out.println(e.getMessage());
//        }

    private String createListOfGames(){
        StringBuilder listString = new StringBuilder();
        listString.append("_____________________________________________________\n");
        listString.append(String.format("| ID  | %-14s| %-14s| %-12s|\n", "White Player", "Black Player", "Game Name"));
        listString.append("_____________________________________________________\n");
        for (int counter = 0; counter < allGames.size(); counter++) {
            var currentGame = allGames.get(counter);
            listString.append(String.format("| %-4d| %-14s| %-14s| %-12s|\n", counter+1, currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName()));

            listString.append("_____________________________________________________\n");
        }
        return String.valueOf(listString);

    }


























}
