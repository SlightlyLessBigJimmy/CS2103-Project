package frontend;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import  backend.GameObject;

public class RendererDef {
    private final ArrayList<GameObject> renderableObjects = new ArrayList<>();
    private final SpriteRegistry sprites = new SpriteRegistry();

    public void add(GameObject obj, String spriteImage) throws IOException {
        renderableObjects.add(obj);
        sprites.register(obj.spriteId,spriteImage, (int)obj.transform.size.x, (int)obj.transform.size.y);
    }
    public void remove(GameObject r) { renderableObjects.remove(r); }

    public void render(Graphics2D g, int width, int height, int x, int y) {
        for (GameObject obj : renderableObjects) {// for each game object only render ones whose visible is true
            //Each object gets rendered the same way
            if(obj.visible){
                //project onto screen based off of camera
                //display on screen

            }
           // r.render(g, width, height, x, y);
        }
    }
}


//public double[] project(double worldX, double worldY){
//    double screenCenterX =
//}