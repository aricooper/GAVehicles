/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gavehicles.classes;

import gavehicles.interfaces.Evaluable;
import java.util.Comparator;

/**
 *
 * @author Ari
 */
public class FitnessComparator implements Comparator<Evaluable>{
    

    @Override
    public int compare(Evaluable o1, Evaluable o2) {
        if (o1.getFitness() > o2.getFitness()) {
            return -1;
        } else if (o1.getFitness() < o2.getFitness()) {
            return 1;
        } else {
            return 0;
        }
    }
    
    
    
   

   

    
    
}

