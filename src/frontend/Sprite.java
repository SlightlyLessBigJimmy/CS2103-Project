package frontend;

import backend.GameObject;
import backend.Transform;
import backend.Vector3;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite
{
    private final GameObject parentObj; // points to the sprites GameObject
    private Image sprite; //might be good to hold a list of images as one sprite and just render them in order for shadows

    public Sprite(String imgFileName, GameObject parentObj) throws IOException {
        this.parentObj = parentObj;
        try{
            this.sprite = ImageIO.read(new File(imgFileName));
        }
        catch (IOException e){
            this.sprite = null;
        }
    }

    public Vector3 getTransformPos(){
        return  parentObj.transform.
    }

    public Image getImage() {
        return sprite;
    }

}