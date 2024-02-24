package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO {

    private Collection<UserData> allUserData = new ArrayList<>();

    public UserData createUserData(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        allUserData.add(newUser);

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

    public void deleteAllUserData() {
        for (UserData currentUserData : allUserData) {
            allUserData.remove(currentUserData);
        }
    }

}
