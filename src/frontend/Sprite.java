package frontend;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite
{
    private Image sprite;
    private final int WIDTH;
    private final int HEIGHT;


    //might be good to hold a list of images as one sprite and just render them in order for shadows

    public Sprite(String imgFileName) throws IOException {
        try{
            this.sprite = ImageIO.read(new File(imgFileName));
        }
        catch (IOException e){
            this.sprite = null;
        }
        WIDTH = 50;
        HEIGHT = 50;

    }
    public Sprite(String imgFileName,int width,int height) throws IOException {
        try{
            this.sprite = ImageIO.read(new File(imgFileName));
        }
        catch (IOException e){
            this.sprite = null;
        }
        this.WIDTH = width;
        this.HEIGHT = height;

    }

    public Image getImage() {
        return sprite;
    }

    public void setImage(Image sprite) {
        this.sprite = sprite;
    }

    public void setSpriteByName(String imgFileName) throws IOException {
        this.sprite = ImageIO.read(new File(imgFileName));
    }

}