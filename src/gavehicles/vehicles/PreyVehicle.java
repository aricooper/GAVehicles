package gavehicles.vehicles;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.abstracts.AbstractSensor;
import gavehicles.abstracts.IndividualVehicle;
import gavehicles.classes.MyUtilities;
import gavehicles.classes.PreyTraitDeterminer;
import gavehicles.interfaces.Evaluable;
import gavehicles.interfaces.Viewable;
import gavehicles.lists.DriveList;
import gavehicles.lists.StimulusList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class PreyVehicle extends IndividualVehicle {

    int FOOD_WEIGHT = 50;
    int dangerSense, herding, speed;
    boolean gotEaten, kill;
    boolean inDanger = false;
    private int count;
    
    
    

    public PreyVehicle() {
        super();
        this.preySense = 1000;
        this.predSense = 12000;
    }

    public PreyVehicle(int fit, byte[] dna) {
        super();
        fitness = fit;
        this.DNA = dna;
        setupSensors();
        determineTraits();

    }

    public PreyVehicle(Point2D.Double location, double orientation) {
        this();
        this.location = location;
        this.orientation = orientation;
        setupSensors();
        determineTraits();
    }

    private void setupSensors() {
        PredSensor predSensor = new PredSensor();
        predSensor.setCrossed(false);
        addSensor(predSensor);

        FoodSensor foodSensor = new FoodSensor();
        foodSensor.setCrossed(false);
        addSensor(foodSensor);

        PreySensor preySensor = new PreySensor();
        preySensor.setCrossed(false);
        addSensor(preySensor);
    }

    @Override
    public AbstractDriveOutput generateOutput(Viewable world) {
        StimulusList stimList = createStimList(world);
        DriveList driveList = createDriveList(world, stimList);

        AbstractDriveOutput returnMe = driveList.sum();

        return returnMe;
    }

    public StimulusList createStimList(Viewable world) {
        StimulusList returnMe = new StimulusList();
        for (AbstractSensor nextSensor : sensors) {
            double l = nextSensor.getStimulusStrength(world, this, leftSensorLocation());
            double r = nextSensor.getStimulusStrength(world, this, rightSensorLocation());
//            To test the max stimulus values
//            if (l > MyUtilities.MAX_STIM) {
//                MyUtilities.MAX_STIM = l;
//                System.out.println("MAX = " + MyUtilities.MAX_STIM);
//            }
//            if (r > MyUtilities.MAX_STIM) {
//                MyUtilities.MAX_STIM = r;
//                System.out.println("MAX = " + MyUtilities.MAX_STIM);
//            }
//            MyUtilities.debug(nextSensor.mySource());
//            MyUtilities.debug("left: " + l + ", right: " + r);
            returnMe.add(new Stimulus(l, r, nextSensor.mySource()));
        }
        while (returnMe.oversaturated(MyUtilities.THRESH)) {
            returnMe.multiply(.8); // makes stimulus strength 20% weaker
        }
        return returnMe;
    }

    public DriveList createDriveList(Viewable world, StimulusList stimList) {
        double totalStim = 0;
        for (Stimulus next : stimList) {
            totalStim += next.getAvgStim();
        }
        DriveList returnMe = new DriveList();
        for (int i = 0; i < sensors.size(); i++) {
            AbstractSensor nextSensor = sensors.get(i);
            Stimulus stim = stimList.get(i);
            stim.setWeight(stim.getAvgStim() / totalStim);
            double left = stim.getLeft();
            double right = stim.getRight();
//            boolean crossed = nextSensor.getCrossed();
//            returnMe.add(nextSensor.createDriveOutput(stim, this, crossed));
            if (nextSensor.getCrossed()) {
                returnMe.add(nextSensor.createDriveOutput(right, left, this, stim)); //crossed
//                returnMe.add(nextSensor.createDriveOutput(right, left, this)); //crossed
            } else {
                returnMe.add(nextSensor.createDriveOutput(left, right, this, stim));
//                returnMe.add(nextSensor.createDriveOutput(left, right, this));
            }
        }
        return returnMe;
    }

    private Point2D.Double rightSensorLocation() {
        double dx = getSize() / 2 * Math.cos(getOrientation() - Math.PI / 4);
        double dy = -getSize() / 2 * Math.sin(getOrientation() - Math.PI / 4);
        return new Point2D.Double(getX() + dx * 2, getY() + dy * 2);
    }

    private Point2D.Double leftSensorLocation() {
        double dx = getSize() / 2 * Math.cos(getOrientation() + Math.PI / 4);
        double dy = -getSize() / 2 * Math.sin(getOrientation() + Math.PI / 4);
        return new Point2D.Double(getX() + dx * 2, getY() + dy * 2);
    }

    @Override
    public void step(Viewable world) {
        inbounds();
        evaluateMyFitness();
        if (gotEaten) {
            kill = true;
        }
        
        timeSinceCloned++;
    }

    private void inbounds() {
        if (getX() > MyUtilities.getWidth()) {
            setLocation(new Point2D.Double(0, getY()));
        } else if (getX() < 0) {
            setLocation(new Point2D.Double(MyUtilities.getWidth(), getY()));
        }

        if (getY() > MyUtilities.getHeight()) {
            setLocation(new Point2D.Double(getX(), 0));
        } else if (getY() < 0) {
            setLocation(new Point2D.Double(getX(), MyUtilities.getHeight()));
        }
    }

    @Override
    public String toString() {
//        String returnMe = "PreyVehicle: location = " + getLocation() + " orientation = " + getOrientation() + "\n";
        String returnMe = "PreyVehicle: " + " fitness = " + fitness + "  dangerSense = " + dangerSense + " herding = " + herding + " speed = " + speed;
//        for (byte next : DNA) {
//            returnMe += next;
//        }
        return returnMe;
    }

    @Override
    public void setGotEaten(boolean b) {
        gotEaten = b;
    }

    @Override
    public void determineTraits() {
        int[] traits = PreyTraitDeterminer.getValue(this);
        dangerSense = traits[0];
        herding = traits[1];
        speed = traits[2];
    }

    @Override
    public byte[] getDNA() {
        return DNA;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public Evaluable myClone() {
        return new PreyVehicle(fitness, DNA.clone());
    }

    @Override
    public void paint(Graphics g) {

        int x = (int) getX();
        int y = (int) getY();
        g.setColor(Color.black);
        if (!gotEaten) {
            g.fillOval(x - size / 2, y - size / 2, size, size);

            g.setColor(Color.green);
            Point2D.Double left = leftSensorLocation();
            g.drawLine(x, y, (int) left.getX(), (int) left.getY());
            Point2D.Double right = rightSensorLocation();
            g.drawLine(x, y, (int) right.getX(), (int) right.getY());
        } else {
            g.setColor(Color.red);
            g.fillOval(x - size / 2, y - size / 2, size, size);

        }
    }

    @Override
    public void setCollision(boolean b) {
        collision = b;
    }

    private void evaluateMyFitness() {

        if (eating()) {
            fitness += FOOD_WEIGHT;
            collision = !collision;
            
        }
//        if (gotEaten) {
//            fitness -= EATEN_WEIGHT;
//            gotEaten = !gotEaten;
//        }
    }

    private boolean eating() {
        return collision;
    }

    @Override
    public void initTraits() {
        determineTraits();
        implementTraits();
    }

    @Override
    public void implementTraits() {
        maxSpeed = speed*2;
        preySense = 100 * herding;
        predSense = 2000 * dangerSense;

    }

    @Override
    public void setCurrentMeal(Evaluable findEatenPrey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getKill() {
        return kill;
    }

    @Override
    public void setFOOD_WEIGHT(int fitness) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int timeSinceCloned() {
    return timeSinceCloned;    }

    @Override
    public void setTimeSinceCloned(int i) {
timeSinceCloned = i;    }

    @Override
    public int getCount() {
        return count;
    }

}
