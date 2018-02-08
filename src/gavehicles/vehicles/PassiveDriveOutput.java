package gavehicles.vehicles;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.abstracts.IndividualVehicle;

public class PassiveDriveOutput extends AbstractDriveOutput {

    public PassiveDriveOutput() {
    }

    public PassiveDriveOutput(double left, double right, double strength, IndividualVehicle v) {
        double l = v.getMaxSpeed();
        double r = v.getMaxSpeed();

        l = l - left;
        r = r - right;

        this.setLeftWheelOutput(l);
        this.setRightWheelOutput(r);
    }

    // Method to sum different DriveOutputs.
    // Written by Paul Schot
    @Override
    public AbstractDriveOutput combine(AbstractDriveOutput o, IndividualVehicle v) {
        PassiveDriveOutput returnMe = new PassiveDriveOutput();
        double left = o.getLeftWheelOutput() + this.getLeftWheelOutput();
        double right = o.getRightWheelOutput() + this.getRightWheelOutput();
        if (left > v.getMaxSpeed()) {
            returnMe.setLeftWheelOutput(v.getMaxSpeed());
        } else {
            returnMe.setLeftWheelOutput(left);
        }
        if (right > v.getMaxSpeed()) {
            returnMe.setRightWheelOutput(v.getMaxSpeed());
        } else {
            returnMe.setRightWheelOutput(right);
        }
        return returnMe;
    }

    @Override
    public String toString() {
        return "myD" + super.toString();
    }

}
