package frontend;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Environment  {
    private Image backgroundImage;



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
