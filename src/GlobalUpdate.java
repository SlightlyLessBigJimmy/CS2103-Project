import java.util.EventListener;

public interface GlobalUpdate extends EventListener {
    default void update() {}
}
