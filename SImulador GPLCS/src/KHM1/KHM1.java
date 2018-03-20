package KHM1;

/**
 * @author moyses
 */
import java.util.Vector;

public class KHM1 {

    private double k1;
    private double k2;
    private double k3;
    private double h1;
    private double h2;
    private double cableLength;
    
    public KHM1(double k1, double k2, double k3, double h1, double h2, double cableLength) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.h1 = h1;
        this.h2 = h2;
        this.cableLength = cableLength;
    }

    public Vector<Double> generateAlphaPropagationConstant(Vector<Double> x){
        Vector<Double> alpha = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
            /*k1*sqrt(f) + k2*f*/
            alpha.add((this.k1*Math.sqrt(x.get(i)) + this.k2*x.get(i)));
        }
        return alpha;
    }

    public Vector<Double> generateBetaPropagationConstant(Vector<Double> x){
        Vector<Double> beta = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
            /*k1*sqrt(f) - k2*(2/pi)*f*ln(f) + k3*f*/
            beta.add((this.k1*Math.sqrt(x.get(i)) - this.k2*(2/Math.PI)*x.get(i)*Math.log(x.get(i)) + this.k3*x.get(i)));
        }
        return beta;
    }
    
    public Vector<Double> generatePropagationConstant(Vector<Double> x, Vector<Double> a, Vector<Double> b){
        Vector<Double> ghama = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
            /*sqrt(a^2 + b^2)*/
            ghama.add(Math.sqrt(Math.pow(a.get(i), 2) + Math.pow(b.get(i), 2)));
        }
        return ghama;
    }

    public Vector<Double> generateRealCharacteristicImpedance(Vector<Double> x){
        Vector<Double> real = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
            /*h1 + h2*(1/sqrt(f))*/
            real.add(this.h1 + this.h2*(1/Math.sqrt(x.get(i))));
        }
        return real;
    }

    public Vector<Double> generateImagCharacteristicImpedance(Vector<Double> x){
        Vector<Double> imag = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
            /*h1 + h2*(1/sqrt(f))*/
            imag.add(- this.h2*(1/Math.sqrt(x.get(i))));
        }
        return imag;
    }
    
    public Vector<Double> generateCharacteristicImpedance(Vector<Double> x, Vector<Double> re, Vector<Double> im){
    	Vector<Double> CI = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
            CI.add(Math.sqrt(Math.pow(re.get(i), 2) + Math.pow(im.get(i), 2)));
        }
        return CI;
    }

    public Vector<Double> generateTransferFunctionGain(Vector<Double> x){
    	Vector<Double> propagationLoss = new Vector<Double>();
    	Vector<Double> alpha = this.generateAlphaPropagationConstant(x);
        for(int i = 0; i < x.size(); i++){
        	propagationLoss.add((-20/Math.log(10))*this.cableLength*alpha.get(i));
        }
        return propagationLoss;
    }
    
    public Vector<Double> generateSeriesResistance(Vector<Double> x, Vector<Double> alpha, Vector<Double> beta, Vector<Double> real, Vector<Double> imag) {
    
    	Vector<Double> resistance = new Vector<Double>();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*real(PC * CI) = a*r - b*i, where i is not imaginary constant, is the imaginary part of CI*/
    		resistance.add(
    				(alpha.get(i) * real.get(i))
    				- 
    				(beta.get(i)  * imag.get(i))
			);
    	}
    	
    	return resistance;
    	
    }

	public Vector<Double> generateShuntingConductance(Vector<Double> x, Vector<Double> alpha, Vector<Double> beta, Vector<Double> real, Vector<Double> imag) {

		Vector<Double> conductance = new Vector<Double>();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*real(PC/CI) = (a*r + b*i)/(r^2 + i^2), where i is not imaginary constant, is the imaginary part of CI*/
    		conductance.add(
	    				(alpha.get(i) * real.get(i)
	    				+
	    				(beta.get(i)  * imag.get(i)))
    				/
    					(Math.pow(real.get(i), 2)
    							+			
    				    Math.pow(imag.get(i), 2))
			);
    	}
    	
    	return conductance;
    			
	}

	public Vector<Double> generateSeriesInductance(Vector<Double> x, Vector<Double> alpha, Vector<Double> beta, Vector<Double> real, Vector<Double> imag) {

		Vector<Double> inductance = new Vector<Double>();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*imag(PC*CI) = a*i + b*r, where i is not imaginary constant, is the imaginary part of CI*/
    		inductance.add(
	    				((alpha.get(i) * imag.get(i)) + (beta.get(i)    * real.get(i)))
	    				/x.get(i)
			);
    	}
    	
    	return inductance;

	}

	public Vector<Double> generateShuntingCapacitance(Vector<Double> x, Vector<Double> alpha, Vector<Double> beta, Vector<Double> real, Vector<Double> imag) {

		Vector<Double> capacitance = new Vector<Double>();
    	
    	for(int i = 0; i < x.size(); i++) {
    		/*imag(PC/CI) = (r*b - a*i)/(r^2 + i^2), where i is not imaginary constant, is the imaginary part of CI*/
    		capacitance.add(
		    				( (beta.get(i) * real.get(i))  - (alpha.get(i) * imag.get(i)) )
	    				/
	    					((Math.pow(real.get(i), 2) + Math.pow(imag.get(i), 2)) * x.get(i))
			);
    	}
    	
    	return capacitance;
	}


}
