package gavehicles.classes;

public class MyUtilities {

    
    /*
    Maximum Stimulus Strengths:
    food: 25.0-30.0 (with 10000 as max intensity)
    pred: 150.0-160.0 (with 10000 as max intensity)
    prey: 300.0-330.0(with 10000 as max intensity)
    */
    public static double MAX_STIM = 0.0;
    
    public static double THRESH = 1500.0;
    protected static boolean DEBUG = true;
    
    protected static int frameW = 1200;
    protected static int frameH = 800;
    protected static int food = 10;
    protected static int preySize = 50;
    protected static int predSize = 4;
    protected static int genTime = 500;
    protected static double growthRate = 0.3;    
    public static void debug(String s) {
        if (DEBUG) {
            System.out.println(s);
        }
    }
    
    public static int randomInt(int max) {
        return (int) (Math.random() * max);
    }
    
    public static int randomInt(int min, int max) {
        int returnMe = min;
        returnMe += (int) (Math.random() * (max - min));
        return returnMe;
    }
    
    public static double randomDouble(double max) {
        return Math.random() * max;
    }
    
    public static double randomDouble(double min, double max) {
        double returnMe = min;
        returnMe += (Math.random() * (max - min));
        return returnMe;
    }
    
    public static int getWidth() {
        return frameW;
    }
    
    public static int getHeight() {
        return frameH;
    }
    
    public static int getFood() {
        return food;
    }

    public static int getPreySize() {
        return preySize;
    }
    
    public static int getPredSize() {
        return predSize;
    }
    
    public static int getGenTime() {
        return genTime;
    }
    
    public static double getGrowthRate() {
        return growthRate;
    }
    
}
