package ui;

import model.GameDataResponse;

import java.util.ArrayList;

public class ClientUI {
    public static ClientState userState = ClientState.LOGGED_OUT;

    private ClientRepl clientRepl;
    private ServerFacade fakeServer;
    private ArrayList<GameDataResponse> ListAllGames;

    public ClientUI(String currentURL, ClientRepl currentRepl) {
        fakeServer = new ServerFacade(currentURL);
        this.clientRepl = currentRepl;
    }

    public String help() {
        String helpString = " Each option is very self-explanatory in name. Good luck in all of your games!";
        return helpString;
    }




















}
