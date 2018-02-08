package gavehicles.vehicles;

public class Stimulus {

    double left, right;
    double weight;
    String source;
    
    public Stimulus() {
        
    }
    
    public Stimulus(double l, double r, String s) {
        this.left = l;
        this.right = r;
        this.source = s;
    }
    
    public double getLeft() {
        return left;
    }
    
    public double getRight() {
        return right;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setLeft(double l) {
        this.left = l;
    }
    
    public void setRight(double r) {
        this.right = r;
    }
    
    public void setWeight(double w) {
        this.weight = w;
    }
    
    public String mySource() {
        return source;
    }
    
    public boolean oversaturated(double thresh) {
        return (left >= thresh || right >= thresh);
    }
    
    public void multiply(double d) {
        setLeft(left * d);
        setRight(right * d);
    }
    
    public double getAvgStim() {
        return (getLeft() + getRight()) / 2;
    }

}
