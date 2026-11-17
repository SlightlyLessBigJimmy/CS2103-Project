import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Environment implements Renderable {
    private Image backgroundImage;

    @Override
    public int getZindex(){
        return -2;// back
    }
    @Override
    public void render(Graphics2D g, int width, int height,int x, int y){
        if (backgroundImage == null){
            System.err.println("NotLoading");
        }

        g.drawImage(backgroundImage,x,y,width,height,null);

    }

    public Environment(){
        String imgFileName = "Background.png";
        String filePath = "images/" + imgFileName;
        try{ //base environment
            //load image
            this.backgroundImage = ImageIO.read(new File(filePath));
        }
        catch (IOException e){
            System.err.println("File not found in: " + System.getProperty("user.dir"));

            this.backgroundImage = null;
        }
        //loop as it rolls

    }

}
