import javax.swing.Renderer;
import javax.swing.event.EventListenerList;

private static boolean isRunning = true;
private static EventListenerList updateListeners = new EventListenerList();

public static void main(){

    //Frontend start WILL refactor
    RendererDef renderer = new RendererDef();//RendererDef is such a stupid name but Renderer was conflicting with an already made class that was messing with things
    Environment env = new Environment();//Dont mind the ugly background its very temporary
    renderer.add(env);                       // register so it gets drawn

    GamePanel panel = new GamePanel(renderer);
    GameWindow window = new GameWindow(panel);   // window just adds the panel to the frame

    window.show();

    new javax.swing.Timer(16, e -> {
        panel.repaint();//simple repaintLoop
    }).start();


    while (isRunning){
        GlobalUpdate[] listeners = updateListeners.getListeners(GlobalUpdate.class);
        for(GlobalUpdate listener : listeners){
            listener.update();
        }
    }
}