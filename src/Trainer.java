import java.util.ArrayList;

public class Trainer {
    private Perceptron perceptron;
    private ArrayList<String[]> data;
    private double[] score;

    public Trainer(Perceptron perceptron, ArrayList<String[]> data) {
        this.perceptron = perceptron;
        this.data = data;
        this.score = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[data.get(0).length - 1].equals("Iris-setosa")) {
                this.score[i] = 1.0;
            } else {
                this.score[i] = 0.0;
            }
        }
    }

    public void train() {
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < data.size(); i++) {
                double[] inputVec = new double[data.get(0).length];
                for (int j = 0; j < inputVec.length - 1; j++) {
                    if (!data.get(i)[j].isEmpty()) {
                        inputVec[j] = Double.parseDouble(data.get(i)[j]);
                    } else {
                        inputVec[j] = 0.0;
                    }
                }
                inputVec[data.get(0).length - 1] = perceptron.getTheta();
                double y = perceptron.compute(inputVec);
                if (score[i] - y != 0.0) {
                    flag = true;
                    perceptron.learn(inputVec, score[i]);
                }
            }
        }
    }
}
