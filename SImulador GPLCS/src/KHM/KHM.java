package KHM;

/**
 * @author moyses
 */

import java.util.Vector;

public class KHM {

    private double k1;
    private double k2;
    private double k3;
    private double h1;
    private double h2;
    private double cableLength;
    
    public KHM(double k1, double k2, double k3, double h1, double h2, double cableLength) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.h1 = h1;
        this.h2 = h2;
        this.cableLength = cableLength;
    }

    public Vector generateAlpha(Vector x){
        Vector alpha = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*k1*sqrt(f) + k2*f*/
            alpha.add(this.k1*Math.sqrt(Double.parseDouble(x.get(i).toString())) + this.k2*Double.parseDouble(x.get(i).toString()));
        }
        return alpha;
    }

    public Vector generateBeta(Vector x){
        Vector beta = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*k1*sqrt(f) - k2*(2/pi)*f*ln(f) + k3*f*/
            beta.add(this.k1*Math.sqrt(Double.parseDouble(x.get(i).toString())) - this.k2*(2/Math.PI)*Double.parseDouble(x.get(i).toString())*Math.log(Double.parseDouble(x.get(i).toString())) + this.k3*Double.parseDouble(x.get(i).toString()));
        }
        return beta;
    }
    

}
