package backend;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.*;

public class Movement extends JFrame implements KeyListener {
    int x = 0;
    int y = 0;
    //TODO: change all of this oh my god this is so stupid. Get it actually working with the main window, Get it actually working with the front end, and impliment momentum and having it actually move an object
    public Movement(){
        setTitle("MovementTestingWindow");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addKeyListener(this);

        setFocusable(true);
        setVisible(true);
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_W){
            y++;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            y--;
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            x--;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            x++;
        }
        System.out.println("(" + x + "," + y + ")");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args){
        new Movement();
    }
}
