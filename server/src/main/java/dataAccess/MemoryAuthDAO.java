package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.security.SecureRandom;

public class MemoryAuthDAO implements AuthDAO {
    private Collection<AuthData> allAuthData = new ArrayList<>();

    public AuthData createAuthData(String username) {

        SecureRandom randomCode = new SecureRandom();
        byte numbers[] = new byte[20];
        randomCode.nextBytes(numbers);
        String authToken = numbers.toString(); // Creating the authToken, used to authorize.

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

    public void deleteAllAuthData() {
        for (AuthData currentAuthData : allAuthData) {
            allAuthData.remove(currentAuthData);
        }
    }



}
