package gavehicles.interfaces;

import gavehicles.abstracts.IndividualVehicle;
import gavehicles.classes.Population;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public interface Modelable {

    public void step();

    public void init();

    public int getT();

    public void paint(Graphics g);

    public void finish();

    public void reset();

    public double getPreyStimulusStrength(Point2D.Double sensorLocation, IndividualVehicle v);

    public double getPredStimulusStrength(Point2D.Double sensorLocation, IndividualVehicle v);

    public void completeGeneration();

    public double getFoodStimulusStrength(Point2D.Double sensorLocation, IndividualVehicle v);
    
    public Population getPreyPop();
    
    public Population getPredPop();

}
