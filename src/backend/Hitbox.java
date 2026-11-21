package backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Hitbox {
    private ArrayList<Vector3> vertices; //Positions of corners on hitboxes.
    private double distanceFurthestVertex;
    private Vector3 origin; //the pivot, for most cases

    public Hitbox(ArrayList<Vector3> vertices, Vector3 origin) {
        if(vertices.size() < 3) throw new IllegalArgumentException("Need at least 3 vertices in a hitbox.");
        this.vertices = vertices;
        this.origin = origin;
        this.distanceFurthestVertex = vertices.stream().max(
                (Comparator.comparing( (Vector3 a) -> a.distance(origin)))
        ).orElseThrow( () -> new IllegalStateException("Somehow there wasn't a furthest vertex!")).distance(origin);
    }

    private static boolean intersects(Hitbox a, Hitbox b) {
        if(!filterFar(a, b)) return false;

        Vector3 closestVertex = b.vertices.stream().min(
                Comparator.comparing( (Vector3 vert) -> vert.distance(a.origin))
        ).orElseThrow( () -> new IllegalStateException("Hitbox b had no vertices somehow!"));

        //TODO: Figure out if closestVertex is in the hitbox of a.

        return false;
    }

    private static boolean filterFar(Hitbox a, Hitbox b) {
        //A point on an edge can never be farther from the origin than the farthest vertex.
        //Return false immediately if the farthest vertices on a & b are not greater than or equal to the distance between their origins.
        return a.distanceFurthestVertex + b.distanceFurthestVertex < a.origin.distance(b.origin);
    }
}
