package gavehicles.classes;

import gavehicles.abstracts.AbstractSource;
import gavehicles.abstracts.IndividualVehicle;
import gavehicles.interfaces.Evaluable;
import gavehicles.interfaces.Modelable;
import gavehicles.interfaces.Viewable;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import gavehicles.lists.SourceList;
import gavehicles.vehicles.PredVehicle;

public class VehicleModel implements Modelable {

    int time;
    Population preyPop, predPop;
    SourceList foodSources;
    Viewable theWorld;

    public VehicleModel() {
        initPop();
        initSources();
    }

    public VehicleModel(Viewable v) {
        theWorld = v;
        initPop();
        initSources();
    }

    private void initPop() {
        preyPop = new Population(false, theWorld);
        predPop = new Population(true, theWorld);
    }
    
    @Override
    public Population getPreyPop() {
        return preyPop;
    }
    
    @Override
    public Population getPredPop() {
        return predPop;
    }

    private void initSources() {
        foodSources = new SourceList();
        for (int i = 0; i < MyUtilities.getFood(); i++) {
            foodSources.add(
                    new FoodSource(
                            new Point2D.Double(
                                    MyUtilities.randomInt(MyUtilities.getWidth()),
                                    MyUtilities.randomInt(MyUtilities.getHeight())
                            ),
                            MyUtilities.randomInt(5000, 10000)
                    )
            );
        }
    }

    @Override
    public void reset() {
        time = 0;
        initPop();
        initSources();
    }

    @Override
    public void step() {
        time++;
        checkForCollisions();
        evaluateFitness();
    }

    public void doAGeneration() {
        preyPop.doAGeneration();
        predPop.doAGeneration();
    }

    public void evaluateFitness() {
        preyPop.step();
        preyPop.update();
        predPop.step();
        predPop.update();
    }

    @Override
    public void init() {
        System.out.println("VehicleModel:init");
    }

    @Override
    public void finish() {
        doAGeneration();
        time = 0;
    }

    @Override
    public int getT() {
//        System.out.println("VehicleModel:getT");
        return time;
    }

    @Override
    public void paint(Graphics g) {
        foodSources.paint(g);
        preyPop.paint(g);
        predPop.paint(g);
    }

    @Override
    public void completeGeneration() {
        doAGeneration();
        time = 0;
    }
    
    private void checkForCollisions() {
        checkForPredFood();
        checkForPreyFood();
        checkForDeadPred();

    }
     
      private void checkForPredFood() {
        for (int i = 0; i < predPop.size(); i++) {
            if (hasEaten(predPop.get(i), i)) {
                predPop.get(i).setCollision(true);
                

            }
        }
        killEatenPrey();
    }
      
      private Evaluable findEatenPrey(Evaluable pred) {
          Evaluable prey = null;
          for (Evaluable p: preyPop) {
              if (ate(pred,p)) {
                  prey = p;
              }
          }
          return prey;
      }
      
      private boolean hasEaten(Evaluable pred, int index) {
           for (int i = 0; i < preyPop.size(); i++) {
              if (ate(pred, preyPop.get(i))) {
                  preyPop.get(i).setGotEaten(true);
                  predPop.get(index).setFOOD_WEIGHT(preyPop.get(i).getFitness()/2);
                  return true;
              }
          }
          return false;
      }

    private void checkForPreyFood() {
        for (int i = 0; i < preyPop.size(); i++) {
            if (feeding(preyPop.get(i), foodSources)) {
                preyPop.get(i).setCollision(true);
            }
        }
    }

    private boolean feeding(Evaluable prey, SourceList foodSources) {
        for (AbstractSource source : foodSources) {
            if (ate(prey, source)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean ate(Evaluable prey, Evaluable pred) {
        double x = prey.getLocation().getX();
        double y = prey.getLocation().getY();

        double x1 = pred.getLocation().getX();
        double y1 = pred.getLocation().getY();
        
        int size = predPop.get(0).getSize();

        if (Math.abs(x - x1) <= size) {
            if (Math.abs(y - y1) <= size) {
                return true;
            }
        }
        return false;
    }

    private boolean ate(Evaluable prey, AbstractSource source) {
        double x = prey.getLocation().getX();
        double y = prey.getLocation().getY();

        double x1 = source.getLocation().getX();
        double y1 = source.getLocation().getY();
        
        int size = preyPop.get(0).getSize();

        if (Math.abs(x - x1) <= size) {
            if (Math.abs(y - y1) <= size) {
                return true;
            }
        }
        return false;
    }


    @Override
    public double getPreyStimulusStrength(Point2D.Double location, IndividualVehicle v) {
        double sum = 0;

        for (Evaluable nextVeh : preyPop) {
            double d = location.distance(nextVeh.getLocation());
            sum += v.getPreySense() / (d * d);
        }

        return sum;
    }

    @Override
    public double getPredStimulusStrength(Point2D.Double location, IndividualVehicle v) {
        double sum = 0;

        for (Evaluable nextVeh : predPop) {
            double d = location.distance(nextVeh.getLocation());
            sum += v.getPredSense() / (d * d);
        }

        return sum;
    }

    @Override
    public double getFoodStimulusStrength(Point2D.Double location, IndividualVehicle v) {
        double sum = 0;

        for (AbstractSource nextSource : foodSources) {
            double d = location.distance(nextSource.getLocation());
            double strength = nextSource.getIntensity() / (1.5 * (d * d));
            sum += strength;
            nextSource.deplete(strength);
        }

        return sum;
    }

    private void killEatenPrey() {
        for (int i = 0; i < preyPop.size(); i++) {
            if (preyPop.get(i).getKill()) {
//                if (preyPop.size() > 1)
                preyPop.remove(i);
            }
        }
    }

    private void checkForDeadPred() {
        for (int i = 0; i < predPop.size(); i++) {
            if (predPop.get(i).getCount() > 150) {
//                if (predPop.size() > 1)
                predPop.remove(i);
            }
        }
        
    }
    

}
