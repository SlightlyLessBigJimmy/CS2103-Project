package backend;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.*;

public class Movement extends JFrame implements KeyListener {
    private int x = 0;
    private int y = 0;
    private boolean moving = false;
    //TODO: Get it working with the main window, Get it working with the front end, and implement momentum and having it actually move an object, along with making sure diagonal movement works once physics are put in
    public Movement(){
       //pretty sure this stuff here is what the front end has to do, then the key pressed is our job
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
        if(!moving) {
            moving = true;
            if (e.getKeyCode() == KeyEvent.VK_W) {
                y++;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                y--;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                x--;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                x++;
            }
            System.out.println("(" + x + "," + y + ")");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        moving = false;
    }
}
