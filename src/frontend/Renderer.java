package frontend;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import backend.Player;
import backend.Transform;

import javax.swing.*;


//This class is a JFrame that handles the rendering of each sprite
public class Renderer extends JFrame {
    private BufferStrategy strategy;
    private Graphics2D g;
    private final double pxPerGameUnit;

    public Renderer(int width, int height){
        this.setTitle("Jersey Slide");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);// TODO Change later
        this.setSize(width, height);
        this.setVisible(true);

        //this panel is what allows us to check for MouseEvents inside the application
        JPanel screen = (JPanel) this.getContentPane(); //I'm not sure why this can safely be cast as a JPanel. But it can.
        screen.setLayout(null);
        screen.setIgnoreRepaint(true);

        //creates buffer strategy
        this.createBufferStrategy(2);// 2 buffers should be plenty
        strategy = this.getBufferStrategy();

        pxPerGameUnit = (double) height / 40;//40 is the arbitrary number of game units from bottom to top of the screen
    }

    public void renderNextFrame(ArrayList<Sprite> renderableSprites, Transform camera ) {
        //Creates a new graphics object for each frame (recommended for memory and bufferStrategy usage)
        g = (Graphics2D)strategy.getDrawGraphics();

        //For every sprite, render that sprite
        for (Sprite sprite : renderableSprites) {
            //Each object gets rendered the same way
            //draw with projected x and projected y
            g.drawImage(sprite.getImage(),projectX(camera, sprite.getTransformPos()),projectY(camera, sprite.getTransformPos()), null);
        }
        strategy.show();// this actually shows the frame just drawn
        g.dispose();//releases memory for that frame

    }

    //calculates where obj is drawn on screen based off of relative position to the camera and adjusted to game scale via game units
    public int projectX(Transform Camera, Transform obj ){
        return (int) ((obj.position.x - Camera.position.x) * pxPerGameUnit);
    }
    public int projectY(Transform Camera, Transform obj ){
        return (int) ((obj.position.y - Camera.position.y) * pxPerGameUnit);
    }
}


