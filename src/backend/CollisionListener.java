package backend;

import java.util.EventListener;

public interface CollisionListener extends EventListener {
    void collided(CollisionEvent e);
}