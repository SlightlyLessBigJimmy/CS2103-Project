import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Sprite {
    private Image sprite;

    public Sprite(String imgFileName) throws IOException {
        this.sprite = ImageIO.read(new File(imgFileName));
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setSpriteByName(String imgFileName) throws IOException {
        this.sprite = ImageIO.read(new File(imgFileName));
    }
}


