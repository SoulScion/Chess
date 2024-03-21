package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientRepl {
    private ClientUI client;

    public ClientRepl(String clientURL) {
        client = new ClientUI(clientURL, this);
    }

    public void run() {
        Scanner clientScanner = new Scanner(System.in);
        var evalResult = "";

        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.println("Welcome to Cade's CS240 Chess! Read every option and determine what you would like to do!");
        System.out.print(client.help());

        while (evalResult.equals("quit") != true) {
            printUserInputLine();
            String currentLine = clientScanner.nextLine();
            try {
                evalResult = client.eval(currentLine);


            }

        }






    }









    private void printUserInputLine() {
        System.out.print(SET_TEXT_COLOR_WHITE + "\n" + "[" + ClientUI.userState + "] >>> " + SET_TEXT_COLOR_BLUE);
    }


}
