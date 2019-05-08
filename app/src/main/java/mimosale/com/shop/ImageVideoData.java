package mimosale.com.shop;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Karshima on 1/25/2018.
 */

public class ImageVideoData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Bitmap bitmap;

    private String path;
    private String videoName;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoNames) {
        this.videoName = videoNames;
    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private String imageName;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
