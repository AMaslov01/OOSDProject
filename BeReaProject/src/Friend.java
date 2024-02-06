import java.util.ArrayList;
import java.util.List;
public class Friend {
    private int friendshipDuration;
    List<User> mutualFriends = new ArrayList<>();

    public int getFriendshipDuration() {
        return friendshipDuration;
    }

    public List<User> getMutualFriends() {
        return mutualFriends;
    }
}
