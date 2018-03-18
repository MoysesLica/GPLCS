package TNO_EAB;

import java.util.Vector;
import Complex.Complex;

public class TNO_EAB {

	double Z0inf;
	double nVF;
	double Rs0;
	double qL;
	double qH;
	double qx;
	double qy;
	double qc;
	double phi;
	double fd;
	double cableLength;
	double c0 = 3e+8;
	double u0 = 4e-7*Math.PI;
	
    public TNO_EAB(double Z0inf, double nVF, double Rs0, double qL, double qH, double qx, double qy, double qc, double phi, double fd, double cableLength) {
        this.Z0inf = Z0inf;
        this.nVF = nVF;
        this.Rs0 = Rs0;
        this.qL = qL;
        this.qH = qH;
        this.qx = qx;
        this.qy = qy;
        this.qc = qc;
        this.phi = phi;
        this.fd = fd;
        this.cableLength = cableLength;
    }
    
    public double generateLsInf(){
    	return ( 1/(this.nVF * this.c0) )*this.Z0inf;
    }
    
    public double generateCp0() {
    	return (1/(this.nVF * this.c0))*(1/this.Z0inf);
    }
    
    public double generateqs() {
    	return 1/(Math.pow(this.qH, 2) * this.qL);
    }
    
    public double generateWs() {
    	return Math.pow(this.qH, 2) * ((4*Math.PI * this.Rs0)/this.u0);
    }
    
    public double generateWd() {
    	return 2*Math.PI*this.fd;
    }
    
    public Vector<Complex> generateSeriesImpedance(Vector<Double> x) {
    	Vector<Complex> Zs = new Vector<Complex>();
    	double qs = this.generateqs();
    	double ws = this.generateWs();
    	for(int i = 0; i < x.size(); i++) {
    		Complex numeradorParenteseMaisInterno   = new Complex(Math.pow(qs, 2),(this.qy*2*Math.PI*x.get(i))/ws);
    		Complex denominadorParenteseMaisInterno = new Complex((Math.pow(qs, 2))/(this.qx),(this.qy*2*Math.PI*x.get(i))/ws);
    		Complex resultado = numeradorParenteseMaisInterno.divides(denominadorParenteseMaisInterno);
    		Complex outroTermo = new Complex(0, (4*Math.PI*x.get(i))/ws);
    		Complex aindaOutroTermo = new Complex(Math.pow(qs, 2)*Math.pow(this.qx, 2), 0);
    		Complex novoResultado = outroTermo.times(resultado).plus(aindaOutroTermo);
    		Complex resultadoRaiz = novoResultado.sqrt();
    		Complex termoParaSomar = new Complex(1 - qs*this.qx,0);
    		Complex resultadoParentese = termoParaSomar.plus(resultadoRaiz);
    		Complex primeiroTermo = new Complex(0, this.generateLsInf()*x.get(i)*2*Math.PI);
    		Complex segundoTermo = resultadoParentese.times(new Complex(this.Rs0, 0));
    		Zs.add(primeiroTermo.plus(segundoTermo));
    	}
    	return Zs;
    }

    public Vector<Complex> generateParalelAdmitance(Vector<Double> x) {
    	Vector<Complex> Yp = new Vector<Complex>();
    	double wd = this.generateWd();
    	double Cp0 = this.generateCp0();
    	for(int i = 0; i < x.size(); i++) {
    		Complex termoElevado = new Complex(1, (2*Math.PI*x.get(i))/wd);
    		Complex termoDepoisDeElevado = termoElevado.pow(-2*this.phi/Math.PI);
    		Complex outroTermo = termoDepoisDeElevado.times(new Complex(1 - this.qc, 0));
    		Complex aindaOutroTermo = outroTermo.times(new Complex(0, 2*Math.PI*x.get(i)*Cp0));
    		Complex outroOutroTermo = new Complex(0, 2*Math.PI*x.get(i)*Cp0*this.qc);
    		Yp.add(aindaOutroTermo.plus(outroOutroTermo));
    	}
    	return Yp;
    }

    public Vector<Complex> generatePropagationConstant(Vector<Double> x) {
    	Vector<Complex> Zs = this.generateSeriesImpedance(x);
    	Vector<Complex> Yp = this.generateParalelAdmitance(x);
    	Vector<Complex> ghama = new Vector<Complex>();
    	for(int i = 0; i < x.size(); i++) {
    		ghama.add((Zs.get(i).times(Yp.get(i))).sqrt());
    	}
    	return ghama;
    }
    
    public Vector<Double> generateAttenuationConstant(Vector<Double> x){
    	Vector<Complex> PC = this.generatePropagationConstant(x);
    	Vector<Double> attenuation = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++) {
    		attenuation.add(PC.get(i).re());
    	}
    	return attenuation;
    }

	public Vector<Double> generatePhaseConstant(Vector<Double> x) {
    	Vector<Complex> PC = this.generatePropagationConstant(x);
    	Vector<Double> phase = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++) {
    		phase.add(PC.get(i).im());
    	}
    	return phase;
	}
	
	public Vector<Complex> generateCharacteristicImpedance(Vector<Double> x){
    	Vector<Complex> Zs = this.generateSeriesImpedance(x);
    	Vector<Complex> Yp = this.generateParalelAdmitance(x);
    	Vector<Complex> Zc = new Vector<Complex>();
    	for(int i = 0; i < x.size(); i++) {
    		Zc.add((Zs.get(i).divides(Yp.get(i))).sqrt());
    	}
    	return Zc;
	}
	
	public Vector<Double> generateRealCharacteristicImpedance(Vector<Double> x){
		Vector<Complex> CI = this.generateCharacteristicImpedance(x);
		Vector<Double> real = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++) {
    		real.add(CI.get(i).re());
    	}
    	return real;
	}

	public Vector<Double> generateImagCharacteristicImpedance(Vector<Double> x){
		Vector<Complex> CI = this.generateCharacteristicImpedance(x);
		Vector<Double> imag = new Vector<Double>();
    	for(int i = 0; i < x.size(); i++) {
    		imag.add(CI.get(i).im());
    	}
    	return imag;
	}
	
    public Vector<Double> generateTransferFunctionGain(Vector<Double> x){
    	Vector<Double> propagationLoss = new Vector<Double>();
    	Vector<Double> alpha = this.generateAttenuationConstant(x);
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
