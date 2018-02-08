package gavehicles.vehicles;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.abstracts.AbstractSensor;
import gavehicles.abstracts.IndividualVehicle;
import gavehicles.classes.MyUtilities;
import gavehicles.interfaces.Viewable;
import java.awt.geom.Point2D;

public class FoodSensor extends AbstractSensor {

    public FoodSensor() {

    }

    public FoodSensor(int w) {
        setWeight(w);
    }

    @Override
    public String mySource() {
        return "food";
    }

    @Override
    public double getStimulusStrength(Viewable world, IndividualVehicle v, Point2D.Double sensorLocation) {
        return world.getFoodStimulusStrength(v, sensorLocation);
    }

    @Override
    public AbstractDriveOutput createDriveOutput(double left, double right, IndividualVehicle v) {
        AbstractDriveOutput returnMe;
        double avgStrength = (left + right) / 2.0;
        try {
            PredVehicle pred = (PredVehicle) v;
            returnMe = new PassiveDriveOutput(left, right, avgStrength, pred);
        } catch (Exception e) {
            PreyVehicle prey = (PreyVehicle) v;
            returnMe = new PassiveDriveOutput(left, right, avgStrength, prey);
        }
        return returnMe;
    }

    @Override
    public AbstractDriveOutput createDriveOutput(double left, double right, IndividualVehicle v, Stimulus s) {
        AbstractDriveOutput returnMe;
        double avgStrength = (left + right) / 2.0;
        try {
            PredVehicle pred = (PredVehicle) v;
            returnMe = new PassiveDriveOutput(left, right, avgStrength, pred);
        } catch (Exception e) {
            PreyVehicle prey = (PreyVehicle) v;
            returnMe = new PassiveDriveOutput(left, right, avgStrength, prey);
        }
        returnMe.multiply(s.getWeight());
        return returnMe;
    }

}
