import java.util.ArrayList;
import java.util.List;

public class User {
    //attributes
    private int userID;
    private static int counter = 0;
    private String username;
    private String password;
    private String profileInfo;
    private Picture profilePicture;
    private Settings settings;
    List<Friend> friendList = new ArrayList<>();
    List<BeReal> berealMemories = new ArrayList<>();
    List<BeReal> pinnedBereals = new ArrayList<>();

    //constructor
    public User(){
        userID = counter;
        counter ++;
    }
    //setters
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileInfo() {
        return profileInfo;
    }

    //setters
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setProfileInfo(String profileInfo){
        this.password = password;
    }



}
