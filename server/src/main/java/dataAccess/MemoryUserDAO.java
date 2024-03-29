package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO {

    private Collection<UserData> allUserData = new ArrayList<>();

    public void createUserData(String username, String password, String email) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashPassword = encoder.encode(password);

        UserData newUser = new UserData(username, hashPassword, email);
        allUserData.add(newUser);

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
        allUserData.clear();
    }

}
