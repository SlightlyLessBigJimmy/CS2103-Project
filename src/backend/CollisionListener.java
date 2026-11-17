package backend;

import java.util.EventListener;

interface CollisionListener extends EventListener {
    void collided(CollisionEvent e);
}