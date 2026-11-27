package backend;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Hitbox implements GlobalUpdate{
    private final ArrayList<Vector3> vertices; //Positions of vertices on hitboxes relative to their origins.
    private final double distanceFurthestVertex; //Helps with optimization, could also just precompute this later instead of on Hitbox creation

    private final GameObject parentObject; //The gameobject which this hitbox is attached to.
    private final EventListenerList collisionListeners = new EventListenerList(); //A list of all collision listeners attached to this hitbox

    public Hitbox(ArrayList<Vector3> vertices, GameObject parentObject) {
        if (vertices.size() < 3) throw new IllegalArgumentException("Need at least 3 vertices in a hitbox.");
        this.vertices = vertices; //If there are duplicate vertices back to back, things break. Don't.
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
     * Determines this hitbox is colliding with another based on the first edge intersection found, or if a ray from the first point declares the shape
     * is completely inside the other.
     * @param other The other hitbox to check a collision with.
     * @return If the hitboxes are colliding, based on a ray of the closest vertex of this hitbox.
     */
    public boolean intersects2D(Hitbox other) throws NoIntersectionException {
        if (filterFar(this, other)) return false;

        ArrayList<Vector3> thisVerts = verticesWorldSpace(this);
        ArrayList<Vector3> thisEdges = edges(this);
        HashMap<Vector3, connectedVertices> thisConnectedVertices = connectedVertices(this, thisEdges);

        ArrayList<Vector3> otherVerts = verticesWorldSpace(other);
        ArrayList<Vector3> otherEdges = edges(other);

        //Stores the first intersection an edge has to calculate a midpoint if it is intersected again
        HashMap<Vector3, ArrayList<Vector3>> edgeAlreadyIntersected = new HashMap<>();
        //Stores a vertex if its edge is involved in a collision. Maps to false on the first involvement, and true on subsequent ones.
        HashMap<Vector3, Boolean> vertexInvolved = new HashMap<>();

        for(int i = 0; i < thisVerts.size(); i++) {
            Vector3 vert1 = thisVerts.get(i);
            Vector3 edge1 = thisEdges.get(i);
            for(int j = 0; j < otherVerts.size(); j++) {
                Vector3 vert2 = otherVerts.get(j);
                Vector3 edge2 = otherEdges.get(j);
                try{
                    Vector3 collisionPoint = whereLinesIntersect(vert1, edge1, vert2, edge2);

                    if(!edgeAlreadyIntersected.containsKey(edge1)) edgeAlreadyIntersected.put(edge1, new ArrayList<>());
                    edgeAlreadyIntersected.get(edge1).add(collisionPoint);

                    //Assign vectors of this hitbox to local space, then put them in vertexInvolved
                    vert1 = thisConnectedVertices.get(edge1).vertex1;
                    Vector3 vert3 = thisConnectedVertices.get(edge1).vertex2;

                    if(vertexInvolved.containsKey(vert1)) vertexInvolved.put(vert1, true); else vertexInvolved.put(vert1, false);
                    if(vertexInvolved.containsKey(vert3)) vertexInvolved.put(vert3, true); else vertexInvolved.put(vert3, false);

                } catch (NoIntersectionException _) {}
            }
        }

        ArrayList<Vector3> collisionsWorldSpace = new ArrayList<>();

        edgeAlreadyIntersected.forEach(
            (_, value) -> {
                if(value.size() < 2) return;
                Vector3 total = new Vector3();
                for(Vector3 vector : value) total.add(vector);
                collisionsWorldSpace.add(total.divide(value.size()));
            }
        );

        ArrayList<Vector3> collisionsLocalSpace = new ArrayList<>();

        vertexInvolved.forEach(
                (key, value) -> {
                    if(value) collisionsLocalSpace.add(key);
                }
        );

        for(Vector3 collision : collisionsWorldSpace) {
            collision.subtract(parentObject.transform.position);
            collision.divide(parentObject.transform.size);
            collision.unrotate2D(parentObject.transform.orientation);
            collisionsLocalSpace.add(collision);
        }

        for(Vector3 collision : collisionsLocalSpace) onCollision(new CollisionEvent(this, collision, other));


        if(!collisionsLocalSpace.isEmpty()) return true;

        if(hitboxFullyInsideOther(thisVerts, otherVerts, otherEdges)) throw new NoIntersectionException("Hitbox is fully inside another!");
        if(hitboxFullyInsideOther(otherVerts, thisVerts, thisEdges)) throw new NoIntersectionException("Hitbox is fully inside another!");

        return false;
    }

    private static boolean hitboxFullyInsideOther(
            ArrayList<Vector3> verticesA, ArrayList<Vector3> verticesB, ArrayList<Vector3> edgesB)
    {
        Vector3 ray = new Vector3(Math.random() + 1, Math.random() + 1, 0).normalized().multiply(Integer.MAX_VALUE);
        int intersections = 0;

        for (int i = 0; i < edgesB.size(); i++) {
            if (rayIntersect(verticesA.get(1), ray, verticesB.get(i), edgesB.get(i))) intersections++;
        }

        return intersections % 2 == 1;
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
            vert = vert.multiply(hitbox.parentObject.transform.size); //To make sure that the hitboxes match up with scaling up
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
     * Relates vertices in local space to their edges in world space. Serves as a helper to intersects2D.
     * @param hitbox The hitbox to operate on the vertices of
     * @param allEdges The edges of the hitbox, as returned by the edges method.
     * @return A hashmap connecting each edge in world space to the two vertices that it is made from.
     */
    private static HashMap<Vector3, connectedVertices> connectedVertices(Hitbox hitbox, ArrayList<Vector3> allEdges) {
        HashMap<Vector3, connectedVertices> map = new HashMap<>();
        ArrayList<Vector3> vertices = hitbox.vertices;

        //Map all the edges, except for the last, to their vertices
        for(int i = 0; i < vertices.size()-1; i++) {
            Vector3 vert1 = vertices.get(i); //Starting vertex
            Vector3 vert2 = vertices.get(i+1); //Ending vertex
            connectedVertices verticesRecord = new connectedVertices(vert1, vert2);
            map.put(allEdges.get(i), verticesRecord);
        }

        Vector3 vert1 = vertices.getLast(); //Starting vertex
        Vector3 vert2 = vertices.getFirst(); //Ending vertex
        connectedVertices verticesRecord = new connectedVertices(vert1, vert2);
        map.put(allEdges.getLast(), verticesRecord);

        return map;
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

        return s <= 1 && t >= 0 && s >= 0;
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

        return t <= 1 && s <= 1 && t >= 0 && s >= 0;
    }

    /**
     * Determines where two lines intersect. Does not check to see if the lines actually intersect in their ranges.
     *
     * @param point1 First line segment's beginning
     * @param slopes1 First line segment's direction and magnitude
     * @param point2 Second line segment's beginning
     * @param slopes2 Second line segment's direction and magnitude
     *
     * @return Where the lines intersect,
     */
    private static Vector3 whereLinesIntersect(
            Vector3 point1, Vector3 slopes1, Vector3 point2, Vector3 slopes2
    ) throws NoIntersectionException{

        //Time the x is equal
        double t = (slopes2.x * (point2.y - point1.y) - slopes2.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x * slopes2.y);
        //Time the y is equal
        double s = (slopes1.x * (point2.y - point1.y) - slopes1.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x * slopes2.y);

        if( ! (t <= 1 && s <= 1 && t >= 0 && s >= 0)) throw new NoIntersectionException("Didn't intersect.");
        return new Vector3(point1.x + slopes1.x * t, point1.y + slopes1.y * s, 0);
    }

    //Used to map vertices and edges to each other
    private record connectedVertices(Vector3 vertex1, Vector3 vertex2) {}
}

