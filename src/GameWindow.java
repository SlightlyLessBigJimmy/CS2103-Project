import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame{

    private final JFrame FRAME;

    final int X_BOUND_OFFSET = 300;
    final int Y_BOUND_OFFSET = 150;

    public GameWindow(JPanel panel){
        FRAME = new JFrame("Jersey Slide");
        FRAME.add(panel);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        FRAME.setSize(screenSize.width - X_BOUND_OFFSET, screenSize.height - Y_BOUND_OFFSET);
        //todo make screen size reactive

    }

    @Override
    public void show() {
        FRAME.setVisible(true);
    }
}
