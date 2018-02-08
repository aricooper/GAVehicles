package gavehicles.classes;

import gavehicles.abstracts.AbstractSource;
import java.awt.geom.Point2D;
import java.awt.*;

public class FoodSource extends AbstractSource {

    int width = 16;
    int offset = width / 2;

    public FoodSource() {
    }

    public FoodSource(Point2D.Double location, double strength) {
        setIntensity(strength);
        setLocation(location);
    }

    @Override
    public void paint(Graphics g) {
        int x = (int) getLocation().getX();
        int y = (int) getLocation().getY();
        g.setColor(new Color(150, 200, 150));
        g.fillOval(x - offset, y - offset, width, width);
        g.setColor(Color.BLACK);
        g.drawString("" + (int) (this.getIntensity()), x, y);
    }

}
