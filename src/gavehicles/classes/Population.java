package gavehicles.classes;

import gavehicles.abstracts.AbstractDriveOutput;
import gavehicles.vehicles.PreyVehicle;
import gavehicles.interfaces.Evaluable;
import gavehicles.interfaces.Viewable;
import gavehicles.lists.EvaluableList;
import gavehicles.vehicles.PredVehicle;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class Population extends EvaluableList {

    Viewable theWorld;
    EvaluableList matingPool = new EvaluableList();
    EvaluableList nextGen = new EvaluableList();
    private int MATING_POOL;
    private int MUTATION_RATE = 25;
    private int cloneThreshold = 250;
    private int regen;

    public Population() {
    }

    public Population(boolean pred) {
        if (pred) {
            regen = 500;
            for (int i = 0; i < MyUtilities.getPredSize(); i++) {
                this.add(new PredVehicle(new Point2D.Double(MyUtilities.randomInt(MyUtilities.getWidth()), MyUtilities.randomInt(MyUtilities.getHeight())), MyUtilities.randomDouble(2) * Math.PI));
            }
        } else {
            regen = 100;
            for (int i = 0; i < MyUtilities.getPreySize(); i++) {
                this.add(new PreyVehicle(new Point2D.Double(MyUtilities.randomInt(MyUtilities.getWidth()), MyUtilities.randomInt(MyUtilities.getHeight())), MyUtilities.randomDouble(2) * Math.PI));
            }
        }
    }

    public Population(boolean pred, Viewable v) {
        this(pred);
        theWorld = v;
    }

    public void doAGeneration() {
        if (this.size() <= 20) {
            MATING_POOL = (int) (this.size() * .3);
        } else {
//            MATING_POOL = 0;
        }
        sortByFitness();
        applyCrossover();
        selectMatingPool();
        applyGeneticOperators();
        replacement();

//        System.out.println("this = " + this);
    }

    public EvaluableList getPopulation() {
        return this;
    }

    public void sortByFitness() {
        FitnessComparator fc = new FitnessComparator();
        this.sort(fc);
//        System.out.println("this = " + this);

    }

    public void selectMatingPool() {

//        System.out.println("MATING_POOL = " + MATING_POOL);
        for (int i = 0; i < MATING_POOL; i++) {
            matingPool.add(this.get(i));
//            System.out.println("added");
        }
//        System.out.println("matingPool = " + matingPool.toString());
    }

    public void applyGeneticOperators() {
        for (Evaluable I : matingPool) {
            mutate(I.myClone());
        }

        initNextGen();
    }

    public void replacement() {
        for (int i = 0; i < nextGen.size(); i++) {

            this.remove(this.size() - 1 - i);
            this.add(nextGen.get(i));
        }
        nextGen.clear();
        matingPool.clear();
        System.out.println("this.get(0) = " + this.get(0));

        for (Evaluable I : this) {
            I.setFitness(0);
        }
    }

    public void step() {
        EvaluableList cloneList = new EvaluableList();
        for (Evaluable veh : this) {
            veh.step(theWorld);

            if (veh.getFitness() >= cloneThreshold && veh.timeSinceCloned() > regen) {
                veh.setFitness(veh.getFitness() / 2);
                Evaluable clone = veh.myClone();
                clone.setLocation(veh.getLocation());
                clone.setOrientation(-veh.getOrientation());
                clone.setFitness(veh.getFitness() / 2);
                cloneList.add(clone);
                // if we need cloning cool-down time
                veh.setTimeSinceCloned(0);
            }
        }

        for (Evaluable clone : cloneList) {
            this.add(clone);
        }

    }

    public void update() {
        for (Evaluable nextVehicle : this) {
            AbstractDriveOutput theOutput = nextVehicle.generateOutput(theWorld);
            nextVehicle.moveIt(theOutput);
        }
    }

    @Override
    public String toString() {
        String returnMe = "I am a Population:";
        returnMe += "\n\tContaining:";
        for (Evaluable next : this) {
            returnMe += "\n\t\t" + next.toString();
        }
        return returnMe;
    }

    public void paint(Graphics g) {
        for (Evaluable e : this) {
            e.paint(g);
        }
    }

    private void mutate(Evaluable I) {
        for (int i = 0; i < Evaluable.getLength(); i++) {
            int rand = (int) (Math.random() * MUTATION_RATE);
            if (rand == 5) {
//                System.out.println("changed");
                switch (I.getDNA()[i]) {
                    case 0:
                        I.getDNA()[i] = 1;
                    case 1:
                        I.getDNA()[i] = 0;
                }
            }
        }

        nextGen.add(I);

    }

    public static void main(String[] args) {
        Population p = new Population(false);
        System.out.println(p.toString());
        giveFitness(p);
        p.doAGeneration();
        System.out.println(p.toString());
    }

    private static void giveFitness(Population p) {
        for (int i = 0; i < p.size(); i++) {
            p.get(i).setFitness(MyUtilities.randomInt(100));
        }
    }

    private void initNextGen() {
        for (int i = 0; i < nextGen.size(); i++) {
            nextGen.get(i).setLocation(new Point2D.Double(MyUtilities.randomInt(MyUtilities.getWidth()), MyUtilities.randomInt(MyUtilities.getHeight())));
            nextGen.get(i).setOrientation(MyUtilities.randomDouble(2) * Math.PI);
            nextGen.get(i).setFitness(0);
        }
    }

    private void applyCrossover() {
        for (int i = 0; i < this.size(); i++) {

            cross(this.get(i), this.get(MyUtilities.randomInt(this.size())), 0);

        }
    }

    private void cross(Evaluable first, Evaluable second, int n) {
        while (n < 5) {
            int start = MyUtilities.randomInt(100);
            for (int i = start; i < Evaluable.getLength(); i++) {
                byte temp = first.getDNA()[i];
                first.getDNA()[i] = second.getDNA()[i];
                second.getDNA()[i] = temp;
            }
            n++;

        }

    }

}
