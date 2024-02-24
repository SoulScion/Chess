package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.security.SecureRandom;
import java.util.HashMap;

public class MemoryAuthDAO {
    private Collection<AuthData> allAuthData = new ArrayList();
    private String privateAuthToken;
    private String privateUsername;

    // private HashMap<authToken, username> authDataHashMap = new HashMap<>();

    // public MemoryAuthDAO(String authToken, String username) {
    //     this.privateAuthToken = authToken;
    //     this.privateUsername = username;
    // }


    public AuthData createAuthData(String username) {

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String authToken = Arrays.toString(bytes); // Creating the authToken, used to authorize.

        AuthData newAuth = new AuthData(authToken, username); // Creating a full AuthData Object.
        allAuthData.add(newAuth); // Adding the new authentication to the list of all authentications.

        return newAuth;
    }

    public Collection<AuthData> listAuthData() {
        return allAuthData;
    }

    public AuthData getAuthData(String authToken) {
        for (AuthData currentAuthData : allAuthData) {
            if (currentAuthData.authToken().equals(authToken)) {
                return currentAuthData;
            }
        }
        return null;
    }

    void deleteAuthData(String authToken) {
        for (AuthData currentAuthData : allAuthData) {
            if (currentAuthData.authToken().equals(authToken)) {
                currentAuthData.authToken() = null;
            }
        }
        return null;
    }

    void deleteAllAuthData() {

    }



}
