package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class Hitbox {
    private ArrayList<Vector3> vertices;
    public HashMap<Vector3, Vector3> edges;

    public Hitbox() {
        vertices = new ArrayList<>();
        edges = new HashMap<>();
    }

    public void populateEdges(){
        edges = new HashMap<>();
        for(int i = 0; i < vertices.size()-1; i++) {
            edges.put(vertices.get(i),(vertices.get(i).line(vertices.get(i+1))));

        }
        edges.put(vertices.getLast(),(vertices.getLast().line(vertices.getFirst())));
    }

    public static void main() {
        Hitbox box = new Hitbox();
        box.vertices.add(new Vector3(0,0,0));
        box.vertices.add(new Vector3(2, 4, 3));
        box.vertices.add(new Vector3(1, 3, 2));

        box.populateEdges();

        box.vertices.forEach(vec -> {System.out.println(box.edges.get(vec));});
    }
}
