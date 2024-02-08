public class Music {
    private String titleID;
    private String title;
    private String artist;
    private int durationInSeconds;
    private String genre;

    public Music(String titleID, String title, String artist, int durationInSeconds, String genre) {
        this.titleID = titleID;
        this.title = title;
        this.artist = artist;
        this.durationInSeconds = durationInSeconds;
        this.genre = genre;
    }

    // Getters and setters
    public String getTitleID() {
        return titleID;
    }

    public void setTitleID(String titleID) {

        this.titleID = titleID;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // toString method to represent Music as a string
    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", durationInSeconds=" + durationInSeconds +
                ", genre='" + genre + '\'' +
                '}';
    }
}
