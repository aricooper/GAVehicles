package gavehicles.UI;

import gavehicles.abstracts.IndividualVehicle;
import gavehicles.classes.Controller;
import gavehicles.interfaces.Modelable;
import gavehicles.interfaces.Viewable;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class MyPanel extends javax.swing.JPanel implements Viewable {

    MyFrame theFrame;
    Controller theController;
    Modelable theModel;

    public MyPanel() {
        initComponents();
    }

    public MyPanel(MyFrame f) {
        this();
        theFrame = f;
        theController = new Controller(this);
        theModel = theController.getModel();
        theController.start();
    }

    @Override
    public void display() {
        theFrame.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        theModel.paint(g);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    @Override
    public double getFoodStimulusStrength(IndividualVehicle v, Point2D.Double sensorLocation) {
        return theModel.getFoodStimulusStrength(sensorLocation, v);
    }

    @Override
    public double getPreyStimulusStrength(IndividualVehicle v, Point2D.Double sensorLocation) {
        return theModel.getPreyStimulusStrength(sensorLocation, v);
    }

    @Override
    public double getPredStimulusStrength(IndividualVehicle v, Point2D.Double sensorLocation) {
       return theModel.getPredStimulusStrength(sensorLocation, v);
    }
}
