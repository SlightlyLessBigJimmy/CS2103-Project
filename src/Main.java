import javax.swing.event.EventListenerList;

private static boolean isRunning = true;
private static EventListenerList updateListeners = new EventListenerList();

public static void main(){
    while (isRunning){
        GlobalUpdate[] listeners = updateListeners.getListeners(GlobalUpdate.class);
        for(GlobalUpdate listener : listeners){
            listener.update();
        }
    }
}