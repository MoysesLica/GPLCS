package BT0;

import java.util.Vector;
import Complex.Complex;
import TransmissionLine.GenericCableModel;

public class BT0 extends GenericCableModel{

    private double Roc;
    private double ac;
    private double L0;
    private double Linf;
    private double fm;
    private double Nb;
    private double g0;
    private double Nge;
    private double Cinf;
    private double C0;
    private double Nce;
    private double cableLength;

    public BT0(double Roc, double ac, double L0, double Linf, double fm, double Nb, double g0,
    		double Nge, double C0, double Cinf, double Nce, double cableLength) {

    	this.Roc = Roc;
    	this.ac = ac;
    	this.L0 = L0;
    	this.Linf = Linf;
    	this.fm = fm;
    	this.Nb = Nb;
    	this.g0 = g0;
    	this.Nge = Nge;
    	this.C0 = C0;
    	this.Cinf = Cinf;
    	this.Nce = Nce;
    	this.cableLength = cableLength;
    	
	}
    
    public Vector<Double> generateResistance(Vector<Double> x){
    	Vector<Double> resistance = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++)
    		resistance.add((Math.sqrt(Math.sqrt(Math.pow(this.Roc, 4) + (this.ac*Math.pow(x.get(i), 2)))))/1000);
    	return resistance;
    }

    public Vector<Double> generateConductance(Vector<Double> x){
    	Vector<Double> conductance = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++)
    		conductance.add((this.g0*Math.pow(x.get(i), this.Nge))/1000);
    	return conductance;
    }

    public Vector<Double> generateInductance(Vector<Double> x){
    	Vector<Double> inductance = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++)
    		inductance.add(((this.L0 + this.Linf*Math.pow((x.get(i)/this.fm), this.Nb))/(1 + Math.pow((x.get(i)/this.fm), this.Nb)))/1000);
    	return inductance;
    }

    public Vector<Double> generateCapacitance(Vector<Double> x){
    	Vector<Double> capacitance = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++)
    		capacitance.add((this.Cinf + (this.C0/Math.pow(x.get(i), this.Nce)))/1000);
    	return capacitance;
    }
    
    public Vector<Complex> generateSeriesImpedance(Vector<Double> x){    	
    	Vector<Complex> Zs = new Vector<Complex>();
    	
    	Vector<Double> R = this.generateResistance(x);
    	Vector<Double> L = this.generateInductance(x);
    	
    	for(int i = 0; i < R.size(); i++)
    		Zs.add(new Complex(R.get(i), Math.PI*x.get(i)*2*L.get(i)));    		
    	return Zs;
    }

    public Vector<Complex> generateParallelAdmittance(Vector<Double> x){    	
    	Vector<Complex> Yp = new Vector<Complex>();
    	
    	Vector<Double> R = this.generateResistance(x);
    	Vector<Double> G = this.generateConductance(x);
    	Vector<Double> C = this.generateCapacitance(x);

    	for(int i = 0; i < R.size(); i++)
    		Yp.add(new Complex(G.get(i), Math.PI*x.get(i)*2*C.get(i)));    		
    	return Yp;
    }

    public Vector<Complex> generatePropagationConstant(Vector<Double> x){    	
    	Vector<Complex> Zs = this.generateSeriesImpedance(x);
    	Vector<Complex> Yp = this.generateParallelAdmittance(x);

    	Vector<Complex> PC = new Vector<Complex>();
    	for(int i = 0; i < Zs.size(); i++)
    		PC.add((Zs.get(i).times(Yp.get(i))).sqrt());    		
    	return PC;
    }

    public Vector<Complex> generateCharacteristicImpedance(Vector<Double> x){    	
    	Vector<Complex> Zs = this.generateSeriesImpedance(x);
    	Vector<Complex> Yp = this.generateParallelAdmittance(x);

    	Vector<Complex> CI = new Vector<Complex>();
    	for(int i = 0; i < Zs.size(); i++)
    		CI.add((Zs.get(i).divides(Yp.get(i))).sqrt());    		
    	return CI;
    }
    
    public Vector<Double> generateAttenuationConstant(Vector<Double> x){
    	Vector<Complex> PC = this.generatePropagationConstant(x);

    	Vector<Double> alpha = new Vector<Double>();
    	for(int i = 0; i < PC.size(); i++)
    		alpha.add(PC.get(i).re());    		
    	return alpha;
    }

    public Vector<Double> generatePhaseConstant(Vector<Double> x){
    	Vector<Complex> PC = this.generatePropagationConstant(x);

    	Vector<Double> beta = new Vector<Double>();
    	for(int i = 0; i < PC.size(); i++)
    		beta.add(PC.get(i).im());    		
    	return beta;
    }

    public Vector<Double> generateRealCharacteristicImpedance(Vector<Double> x){
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);

    	Vector<Double> real = new Vector<Double>();
    	for(int i = 0; i < CI.size(); i++)
    		real.add(CI.get(i).re());    		
    	return real;
    }

    public Vector<Double> generateImagCharacteristicImpedance(Vector<Double> x){
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);

    	Vector<Double> imag = new Vector<Double>();
    	for(int i = 0; i < CI.size(); i++)
    		imag.add(CI.get(i).im());    		
    	return imag;
    }
    
    public Vector<Double> generatePropagationConstantAbs(Vector<Double> x){
    	Vector<Complex> PC = this.generatePropagationConstant(x);

    	Vector<Double> abs = new Vector<Double>();
    	for(int i = 0; i < PC.size(); i++)
    		abs.add(PC.get(i).abs());    		
    	return abs;    	
    }

    public Vector<Double> generateCharacteristicImpedanceAbs(Vector<Double> x){
    	Vector<Complex> CI = this.generateCharacteristicImpedance(x);

    	Vector<Double> abs = new Vector<Double>();
    	for(int i = 0; i < CI.size(); i++)
    		abs.add(CI.get(i).abs());    		
    	return abs;    	
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
