package KHM1;

/**
 * @author moyses
 */
import java.util.Vector;

import Complex.Complex;
import TransmissionLine.GenericCableModel;

public class KHM1 extends GenericCableModel{

    private double k1;
    private double k2;
    private double k3;
    private double h1;
    private double h2;
    private double cableLength;
    
	public double getCableLength() {
		return this.cableLength;
	}

    public KHM1(double k1, double k2, double k3, double h1, double h2, double cableLength) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
        this.h1 = h1;
        this.h2 = h2;
        this.cableLength = cableLength;
    }

    public Vector<Double> generateAttenuationConstant(Vector<Double> x){
        Vector<Double> alpha = new Vector<Double>();
        Vector<Complex> PC = this.generatePropagationConstant(x);
        for(int i = 0; i < x.size(); i++){
            alpha.add(PC.get(i).re());
        }
        return alpha;
    }

    public Vector<Double> generatePhaseConstant(Vector<Double> x){
        Vector<Double> beta = new Vector<Double>();
        Vector<Complex> PC = this.generatePropagationConstant(x);
        for(int i = 0; i < x.size(); i++){
            beta.add(PC.get(i).im());
        }
        return beta;
    }
    
    public Vector<Complex> generatePropagationConstant(Vector<Double> x){
        Vector<Complex> ghama = new Vector<Complex>();
        for(int i = 0; i < x.size(); i++){
        	ghama.add(new Complex(
        			(this.k1*Math.sqrt(x.get(i)) + this.k2*x.get(i))/1000
        			, 
        			(this.k1*Math.sqrt(x.get(i)) - this.k2*(2/Math.PI)*x.get(i)*Math.log(x.get(i)) + this.k3*x.get(i))/1000
        			));
        }
        return ghama;
    }

    public Vector<Double> generatePropagationConstantAbs(Vector<Double> x){
        Vector<Complex> ghama = this.generatePropagationConstant(x);
        Vector<Double> PC = new Vector<Double>();
        for(int i = 0; i < x.size(); i++){
        	PC.add(ghama.get(i).abs());
        }
        return PC;
    }

    public Vector<Double> generateRealCharacteristicImpedance(Vector<Double> x){
        Vector<Double> real = new Vector<Double>();
        Vector<Complex> CI = this.generateCharacteristicImpedance(x);
        for(int i = 0; i < x.size(); i++){
            real.add(CI.get(i).re());
        }
        return real;
    }

    public Vector<Double> generateImagCharacteristicImpedance(Vector<Double> x){
        Vector<Double> imag = new Vector<Double>();
        Vector<Complex> CI = this.generateCharacteristicImpedance(x);
        for(int i = 0; i < x.size(); i++){
            imag.add(CI.get(i).im());
        }
        return imag;
    }
    
    public Vector<Complex> generateCharacteristicImpedance(Vector<Double> x){
    	Vector<Complex> CI = new Vector<Complex>();
        for(int i = 0; i < x.size(); i++){
        	CI.add(new Complex(this.h1 + this.h2*(1/Math.sqrt(x.get(i))), -this.h2*(1/Math.sqrt(x.get(i)))));
        }
        return CI;
    }

    public Vector<Double> generateCharacteristicImpedanceAbs(Vector<Double> x){
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);
    	Vector<Double> CharacteristicImpedance = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++){
    		CharacteristicImpedance.add(CI.get(i).abs());
        }
        return CharacteristicImpedance;
    }

    public Vector<Double> generateTransferFunctionGain(Vector<Double> x){
    	Vector<Double> propagationLoss = new Vector<Double>();
    	Vector<Double> alpha = this.generateAttenuationConstant(x);
        for(int i = 0; i < x.size(); i++){
        	propagationLoss.add((-20/Math.log(10))*this.cableLength*alpha.get(i));
        }
        return propagationLoss;
    }
    
    public Vector<Double> generateSeriesResistance(Vector<Double> x) {
    
    	Vector<Double> resistance = new Vector<Double>();
    	
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);
    	Vector<Complex> PC = this.generatePropagationConstant(x);
    	
    	for(int i = 0; i < x.size(); i++) {
    		resistance.add( PC.get(i).times(CI.get(i)).re() );
    	}
    	
    	return resistance;
    	
    }

	public Vector<Double> generateShuntingConductance(Vector<Double> x) {

		Vector<Double> conductance = new Vector<Double>();
		
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);
    	Vector<Complex> PC = this.generatePropagationConstant(x);
    	
    	for(int i = 0; i < x.size(); i++) {

    		conductance.add(PC.get(i).divides(CI.get(i)).re());

    	}
    	
    	return conductance;
    			
	}

	public Vector<Double> generateSeriesInductance(Vector<Double> x) {

		Vector<Double> inductance = new Vector<Double>();

    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);
    	Vector<Complex> PC = this.generatePropagationConstant(x);
		
    	for(int i = 0; i < x.size(); i++) {
    		inductance.add(
    				PC.get(i).times(CI.get(i)).im()/(x.get(i)*Math.PI*2)
			);
    	}
    	
    	return inductance;

	}

	public Vector<Double> generateShuntingCapacitance(Vector<Double> x) {

		Vector<Double> capacitance = new Vector<Double>();
		
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);
    	Vector<Complex> PC = this.generatePropagationConstant(x);
    	
    	for(int i = 0; i < x.size(); i++) {
    		
    		capacitance.add(
    				PC.get(i).divides(CI.get(i)).im()/(x.get(i)*Math.PI*2)
			);
    	}
    	
    	return capacitance;
	}


}
