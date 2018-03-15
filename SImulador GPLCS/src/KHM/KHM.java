package KHM;

/**
 * @author moyses
 */
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

    public Vector generateAlphaPropagationConstant(Vector x){
        Vector alpha = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*k1*sqrt(f) + k2*f*/
        	/*Neper -> dB = (20/ln(10))*Neper, But i don't use that */
            alpha.add(this.k1*Math.sqrt(Double.parseDouble(x.get(i).toString())) + this.k2*Double.parseDouble(x.get(i).toString()));
        }
        return alpha;
    }

    public Vector generateBetaPropagationConstant(Vector x){
        Vector beta = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*k1*sqrt(f) - k2*(2/pi)*f*ln(f) + k3*f*/
            beta.add(this.k1*Math.sqrt(Double.parseDouble(x.get(i).toString())) - this.k2*(2/Math.PI)*Double.parseDouble(x.get(i).toString())*Math.log(Double.parseDouble(x.get(i).toString())) + this.k3*Double.parseDouble(x.get(i).toString()));
        }
        return beta;
    }
    
    public Vector generatePropagationConstant(Vector x, Vector a, Vector b){
        Vector ghama = new Vector();
        for(int i = 0; i < x.size(); i++){
            /*sqrt(a^2 + b^2)*/
            ghama.add(Math.sqrt(Math.pow(Double.parseDouble(a.get(i).toString()), 2) + Math.pow(Double.parseDouble(b.get(i).toString()), 2)));
        }
        return ghama;
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
    
    public Vector generateCharacteristicImpedance(Vector x, Vector re, Vector im){
    	Vector CI = new Vector();
        for(int i = 0; i < x.size(); i++){
            CI.add(Math.sqrt(Math.pow(Double.parseDouble(re.get(i).toString()), 2) + Math.pow(Double.parseDouble(im.get(i).toString()), 2)));
        }
        return CI;
    }

    public Vector generateTransferFunctionGain(Vector x){
    	Vector propagationLoss = new Vector();
    	Vector alpha = this.generateAlphaPropagationConstant(x);
        for(int i = 0; i < x.size(); i++){
        	double value = (-20/Math.log(10))*this.cableLength*Double.parseDouble(alpha.get(i).toString());
        	propagationLoss.add(value);
        }
        return propagationLoss;
    }
    
    public Vector generateSeriesResistance(Vector x, Vector alpha, Vector beta, Vector real, Vector imag) {
    
    	Vector resistance = new Vector();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*real(PC * CI) = a*r - b*i, where i is not imaginary constant, is the imaginary part of CI*/
    		resistance.add(
    				(Double.parseDouble(alpha.get(i).toString()) * Double.parseDouble(real.get(i).toString()))
    				- 
    				(Double.parseDouble(beta.get(i).toString())  * Double.parseDouble(imag.get(i).toString()))
			);
    	}
    	
    	return resistance;
    	
    }

	public Vector generateShuntingConductance(Vector x, Vector alpha, Vector beta, Vector real, Vector imag) {

		Vector conductance = new Vector();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*real(PC/CI) = (a*r + b*i)/(r^2 + i^2), where i is not imaginary constant, is the imaginary part of CI*/
    		conductance.add(
	    				((Double.parseDouble(alpha.get(i).toString()) * Double.parseDouble(real.get(i).toString()))
	    				+
	    				(Double.parseDouble(beta.get(i).toString())  * Double.parseDouble(imag.get(i).toString())))
    				/
    					(Math.pow(Double.parseDouble(real.get(i).toString()), 2)
    							+			
    				    Math.pow(Double.parseDouble(imag.get(i).toString()), 2))
			);
    	}
    	
    	return conductance;
    			
	}

	public Vector generateSeriesInductance(Vector x, Vector alpha, Vector beta, Vector real, Vector imag) {

		Vector inductance = new Vector();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*imag(PC*CI) = a*i + b*r, where i is not imaginary constant, is the imaginary part of CI*/
    		inductance.add(
	    				(((Double.parseDouble(alpha.get(i).toString()) * Double.parseDouble(imag.get(i).toString()))
	    				+
	    				(Double.parseDouble (beta.get(i).toString()) * Double.parseDouble(real.get(i).toString()))))
	    				/Double.parseDouble(x.get(i).toString())
			);
    	}
    	
    	return inductance;

	}

	public Vector generateShuntingCapacitance(Vector x, Vector alpha, Vector beta, Vector real, Vector imag) {

		Vector capacitance = new Vector();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*imag(PC/CI) = (r*b - a*i)/(r^2 + i^2), where i is not imaginary constant, is the imaginary part of CI*/
    		capacitance.add(
		    				(((Double.parseDouble(beta.get(i).toString()) * Double.parseDouble(real.get(i).toString()))
		    				-
		    				(Double.parseDouble(alpha.get(i).toString())  * Double.parseDouble(imag.get(i).toString())))
	    				/
	    					((Math.pow(Double.parseDouble(real.get(i).toString()), 2)
	    							+			
	    				    Math.pow(Double.parseDouble(imag.get(i).toString()), 2))
	    							*
	    					Double.parseDouble(x.get(i).toString()))
	    					)
			);
    	}
    	
    	return capacitance;
	}


}
