package ui;

import java.util.Scanner;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotifyMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotifyHandler;

import static ui.EscapeSequences.*;

public class ClientRepl implements NotifyHandler{
    private final ClientUI clientUI;

    public ClientRepl(String serverUrl) {
        clientUI = new ClientUI(serverUrl, this);
    }

    public void run() {
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.println("\uD83D\uDC51 Welcome to 240 Chess. Type Help to get started. \uD83D\uDC51");
        System.out.print(clientUI.displayHelp());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = clientUI.evaluateLine(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }

        System.out.println();
    }

    private void printPrompt() {
        System.out.print(SET_TEXT_COLOR_BLACK + "\n" + "[" + ClientUI.userState + "] >>> " + SET_TEXT_COLOR_GREEN);
    }
    public void notifyMessage(NotifyMessage notifyMessage) {
        System.out.print(SET_TEXT_COLOR_RED + notifyMessage.getNotifyMessage());
        printPrompt();
    }

    public void loadGameMessage(LoadGameMessage loadMessage) {
        var gameLoad = loadMessage.getNewGame();
        var colorLoad = loadMessage.getTeamColor();
        ChessBoardUI.main(gameLoad.game(), colorLoad);
        printPrompt();
    }

    public void errorMessage(ErrorMessage errorMessage) {
        System.out.print(SET_TEXT_COLOR_YELLOW + errorMessage.getErrorMessage());
        printPrompt();
    }
}
