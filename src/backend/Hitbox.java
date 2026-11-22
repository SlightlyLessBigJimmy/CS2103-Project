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
        if(filterFar(a, b)) return false;

        Vector3 closestVertexPosition = verticesWorldSpace(b).stream().min(
                Comparator.comparing( (Vector3 vert) -> vert.distance(a.parentObject.transform.position))
        ).orElseThrow( () -> new IllegalStateException("Hitbox b had no vertices somehow!"));

        System.out.println(closestVertexPosition + " Position closest");

        Vector3 ray = new Vector3(Math.random() + 1, Math.random() + 1, 0).normalized().multiply(a.distanceFurthestVertex);

        System.out.println(ray + " Ray");

        ArrayList<Vector3> vertices = verticesWorldSpace(a);
        ArrayList<Vector3> edges = Hitbox.edges(a);

        double tolerance = 0.01;
        int intersections = 0;

        for(int i = 0; i < edges.size(); i++) {
            if(linesIntersect(closestVertexPosition, ray, vertices.get(i), edges.get(i))) intersections++;
        }

        return intersections % 2 == 1;
    }

    private static boolean linesIntersect(Vector3 point1, Vector3 slopes1, Vector3 point2, Vector3 slopes2) {

        double t = (slopes2.x * (point2.y - point1.y) - slopes2.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x - slopes2.y);
        double s = (slopes1.x * (point2.y - point1.y) - slopes1.y * (point2.x - point1.x)) / (slopes2.x * slopes1.y - slopes1.x - slopes2.y);

        return t < 1 && s < 1 && t > 0 && s > 0;

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

    static boolean test1(){//should return true
        ArrayList<Vector3> shape1 = new ArrayList<>();

        shape1.add(new Vector3(0,0,0));
        shape1.add(new Vector3(1,0,0));
        shape1.add(new Vector3(1,1,0));
        shape1.add(new Vector3(0,1,0));

        Hitbox h1 = new Hitbox(shape1, new GameObject(new Vector3(0,0,0), new Vector3(1,1,1), 0));
        Hitbox h2 = new Hitbox(shape1, new GameObject(new Vector3(0.5,0.5,0), new Vector3(1,1,1), 0));

        return intersects2D(h1,h2);
    }

    static boolean test2(){//should return false
        ArrayList<Vector3> shape3 = new ArrayList<>();

        shape3.add(new Vector3(0,0,0));
        shape3.add(new Vector3(1,0,0));
        shape3.add(new Vector3(1,1,0));
        shape3.add(new Vector3(0,1,0));

        Hitbox h1 = new Hitbox(shape3, new GameObject(new Vector3(0,0,0), new Vector3(1,1,1), 0));
        Hitbox h2 = new Hitbox(shape3, new GameObject(new Vector3(5,5,0), new Vector3(1,1,1), 0));

        return intersects2D(h1,h2);
    }

    static boolean test3(){//should return true
        ArrayList<Vector3> shape1 = new ArrayList<>();

        shape1.add(new Vector3(0,0,0));
        shape1.add(new Vector3(1,0,0));
        shape1.add(new Vector3(1,1,0));
        shape1.add(new Vector3(0,1,0));

        Hitbox h1 = new Hitbox(shape1, new GameObject(new Vector3(0,0,0), new Vector3(1,1,1), 0));
        Hitbox h2 = new Hitbox(shape1, new GameObject(new Vector3(0,0,0), new Vector3(1,1,1), 45));

        return intersects2D(h1,h2);
    }

    static boolean test4(){//should return true
        ArrayList<Vector3> shape1 = new ArrayList<>();

        shape1.add(new Vector3(0,0,0));
        shape1.add(new Vector3(1,0,0));
        shape1.add(new Vector3(1,1,0));
        shape1.add(new Vector3(0,1,0));

        Hitbox h1 = new Hitbox(shape1, new GameObject(new Vector3(0,0,0), new Vector3(1,1,1), 0));
        Hitbox h2 = new Hitbox(shape1, new GameObject(new Vector3(0,0,0), new Vector3(1,1,1), 0));

        return intersects2D(h1,h2);
    }


    static void main() {
        System.out.println(test1());//corners overlap
        System.out.println(test2());//completely not touching
        System.out.println(test3());//on top of each other, one rotated 45 degrees
        System.out.println(test4());//exactly on top of each other

    }
}
