package gavehicles.vehicles;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.abstracts.IndividualVehicle;

public class GeneralDriveOutput extends AbstractDriveOutput {

    public GeneralDriveOutput() {
        
    }
    
    public GeneralDriveOutput(double left, double right) {
        setLeftWheelOutput(left);
        setRightWheelOutput(right);
    }
    
    @Override
    public AbstractDriveOutput combine(AbstractDriveOutput o, IndividualVehicle v) {
        GeneralDriveOutput returnMe = new GeneralDriveOutput();
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
