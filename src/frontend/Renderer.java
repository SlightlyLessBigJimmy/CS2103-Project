package frontend;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Comparator;

import backend.Transform;

import javax.swing.*;


//This class is a JFrame that handles the rendering of each sprite
public class Renderer extends JFrame {
    private final BufferStrategy strategy;
    private final double pxPerGameUnit;
    private static final double GAME_UNITS_TALL = 40; //arbitrary number of game units from bottom to top of the screen
    private Color clearColor = Color.BLACK;

    public Renderer(int width, int height){
        this.setTitle("Jersey Slide");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);// TODO Change later

        //this panel is what allows us to check for MouseEvents inside the application
        JPanel screen = (JPanel) this.getContentPane();
        screen.setLayout(null);
        screen.setIgnoreRepaint(true);

        //size the DRAWABLE area (not the whole window) so the title bar doesn't eat into the game view
        screen.setPreferredSize(new Dimension(width, height));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //creates buffer strategy (must happen after the frame is visible)
        this.createBufferStrategy(2);// 2 buffers should be plenty
        strategy = this.getBufferStrategy();

        pxPerGameUnit = (double) height / GAME_UNITS_TALL;
    }

    public void setClearColor(Color color) { this.clearColor = color; }

    public void renderNextFrame(ArrayList<Sprite> renderableSprites, Transform camera) {
        //Standard BufferStrategy loop: redraw if the buffer was lost/restored mid-frame
        do {
            do {
                //Creates a new graphics object for each frame (recommended for memory and bufferStrategy usage)
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                try {
                    Insets in = getInsets();
                    int viewW = getWidth() - in.left - in.right;
                    int viewH = getHeight() - in.top - in.bottom;

                    //JFrame buffers start at the window's top-left corner (under the title bar) - shift into the content area
                    g.translate(in.left, in.top);
                    drawFrame(g, renderableSprites, camera, viewW, viewH, pxPerGameUnit, clearColor);
                } finally {
                    g.dispose();//releases memory for that frame
                }
            } while (strategy.contentsRestored());
            strategy.show();// this actually shows the frame just drawn
        } while (strategy.contentsLost());
        Toolkit.getDefaultToolkit().sync(); //avoids stutter on Linux/macOS
    }

    //Draws one full frame into g. Static + package-private so it can be tested on a BufferedImage without a window.
    static void drawFrame(Graphics2D g, ArrayList<Sprite> sprites, Transform camera,
                          int viewW, int viewH, double pxPerGameUnit, Color clearColor) {
        //clear the previous frame, otherwise moving/rotating sprites smear
        g.setColor(clearColor);
        g.fillRect(0, 0, viewW, viewH);

        //smooth edges when images are scaled/rotated
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //lowest zIndex drawn first (background), highest last (on top)
        sprites.sort(Comparator.comparingInt(Sprite::getZindex));

        AffineTransform base = g.getTransform();
        for (Sprite sprite : sprites) {
            drawSprite(g, sprite, camera, viewW, viewH, pxPerGameUnit);
            g.setTransform(base); //undo this sprite's translate/rotate/scale before the next one
        }
    }

    //Draws a single sprite centered on its transform position, rotated by its orientation, scaled to its size
    static void drawSprite(Graphics2D g, Sprite sprite, Transform camera, int viewW, int viewH, double pxPerGameUnit) {
        Image img = sprite.getImage();
        if (img == null) return;
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);
        if (imgW <= 0 || imgH <= 0) return;

        Transform t = sprite.getTransformPos();

        //size in game units -> size in pixels
        double drawW = t.size.x * pxPerGameUnit;
        double drawH = t.size.y * pxPerGameUnit;

        //1. move the origin to the sprite's centre on screen
        g.translate(projectX(camera, t, viewW, pxPerGameUnit), projectY(camera, t, viewH, pxPerGameUnit));
        //2. rotate around that centre.
        //   Game space is y-up with counter-clockwise-positive degrees (see Transform.up()),
        //   screen space is y-down where Graphics2D.rotate(+) turns clockwise, so the angle is negated.
        g.rotate(-Math.toRadians(t.orientation + sprite.getRotationOffset()));
        //3. scale image pixels to the object's size in game units
        g.scale(drawW / imgW, drawH / imgH);
        //4. draw with the image's centre on the origin (not its top-left corner)
        g.drawImage(img, -imgW / 2, -imgH / 2, null);
    }

    //calculates where obj is drawn on screen based off of relative position to the camera and adjusted to game scale via game units
    //the camera sits in the middle of the screen; game y goes up, screen y goes down
    public static double projectX(Transform camera, Transform obj, int viewW, double pxPerGameUnit){
        double camX = (camera == null) ? 0 : camera.position.x;
        return viewW / 2.0 + (obj.position.x - camX) * pxPerGameUnit;
    }
    public static double projectY(Transform camera, Transform obj, int viewH, double pxPerGameUnit){
        double camY = (camera == null) ? 0 : camera.position.y;
        return viewH / 2.0 - (obj.position.y - camY) * pxPerGameUnit;
    }
}