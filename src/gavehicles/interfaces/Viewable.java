package gavehicles.interfaces;

import gavehicles.abstracts.IndividualVehicle;
import java.awt.geom.Point2D;

public interface Viewable {

    public void display();

    public double getPredStimulusStrength(IndividualVehicle v, Point2D.Double sensorLocation);

    public double getFoodStimulusStrength(IndividualVehicle v, Point2D.Double sensorLocation);

    public double getPreyStimulusStrength(IndividualVehicle v, Point2D.Double sensorLocation);

}
