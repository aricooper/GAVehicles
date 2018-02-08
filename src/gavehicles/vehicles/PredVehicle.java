package gavehicles.vehicles;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.abstracts.AbstractSensor;
import gavehicles.abstracts.IndividualVehicle;
import gavehicles.classes.MyUtilities;
import gavehicles.classes.PredTraitDeterminer;
import gavehicles.interfaces.Evaluable;
import gavehicles.interfaces.Viewable;
import gavehicles.lists.DriveList;
import gavehicles.lists.StimulusList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class PredVehicle extends IndividualVehicle {

    int FOOD_WEIGHT = 1;
    private int t = 0;
    int count = 0;
    int speed, appetite, sight;
    private Evaluable currentMeal, lastMeal = null;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setFOOD_WEIGHT(int FOOD_WEIGHT) {
        this.FOOD_WEIGHT = FOOD_WEIGHT;
    }

    public PredVehicle() {
        super();
        this.preySense = 12000;
        this.predSense = 1000;
    }

    public PredVehicle(Point2D.Double location, double orientation) {
        this();
        this.location = location;
        this.orientation = orientation;
        setupSensors();
        initTraits();
    }

    public PredVehicle(int fit, byte[] dna) {
        this();
        fitness = fit;
        this.DNA = dna;
        setupSensors();
        initTraits();

    }

    private void setupSensors() {
        PreySensor preySensor = new PreySensor(1);
        preySensor.setCrossed(true);
        addSensor(preySensor);

        PredSensor predSensor = new PredSensor();
        predSensor.setCrossed(false);
        addSensor(predSensor);

//        FoodSensor foodSensor = new FoodSensor();
//        foodSensor.setCrossed(true);
//        addSensor(foodSensor);
    }

    @Override
    public void paint(Graphics g) {
        int x = (int) getX();
        int y = (int) getY();
        g.setColor(Color.yellow);
        Point2D.Double left = leftSensorLocation();
        Point2D.Double right = rightSensorLocation();
        g.fillOval(x - size / 2, y - size / 2, size, size);
        int Xratio = (int) (left.getX() + right.getX()) / 2;
        int Yratio = (int) (left.getY() + right.getY()) / 2;
        if (t % 10 == 0) {
            g.setColor(Color.black);

//            g.drawLine(x, y, Xratio, Yratio);
        } else {
        }
        g.setColor(Color.RED);

        g.drawLine(x, y, (int) left.getX(), (int) left.getY());

        g.drawLine(x, y, (int) right.getX(), (int) right.getY());
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
            returnMe.add(new Stimulus(l, r, nextSensor.mySource()));
        }
        while (returnMe.oversaturated(MyUtilities.THRESH)) {
            returnMe.multiply(.8); // makes stimulus strength 20% weaker
        }
        return returnMe;
    }

    public DriveList createDriveList(Viewable world, StimulusList stimList) {
        DriveList returnMe = new DriveList();
        for (int i = 0; i < sensors.size(); i++) {
            AbstractSensor nextSensor = sensors.get(i);
            Stimulus stim = stimList.get(i);
            double left = stim.getLeft();
            double right = stim.getRight();
            if (nextSensor.getCrossed()) {
                returnMe.add(nextSensor.createDriveOutput(right, left, this)); //crossed
            } else {
                returnMe.add(nextSensor.createDriveOutput(left, right, this));
            }
        }
        return returnMe;
    }

    private Point2D.Double rightSensorLocation() {
        double dx = getSize() * Math.cos(getOrientation() - Math.PI / 4);
        double dy = -getSize() * Math.sin(getOrientation() - Math.PI / 4);
        return new Point2D.Double(getX() + dx * 2, getY() + dy * 2);
    }

    private Point2D.Double leftSensorLocation() {
        double dx = getSize() * Math.cos(getOrientation() + Math.PI / 4);
        double dy = -getSize() * Math.sin(getOrientation() + Math.PI / 4);
        return new Point2D.Double(getX() + dx * 2, getY() + dy * 2);
    }

    @Override
    public void step(Viewable world) {
        t++;
        inbounds();
        evaluateMyFitness();
        count++;
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
//        return "ProtoVehicle: location = " + getLocation() + " orientation = " + getOrientation();
        return "ProtoVehicle: " + " fitness = " + fitness + "  speed = " + speed + " sight = " + sight + " appetite = " + appetite + " DNA = " + getStringDNA();
    }

    @Override
    public void determineTraits() {
        int[] traits = PredTraitDeterminer.getValue(this);
        speed = traits[0];
        appetite = traits[1];
        sight = traits[2];
    }

    @Override
    public void setCollision(boolean b) {
        collision = b;
        count = 0;
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
        return new PredVehicle(fitness, DNA.clone());
    }

    private void evaluateMyFitness() {
        if (atePrey()) {
//            System.out.println("eating");
//            if (notSameMeal()) {
//                System.out.println("fitness++");
            fitness += FOOD_WEIGHT;
//            }

            collision = !collision;
        }

    }

    private boolean atePrey() {
        return collision;
    }

    @Override
    public void setGotEaten(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public final void initTraits() {
        determineTraits();
        implementTraits();

    }

    @Override
    public void implementTraits() {
        maxSpeed = speed;
        preySense = 2000 * appetite;
        predSense = 100 * sight;

    }

    @Override
    public void setCurrentMeal(Evaluable meal) {
        currentMeal = meal;
    }

    private boolean notSameMeal() {
        return lastMeal.getDNA() != currentMeal.getDNA();
    }

    @Override
    public boolean getKill() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int timeSinceCloned() {
        return timeSinceCloned;
    }

    @Override
    public void setTimeSinceCloned(int i) {
        timeSinceCloned = i;
    }

}
