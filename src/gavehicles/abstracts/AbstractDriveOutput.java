package gavehicles.abstracts;

/*

 crossed sensor + aggressive = attack the stimulus
 crossed + passive = admires the stimulus, then leaves
 uncrossed sensor + aggressive = run away from stimulus
 uncrossed + passive = attracted to the stimulus

 */
public abstract class AbstractDriveOutput {

    static final int MAX_OUTPUT = 100;
    protected double leftWheelOutput;
    protected double rightWheelOutput;

    public AbstractDriveOutput() {
    }

    public double getLeftWheelOutput() {
        return leftWheelOutput;
    }

    public double getRightWheelOutput() {
        return rightWheelOutput;
    }
   
    public void setLeftWheelOutput(double nuLeftWheelOutput) {
        if (nuLeftWheelOutput > MAX_OUTPUT) {
            leftWheelOutput = MAX_OUTPUT;
        } else {
            leftWheelOutput = nuLeftWheelOutput;
        }
    }

    public void setRightWheelOutput(double nuRightWheelOutput) {
        if (nuRightWheelOutput > MAX_OUTPUT) {
            rightWheelOutput = MAX_OUTPUT;
        } else {
            rightWheelOutput = nuRightWheelOutput;
        }
    }

    public void multiply(double d) {
        leftWheelOutput *= d;
        rightWheelOutput *= d;
    }
    
    public abstract AbstractDriveOutput combine(AbstractDriveOutput o, IndividualVehicle v);

    @Override
    public String toString() {
        String returnMe = "I am a DO: ";
        returnMe += "\tleft=" + leftWheelOutput;
        returnMe += "\tright=" + rightWheelOutput;
        return returnMe;
    }

}
