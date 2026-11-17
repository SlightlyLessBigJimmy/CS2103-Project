package frontend;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final RendererDef RENDERERDef;

    public GamePanel(RendererDef RENDERERDef){
        this.RENDERERDef = RENDERERDef;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        RENDERERDef.render((Graphics2D) g, getWidth(), getHeight(),getX(), getY());
    }
}
