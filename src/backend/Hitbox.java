package backend;

import java.util.ArrayList;
import java.util.Comparator;

public class Hitbox {
    private final ArrayList<Vector3> vertices; //Positions of corners on hitboxes.
    private final double distanceFurthestVertex;
    private final GameObject parentObject;

    public Hitbox(ArrayList<Vector3> vertices, GameObject parentObject) {
        if(vertices.size() < 3) throw new IllegalArgumentException("Need at least 3 vertices in a hitbox.");
        this.vertices = vertices;
        this.parentObject = parentObject;
        this.distanceFurthestVertex = vertices.stream().max(
                (Comparator.comparing( (Vector3 a) -> a.distance(parentObject.transform.position)))
        ).orElseThrow( () -> new IllegalStateException("Somehow there wasn't a furthest vertex!")).distance(parentObject.transform.position);
    }

    private static boolean intersects2D(Hitbox a, Hitbox b) {
        if(!filterFar(a, b)) return false;

        Vector3 closestVertexPosition = verticesWorldSpace(b).stream().min(
                Comparator.comparing( (Vector3 vert) -> vert.distance(a.parentObject.transform.position))
        ).orElseThrow( () -> new IllegalStateException("Hitbox b had no vertices somehow!"));

        Vector3 ray = new Vector3(Math.random(), Math.random(), 0).multiply(a.distanceFurthestVertex * 2);

        ArrayList<Vector3> vertices = verticesWorldSpace(a);
        ArrayList<Vector3> edges = Hitbox.edges(a);

        int intersections = 0;
        for(int i = 0; i < vertices.size(); i++) {
            double tolerance = 0.01;
            double x = (closestVertexPosition.x - vertices.get(i).x) / (edges.get(i).x - ray.x);
            double y = (closestVertexPosition.y - vertices.get(i).y) / (edges.get(i).y - ray.y);

            if(Math.abs(x-y) < tolerance && 0 < x && x < 1) intersections++;
        }

        return intersections % 2 == 1;
    }

    private static boolean filterFar(Hitbox a, Hitbox b) {
        //A point on an edge can never be farther from the origin than the farthest vertex.
        //Return false immediately if the farthest vertices on a & b from their origins are not greater than or equal to the current distance between their origins.
        return a.distanceFurthestVertex + b.distanceFurthestVertex < a.parentObject.transform.position.distance(b.parentObject.transform.position);
    }

    private static ArrayList<Vector3> verticesWorldSpace(Hitbox hitbox) {
        ArrayList<Vector3> newVertices = (ArrayList<Vector3>) hitbox.vertices.clone();
        for(Vector3 vert : newVertices) {
            vert.rotate2D(hitbox.parentObject.transform.orientation);
            vert.add(hitbox.parentObject.transform.position);
        }
        return newVertices;
    }

    private static ArrayList<Vector3> edges(Hitbox hitbox) {
        ArrayList<Vector3> vertices = verticesWorldSpace(hitbox);
        ArrayList<Vector3> edges = new ArrayList<>();
        for(int i = 0; i < vertices.size() - 1; i++) {
            edges.add(vertices.get(i).line(vertices.get(i+1)));
        }
        edges.add(vertices.getLast().line(vertices.getFirst()));
        return edges;
    }

    static void main() {

    }
}
