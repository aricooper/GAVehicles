package gavehicles.abstracts;

import gavehicles.classes.MyUtilities;
import java.awt.*;
import java.awt.geom.*;

public abstract class AbstractSource {

    protected Point2D.Double location;

    protected double intensity;

    public Point2D.Double getLocation() {
        return location;
    }

    public void setLocation(Point2D.Double l) {
        location = l;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double i) {
        intensity = i;
    }

    abstract public void paint(Graphics g);

    public void deplete(double strength) {
        intensity -= strength;
        intensity += MyUtilities.getGrowthRate();
    }
}
