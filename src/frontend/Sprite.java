package frontend;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite implements Renderable {// This should 100% implement frontend.Renderable I just havent gotten there yet
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

    public Image getSprite() {
        return sprite;//should try catch this
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public void setSpriteByName(String imgFileName) throws IOException {
        this.sprite = ImageIO.read(new File(imgFileName));
    }

    @Override
    public int getZindex(){
        return 0;// back
    }

    @Override
    public void render(Graphics2D g, int width, int height,int x, int y){
        if (sprite == null){
            System.err.println("NotLoading");
        }

        g.drawImage(sprite,x,y,WIDTH,HEIGHT,null);

    }
}