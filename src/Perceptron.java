import java.util.Arrays;

public class Perceptron {
    private double[] weightVector;
    private double theta;
    private double alpha;

    public Perceptron(int inputSize, double alpha) {
        this.weightVector = new double[inputSize];
        this.theta = 0;
        this.alpha = alpha;
        Arrays.fill(this.weightVector, 0.4);
        this.weightVector[inputSize - 1] = -1;
    }

    public double compute(double[] inputVec) {
        double y = 0;
        for (int j = 0; j < this.weightVector.length; j++) {
            y += inputVec[j] * this.weightVector[j];
        }
        return y >= 0 ? 1.0 : 0;
    }

    public void learn(double[] inputVec, double d) {
        double y = compute(inputVec);
        while (d - y != 0.0) {
            for (int j = 0; j < this.weightVector.length; j++) {
                this.weightVector[j] += (d - y) * this.alpha * inputVec[j];
            }
            y = compute(inputVec);
        }
    }

    public double[] getWeightVector() {
        return weightVector;
    }

    public double getTheta() {
        return theta;
    }
}
