package ui;

import model.AuthData;
import model.GameDataResponse;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ClientUI {
    public static ClientState userState = ClientState.LOGGED_OUT;

    private ClientRepl clientRepl;
    private ServerFacade facadeServer;
    private ArrayList<GameDataResponse> ListAllGames;

    public ClientUI(String currentURL, ClientRepl currentRepl) {
        fakeServer = new ServerFacade(currentURL);
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

            };




        }

    }

    public String display() {
        String[] options = {
                "Create <NAME>",
                "List Games",
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
            displayOutput.append(" - "));
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




















}
