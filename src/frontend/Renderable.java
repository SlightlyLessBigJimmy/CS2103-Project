package frontend;

import java.awt.*;

public interface Renderable
{
    int getZindex();
    void render(Graphics2D g, int width, int height, int x, int y);
}