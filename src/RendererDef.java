import java.awt.*;
import java.util.ArrayList;


public class RendererDef {
    private final ArrayList<Renderable> renderables = new ArrayList<>();

    public void add(Renderable r)    { renderables.add(r); }
    public void remove(Renderable r) { renderables.remove(r); }

    public void render(Graphics2D g, int width, int height, int x, int y) {
        for (Renderable r : renderables) {
            r.render(g, width, height, x, y);
        }
    }
}
