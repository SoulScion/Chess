package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO {

    final private Collection<UserData> allUserData = new ArrayList<>();
    private String privateAuthToken;
    private String privateUsername;

    private String privateEmail;


    public UserData createUserData(String username, String password, String email) {
        UserData newUser = null;

        if (getUserData(username) == null) {
            newUser = new UserData(username, password, email);
            allUserData.add(newUser);
        } else {
            // throw DataAccessException("User already exists");
        }

        return newUser;

    }

     public Collection<UserData> listUserData() {
        return allUserData;
    }

    public UserData getUserData(String username) {
        for (UserData currentUserData : allUserData) {
            if (currentUserData.username().equals(username)) {
                return currentUserData;
            }
        }
        return null;
    }

    void deleteUserData(String username) {
        for (UserData currentUserData : allUserData) {
            if (currentUserData.username().equals(username)) {
                currentUserData = new UserData(null, null, null);
            }
        }
    }

    void deleteAllUserData() {
        for (UserData currentUserData : allUserData) {
            currentUserData = new UserData(null, null, null);
        }
    }

}
