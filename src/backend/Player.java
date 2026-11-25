package backend;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.http.WebSocket;

public class Player extends Car implements KeyListener, GlobalUpdate {
    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean turningRight = false;
    private boolean turningLeft = false;
    //TODO: get the key pressed connected to the panel listeners

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if(!movingUp)
                movingUp = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if(!movingDown)
                movingDown = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if(!turningLeft)
                turningLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if(!turningRight)
                turningRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if(movingUp)
                movingUp = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if(movingDown)
                movingDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if(turningLeft)
                turningLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if(turningRight)
                turningRight = false;
        }
    }

    @Override
    public void update() {
        //TODO: make sure the actual numbers for the lerping works
        double currentAccel = getAccelerationInput();
        double currentSteer = getSteeringInput();
        double steering = currentSteer;
        double acceleration = currentAccel;
        if(movingUp){
            acceleration = lerp(currentAccel, 1, 1.0);
        }
        else if(movingDown)
            acceleration = lerp(currentAccel, -1, 1.0);
        if(turningLeft)
            steering = lerp(currentSteer, -1, 1.0);
        else if(turningRight)
            steering = lerp(currentSteer, 1, 1.0);
        updateInputs(acceleration, steering);
    }
}

