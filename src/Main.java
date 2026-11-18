import backend.*;
import frontend.*;

import javax.swing.event.EventListenerList;

private static boolean isRunning = true;
private static EventListenerList updateListeners = new EventListenerList();

private static boolean fixedTime = false;
private static int fixedFrameRate = 60;

public static GameObject Camera;

public static void main(){

    //Frontend start WILL refactor
    RendererDef renderer = new RendererDef();//frontend.RendererDef is such a stupid name but Renderer was conflicting with an already made class that was messing with things
    Environment env = new Environment();//Dont mind the ugly background its very temporary
    renderer.add(env);                       // register so it gets drawn

    GamePanel panel = new GamePanel(renderer);
    GameWindow window = new GameWindow(panel);   // window/frame just adds the panel to the frame




    //Bad very bad practice but setting up some cars
    //not even doing it correctly either im setting up just a sprite, which Im thinking the backend will hold a variable to the sprite
    try {
        System.out.println("Got image");

        Sprite car1 = new Sprite("images/car1.png");
        renderer.add(car1);
    }
    catch (IOException e){
        System.out.println("Error getting image");
    }


    window.show();

    new javax.swing.Timer(16, e -> {
        panel.revalidate();
        panel.repaint();//simple repaintLoop
    }).start();

    while (isRunning){
        if(!fixedTime) Timing.updateTime(); else Timing.updateTime(fixedFrameRate);
        GlobalUpdate[] listeners = updateListeners.getListeners(GlobalUpdate.class);
        for(GlobalUpdate listener : listeners){
            listener.update();
        }
    }
}