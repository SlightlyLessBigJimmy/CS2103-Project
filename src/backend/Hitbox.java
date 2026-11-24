package backend;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Comparator;

public class Hitbox implements GlobalUpdate{
    private final ArrayList<Vector3> vertices; //Positions of vertices on hitboxes relative to their origins.
    private final double distanceFurthestVertex; //Helps with optimization, could also just precompute this later instead of on Hitbox creation
    private final GameObject parentObject; //The gameobject which this hitbox is attached to.

    private final EventListenerList collisionListeners = new EventListenerList(); //A list of all collision listeners attached to this hitbox

    public Hitbox(ArrayList<Vector3> vertices, GameObject parentObject) {
        if (vertices.size() < 3) throw new IllegalArgumentException("Need at least 3 vertices in a hitbox.");
        this.vertices = vertices;
        this.parentObject = parentObject;
        this.distanceFurthestVertex = vertices.stream().max(
                (Comparator.comparing((Vector3 a) -> a.distance(parentObject.transform.position)))
        ).orElseThrow(() -> new IllegalStateException("Somehow there wasn't a furthest vertex!")).distance(parentObject.transform.position);
    }

    /**
     * Add a listener to this hitbox.
     * @param listener the object whose collided function will be called upon a collision.
     */
    public void addCollisionListener(CollisionListener listener) {
        collisionListeners.add(CollisionListener.class, listener);
    }

    /**
     * Removes a listener from this hitbox.
     * @param listener the object whose collided function will be no longer be called upon a collision.
     */
    public void removeCollisionListener(CollisionListener listener) {
        collisionListeners.remove(CollisionListener.class, listener);
    }

    private void onCollision(CollisionEvent e) {
        CollisionListener[] listeners = collisionListeners.getListeners(CollisionListener.class);
        for (CollisionListener listener : listeners) listener.collided(e);
    }

    /**
     * Determines if two Hitboxes are colliding based on their vertices. This function will raise an events between two colliding objects.
     *
     * @param a The first hitbox
     * @param b The second hitbox
     *
     * @return If the hitboxes are colliding, based on their vertices.
     */
    private static boolean intersects2D(Hitbox a, Hitbox b) {
        if (filterFar(a, b)) return false;

        Vector3 closestVertexPosition = verticesWorldSpace(b).stream().min(
                Comparator.comparing((Vector3 vert) -> vert.distance(a.parentObject.transform.position))
        ).orElseThrow(() -> new IllegalStateException("Hitbox b had no vertices somehow!"));

        Vector3 ray = new Vector3(Math.random() + 1, Math.random() + 1, 0).normalized().multiply(Integer.MAX_VALUE);

        ArrayList<Vector3> vertices = verticesWorldSpace(a);
        ArrayList<Vector3> edges = edges(a);

        int intersections = 0;

        for (int i = 0; i < edges.size(); i++) {
            if (rayIntersect(closestVertexPosition, ray, vertices.get(i), edges.get(i))) intersections++;
        }

        if(intersections % 2 == 1) {
            //TODO: Return vertices closest to the collision in relative space.
            return true;
        }
        else return false;
    }

    private static boolean intersects2DAggressive(Hitbox a, Hitbox b) {
        if (filterFar(a, b)) return false;

        ArrayList<Vector3> aVertices = verticesWorldSpace(a);
        ArrayList<Vector3> aEdges = edges(a);
        ArrayList<Vector3> bVertices = verticesWorldSpace(b);
        ArrayList<Vector3> bEdges = verticesWorldSpace(b);

        for (int i = 0; i < aEdges.size(); i++) {
            Vector3 vertexA = aVertices.get(i);
            Vector3 edgeA = aEdges.get(i);
            for (int j = 0; j < bEdges.size(); j++) {
                if (linesIntersect(vertexA, edgeA, bVertices.get(j), bEdges.get(j))) return true;
            }
        }

        return intersects2D(a, b);
    }

    /**A point on an edge can never be farther from the origin than the farthest vertex.
     * Return false immediately if the farthest vertices on a & b from their origins are not greater than or
     * equal to the current distance between their origins.
     *
     * @param a The first hitbox
     * @param b The second hitbox
     *
     * @return true if it's impossible for the two hitboxes to be colliding.
     */
    private static boolean filterFar(Hitbox a, Hitbox b) {
        return a.distanceFurthestVertex + b.distanceFurthestVertex < a.parentObject.transform.position.distance(b.parentObject.transform.position);
    }

    /**
     * Determine where a hitbox's vertices are in world space.
     * @param hitbox The hitbox whose vertices to map to world space
     * @return A list of the hitbox's vertices in world space.
     */
    private static ArrayList<Vector3> verticesWorldSpace(Hitbox hitbox) {
        @SuppressWarnings("unchecked") //What's the proper way to clone that doesn't need this suppression to not give a warning?
        ArrayList<Vector3> vertices = (ArrayList<Vector3>) hitbox.vertices.clone();
        ArrayList<Vector3> newVertices = new ArrayList<>();
        for (Vector3 vert : vertices) {
            vert = vert.rotate2D(hitbox.parentObject.transform.orientation);
            vert = vert.add(hitbox.parentObject.transform.position);
            newVertices.add(vert);
        }
        return newVertices;
    }

    /**
     * Create vectors that draw from each vertex to the next one's, as though in a circular linked list.
     * @param hitbox The hitbox whose vertices to draw edges between
     * @return Vectors with magnitude and direction such that they draw from the corresponding vertex to the next one in the list.
     */
    private static ArrayList<Vector3> edges(Hitbox hitbox) {
        ArrayList<Vector3> vertices = verticesWorldSpace(hitbox);
        ArrayList<Vector3> edges = new ArrayList<>();
        for (int i = 0; i < vertices.size() - 1; i++) {
            edges.add(vertices.get(i).line(vertices.get(i + 1)));
        }
        edges.add(vertices.getLast().line(vertices.getFirst()));
        return edges;
    }

    /**
     * Determine if a ray intersects with a line segment
     *
     * @param point1 Ray's beginning
     * @param slopes1 Ray's direction
     * @param point2 Line segment's beginning
     * @param slopes2 Line segment's direction and magnitude
     *
     * @return If the ray intersects the line segment
     */
    private static boolean rayIntersect(Vector3 point1, Vector3 slopes1, Vector3 point2, Vector3 slopes2) {

        //Time the x is equal
        double t = (slopes2.x * (point2.y - point1.y) - slopes2.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x * slopes2.y);
        //Time the y is equal
        double s = (slopes1.x * (point2.y - point1.y) - slopes1.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x * slopes2.y);

        return s < 1 && t > 0 && s > 0;
    }

    /**
     * Determine if a line segment intersects with another line segment
     *
     * @param point1 First line segment's beginning
     * @param slopes1 First line segment's direction and magnitude
     * @param point2 Second line segment's beginning
     * @param slopes2 Second line segment's direction and magnitude
     *
     * @return If the first line segment intersects the second line segment
     */
    private static boolean linesIntersect(Vector3 point1, Vector3 slopes1, Vector3 point2, Vector3 slopes2) {

        //Time the x is equal
        double t = (slopes2.x * (point2.y - point1.y) - slopes2.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x * slopes2.y);
        //Time the y is equal
        double s = (slopes1.x * (point2.y - point1.y) - slopes1.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x * slopes2.y);

        return t < 1 && s < 1 && t > 0 && s > 0;
    }
}