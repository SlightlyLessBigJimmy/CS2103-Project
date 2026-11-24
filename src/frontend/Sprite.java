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
    private int zIndex = 0; //higher draws on top
    private double rotationOffset = 0; //degrees; use if the image art does not face "up" (+y) at orientation 0

    public Sprite(String imgFileName, GameObject parentObj) throws IOException {
        this(imgFileName, parentObj, 0, 0);
    }

    public Sprite(String imgFileName, GameObject parentObj, int zIndex, double rotationOffset) throws IOException {
        this.parentObj = parentObj;
        this.zIndex = zIndex;
        this.rotationOffset = rotationOffset;
        try{
            this.sprite = ImageIO.read(new File(imgFileName));
            if (this.sprite == null) System.err.println("Sprite: unsupported image format: " + imgFileName);
        }
        catch (IOException e){
            this.sprite = null;
            System.err.println("Sprite: could not load '" + imgFileName + "' (working dir: " + System.getProperty("user.dir") + ")");
        }
    }

    //returns a real copy of the parent's transform.
    //(Transform's copy constructor currently copies position into size, so it is not used here.)
    public Transform getTransformPos(){
        Transform src = parentObj.transform;
        return new Transform(
                new Vector3(src.position.x, src.position.y, src.position.z),
                new Vector3(src.size.x, src.size.y, src.size.z),
                src.orientation);
    }

    public Image getImage() {
        return sprite;
    }

    public int getZindex() { return zIndex; }
    public void setZindex(int zIndex) { this.zIndex = zIndex; }

    public double getRotationOffset() { return rotationOffset; }
    public void setRotationOffset(double degrees) { this.rotationOffset = degrees; }

}