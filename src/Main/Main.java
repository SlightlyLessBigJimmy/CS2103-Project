package Main;

import backend.*;
import frontend.*;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static boolean isRunning = true;
    private static EventListenerList updateListeners = new EventListenerList();

    private static boolean fixedTime = false;
    private static int fixedFrameRate = 60;

    public static Transform camera;

    //problably wanna change this later for proper encapsulation
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
        renderer.setVisible(true);

        while (isRunning){
            if(!fixedTime) Timing.updateTime(); else Timing.updateTime(fixedFrameRate);
            GlobalUpdate[] listeners = updateListeners.getListeners(GlobalUpdate.class);

            ArrayList<Sprite> sprites = new ArrayList<>();

            for(GlobalUpdate listener : listeners){
                listener.update();
                if (listener instanceof Sprite){
                    sprites.add((Sprite) listener);
                }
            }
            renderer.renderNextFrame(sprites, camera);
        }
    }
}