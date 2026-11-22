package Main;

import backend.*;
import frontend.*;

import javax.swing.event.EventListenerList;
import java.io.IOException;

public class Main {
    private static boolean isRunning = true;
    private static EventListenerList updateListeners = new EventListenerList();

    private static boolean fixedTime = false;
    private static int fixedFrameRate = 60;

    public static GameObject Camera;

    //probably wanna change this later for proper encapsulation
    public static void addToUpdateListeners(GlobalUpdate listener){
        updateListeners.add(GlobalUpdate.class, listener);
    }

    public static void removeFromUpdateListeners(GlobalUpdate listener) {
        updateListeners.remove(GlobalUpdate.class, listener);
    }

    public static void main(){

        Renderer renderer = new Renderer(900,600);
        renderer.setVisible(true);
        CustomMath.instantiate(new GameObject());


        //Todo put renderer.renderNextFrame(...) inside the main game loop after physics
        //Game loop
        while (isRunning){
            if(!fixedTime) Timing.updateTime(); else Timing.updateTime(fixedFrameRate);
            GlobalUpdate[] listeners = updateListeners.getListeners(GlobalUpdate.class);
            for(GlobalUpdate listener : listeners){
                listener.update();
            }
        }
    }
}