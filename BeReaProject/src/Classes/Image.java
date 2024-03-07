import java.util.Date;

public class Image {
    private int imageId;

    public Image(int imageId) {
        this.imageId = imageId;
    }

    // Getter and setter for imageId
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    // toString method to represent Image as a string
    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                '}';
    }
}

