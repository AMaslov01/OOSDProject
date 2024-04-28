package CRUD;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {
    @Test
    void fetchUserIdFromDatabase() {
        var query = new Query();
        assertEquals(2, query.fetchUserIdFromDatabase("user"));
    }

    @Test
    void fetchFriendsIdsFromDatabase() {
        var query = new Query();
        long[] arr = {1, 121, 122, 123};
        assertArrayEquals(arr, query.fetchFriendsIdsFromDatabase(2));
    }

    @Test
    void fetchUserComments() {
        var query = new Query();
        String[][] arr = {{"user", "this is photoshop!"}, {"Pulse23", "ahah"}};
        assertArrayEquals(arr, query.fetchUserComments(258));
    }

    @Test
    void fetchUserBeRealId() {
        var query = new Query();
        assertEquals(254, query.fetchUserBeRealId(2));
    }

    @Test
    void fetchUserNameFromDatabase() {
        var query = new Query();
        assertEquals("user", query.fetchUserNameFromDatabase(2));
    }
}