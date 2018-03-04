package KHM;

/**
 * @author moyses
 */
import org.nevec.rjm.*;
import java.math.BigDecimal;
import java.util.Vector;
import javafx.scene.control.Alert;

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
    
    public Vector generateRealCharacteristicImpedance(Vector x){
        Vector real = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*h1 + h2*(1/sqrt(f))*/
            real.add(this.h1 + this.h2*(1/Math.sqrt(Double.parseDouble(x.get(i).toString()))));
        }
        return real;
    }

    public Vector generateImagCharacteristicImpedance(Vector x){
        Vector imag = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*h1 + h2*(1/sqrt(f))*/
            imag.add(- this.h2*(1/Math.sqrt(Double.parseDouble(x.get(i).toString()))));
        }
        return imag;
    }

    public Vector generateTransferFunction(Vector x){

        Vector real = new Vector();
        Vector imag = new Vector();
        Vector alpha = this.generateAlpha(x);
        Vector beta = this.generateBeta(x);
        for(int i = 0; i < x.size(); i++){
            /*real = e^(-d*gama) = e^(-d*alpha)*e^(-d*beta)*/

        	

        	
        }
        
        return real;

    }


    public Vector generateTransferFunctionGain(Vector x){
    	Vector propagationLoss = new Vector();
    	Vector alpha = this.generateAlpha(x);
        for(int i = 0; i < x.size(); i++){
        	double value = (-20/Math.log(10))*this.cableLength*Double.parseDouble(alpha.get(i).toString());
        	propagationLoss.add(value);
        }
        return propagationLoss;
    }


}
