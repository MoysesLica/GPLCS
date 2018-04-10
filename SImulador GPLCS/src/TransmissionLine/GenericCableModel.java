package TransmissionLine;

import java.util.Vector;

import Complex.Complex;

public abstract class GenericCableModel {

	public abstract Vector<Double> generateAttenuationConstant(Vector<Double> x);
	public abstract Vector<Double> generatePhaseConstant(Vector<Double> x);
	public abstract Vector<Complex>generatePropagationConstant(Vector<Double> x);
	public abstract Vector<Double> generatePropagationConstantAbs(Vector<Double> x);
	public abstract Vector<Double> generateRealCharacteristicImpedance(Vector<Double> x);
	public abstract Vector<Double> generateImagCharacteristicImpedance(Vector<Double> x);
	public abstract Vector<Complex>generateCharacteristicImpedance(Vector<Double> x);
	public abstract Vector<Double> generateCharacteristicImpedanceAbs(Vector<Double> x);
	public abstract Vector<Double> generateSeriesResistance(Vector<Double> x);
	public abstract Vector<Double> generateSeriesInductance(Vector<Double> x);
	public abstract Vector<Double> generateShuntingConductance(Vector<Double> x);
	public abstract Vector<Double> generateShuntingCapacitance(Vector<Double> x);
	public abstract Vector<Double> generateTransferFunctionGain(Vector<Double> x);
	
	public abstract double getCableLength();
	
}
