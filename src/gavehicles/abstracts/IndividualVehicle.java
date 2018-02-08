package gavehicles.abstracts;

import gavehicles.classes.MyUtilities;
import gavehicles.interfaces.Evaluable;
import gavehicles.interfaces.Viewable;
import gavehicles.lists.SensorList;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public abstract class IndividualVehicle implements Evaluable {

    public boolean collision; // true == pred-ate prey, prey-ate source
    public byte[] DNA;
    public int fitness;
    
    public int preySense, predSense, foodSense;
    public int timeSinceCloned = 0;

    public Point2D.Double location;
    public double orientation;
    public double velocity;
    public SensorList sensors;
    public int size = 30;
    public double maxSpeed = 10.0;
    public double baseSpeed = 2.0;
    public double vX, vY;

    public IndividualVehicle() {
        
        sensors = new SensorList();
        DNA = new byte[Evaluable.getLength()];
        for (int i = 0; i < Evaluable.getLength(); i++) {
            DNA[i] = (byte) (MyUtilities.randomInt(2));
        }
    }

    public IndividualVehicle(byte[] DNA) {
        this();
        this.DNA = DNA;
    }
    public String getStringDNA() {
        String returnMe = "";
        for (byte b : DNA) {
            returnMe += b;
        }
        return returnMe;
    }

    public abstract void determineTraits();

    public void addSensor(AbstractSensor s) {
        sensors.add(s);
    }

    @Override
    public abstract AbstractDriveOutput generateOutput(Viewable world);

    public double getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public double getOrientation() {
        return orientation;
    }

    @Override
    public Point2D.Double getLocation() {
        return location;
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }

    @Override
    public void setOrientation(double nuOrientation) {
        orientation = nuOrientation;
    }

    @Override
    public void setLocation(Point2D.Double nuLocation) {
        location = nuLocation;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setCrossed(boolean b) {
        for (AbstractSensor next : sensors) {
            next.crossed = b;
        }
    }

    @Override
    public abstract void paint(Graphics g);

    @Override
    public String toString() {
        String returnMe = "I am an Individual:";
        return returnMe;
    }

    public double getPreySense() {
        return preySense;
    }
    
    public double getPredSense() {
        return predSense;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }
    
    @Override
    public void moveIt(AbstractDriveOutput theOutput) {
        double leftOutput = theOutput.getLeftWheelOutput();
        double rightOutput = theOutput.getRightWheelOutput();
        double direction = this.getOrientation();

        double distance = (leftOutput + rightOutput) / 2;
        double dx = distance * Math.cos(direction);
        double dy = -distance * Math.sin(direction);

        double x = this.getLocation().getX();
        double y = this.getLocation().getY();

        this.setLocation(new Point2D.Double(x + dx, y + dy));

        double deltaDirection = ((rightOutput - leftOutput) / this.getSize()) * (Math.PI / 8);
        this.setOrientation(direction + deltaDirection);
    }

}
