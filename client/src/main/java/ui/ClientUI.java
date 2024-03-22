package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ClientUI {
    public static ClientState userState = ClientState.LOGGED_OUT;

    private ClientRepl clientRepl;
    private ServerFacade facadeServer;
    private ArrayList<GameDataResponse> listOfGames;

    public ClientUI(String currentURL, ClientRepl currentRepl) {
        facadeServer = new ServerFacade(currentURL);
        this.clientRepl = currentRepl;
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
                case "Join", "Observe" -> joinGame(inputParamaters);
                case "clearDatabase" -> clearData();
                case "Quit" -> "Quit";
                default -> display();

            };

        } catch (ClientAccessException error) {
            return "ERROR: 500";
        }

    }

    public String display() {
        String[] options = {
                "Create <NAME>",
                "List",
                "Join <ID> [White|Black|<empty>]",
                "Observe <ID>",
                "Logout",
                "Help",
                "Quit"
        };
        String[] smallDescriptions = {
                "Creates a game given a name.",
                "List all games currently on the server.",
                "Join a game to play or spectate.",
                "Spectate a ongoing game.",
                "Logout from this server.",
                "Goes further into detail on all commands. (This displays nothing of actual use.)",
                "Go back to the main menu."
        };

        if (userState == ClientState.LOGGED_OUT) {
            options = new String[]{
                    "Register <USERNAME> <PASSWORD> <EMAIL>",
                    "Login <USERNAME> <PASSWORD>",
                    "Help",
                    "Quit"
            };
            smallDescriptions = new String[]{
                    "Register yourself to the server.",
                    "Login to the server in order to play.",
                    "Goes further into detail on all commands. (This displays nothing of actual use.)",
                    "Exit the Chess Application."
            };
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

    private String register(String[] inputParameters) {
        if (userState == ClientState.LOGGED_IN) {
            return "In order to register a new user, you must be logged out.";
        }

        if (inputParameters.length == 3) {
            UserData newUser = new UserData(inputParameters[0], inputParameters[1], inputParameters[2]);
            AuthData newUserAuth;

            try {
                newUserAuth = facadeServer.register(newUser);
            } catch (ClientAccessException error) {
                return "Invalid Authorization";
            }

            userState = ClientState.LOGGED_IN;
            return "You are now logged in as" + newUserAuth.username();
        }
        return "Invalid Authorization";

    }

    private String login(String[] inputParameters) {
        if (userState == ClientState.LOGGED_IN) {
            return "In order to login a new user, you must be logged out.";
        }

        if (inputParameters.length == 2) {
            UserData newUser = new UserData(inputParameters[0], inputParameters[1], null);
            AuthData newUserAuth;

            try {
                newUserAuth = facadeServer.login(newUser);
            } catch (ClientAccessException error) {
                return "Invalid Authorization";
            }

            userState = ClientState.LOGGED_IN;
            return "You are now logged in as" + newUserAuth.username();
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
            return "In order to create a new chess game, you must be logged in.";
        }
        String gameName = String.join(" ", inputParameters);
        GameID newGameID;
        try {
            newGameID = facadeServer.createGame(new GameName(gameName));
            addNewGame();
            for (int counter = 0; counter < listOfGames.size(); counter++) {
                if (listOfGames.get(counter).gameID() == newGameID.gameID()) {
                    return "Game Created: " + gameName + " ID: " + (counter + 1);
                }
            }
            return "Error: Failed to create game. Please try again.";
        } catch (ClientAccessException error) {
            return "Name already taken, please try a different game name.";
        }
    }

    private String displayAllGames() {
        if (userState == ClientState.LOGGED_OUT) {
            return "In order to list all chess games, you must be logged in.";
        }
        addNewGame();
        return createListOfGames();

    }

    private String joinGame(String[] inputParameters) {
        if (userState == ClientState.LOGGED_OUT) {
            return "In order to join a game, you must be logged in.";
        }
        int counter = Integer.parseInt(inputParameters[0]);
        try {
            addNewGame();
            var currentGame = listOfGames.get(counter - 1);
            if (inputParameters.length == 1) {
                try {
                    facadeServer.joinGame(new JoinRequest(currentGame.gameID(), null));
                } catch (ClientAccessException error) {
                    return "Error: Failed to join and Observe Game.";
                }
                ChessBoardUI.main(new String[]{new ChessBoard().toString()});
                return "";

            } else if (inputParameters.length == 2) {
                if (Objects.equals(inputParameters[1].toLowerCase(), "white")) {
                    if (currentGame.whiteUsername() != null) {
                        return "White team is taken. Please, try again.";
                    }

                    ChessGame.TeamColor userColor = ChessGame.TeamColor.WHITE;
                    try {
                        facadeServer.joinGame(new JoinRequest(currentGame.gameID(), userColor));
                    } catch (ClientAccessException error) {
                        return "Error: Failed to join and Observe Game as White.";
                    }
                } else if (Objects.equals(inputParameters[1].toLowerCase(), "black")) {
                    if (currentGame.whiteUsername() != null) {
                        return "Black team is taken. Please, try again.";
                    }

                    ChessGame.TeamColor userColor = ChessGame.TeamColor.BLACK;
                    try {
                        facadeServer.joinGame(new JoinRequest(currentGame.gameID(), userColor));
                    } catch (ClientAccessException error) {
                        return "Error: Failed to join and Observe Game as Black.";
                    }

                } else {
                    return "Gave an Invalid Color.";
                }
                ChessBoardUI.main(new String[]{new ChessBoard().toString()});
                return "";
            }
        } catch (IndexOutOfBoundsException error) {
            return "The game your trying to join doesn't exist.";
        }
        return "Given an invalid input";

    }

    private String clearData() throws ClientAccessException {
        facadeServer.clear();
        return "Database Deleted";
    }


    private void addNewGame() {
        try {
            var currentGames = facadeServer.listGames();
            ArrayList<GameDataResponse> tempList = new ArrayList<>();

            if (listOfGames != null) {
                for (var currentInList : listOfGames) {
                    for (var newGame : currentGames) {
                        if (Objects.equals(newGame.gameID(), currentInList.gameID())) {
                            tempList.add(newGame);
                        }
                    }
                }
                for (var newGame : currentGames) {
                    boolean gameSearch = false;
                    for (var currentInList : listOfGames) {
                        if (Objects.equals(newGame.gameID(), currentInList.gameID())) {
                            gameSearch = true;
                            break;
                        }
                    }
                    if (!gameSearch) {
                        tempList.add(newGame);
                    }
                }
            } else {
                tempList = currentGames;
            }
            listOfGames = tempList;

        } catch (ClientAccessException error) {
            System.out.println(error.getMessage());
        }
    }

    private String createListOfGames(){
        StringBuilder listString = new StringBuilder();
        listString.append("_____________________________________________________\n");
        listString.append(String.format("| ID  | %-14s| %-14s| %-12s|\n", "White Player", "Black Player", "Game Name"));
        listString.append("_____________________________________________________\n");
        for (int counter = 0; counter < listOfGames.size(); counter++) {
            var currentGame = listOfGames.get(counter);
            String.format("| %-4d| %-14s| %-14s| %-12s|\n", counter+1, currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName());
            listString.append("_____________________________________________________\n");
        }
        return String.valueOf(listString);

    }


























}
