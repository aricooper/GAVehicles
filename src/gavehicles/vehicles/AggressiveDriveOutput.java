package gavehicles.vehicles;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.abstracts.IndividualVehicle;

public class AggressiveDriveOutput extends AbstractDriveOutput {

    public AggressiveDriveOutput() {
    }

    public AggressiveDriveOutput(double left, double right, double strength, IndividualVehicle v) {
        double l = 0;
        double r = 0;
        double ml = v.getMaxSpeed();
        double mr = v.getMaxSpeed();

        if (left + l < ml) {
            this.setLeftWheelOutput(left + l);
        } else {
            this.setLeftWheelOutput(ml);
        }
        if (right + r < mr) {
            this.setRightWheelOutput(right + r);
        } else {
            this.setRightWheelOutput(mr);
        }
    }

    @Override
    public String toString() {
        return "myD" + super.toString();
    }

    @Override
    public AbstractDriveOutput combine(AbstractDriveOutput o, IndividualVehicle v) {
       AggressiveDriveOutput returnMe = new AggressiveDriveOutput();
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

}
