import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String file = "/Users/kacperkus/Desktop/NAI_PROJECT_2/iris_training.txt";
        String secfile = "/Users/kacperkus/Desktop/NAI_PROJECT_2/iris_test.txt";

        ArrayList<String[]> data = readData(file);
        int lineLength = data.get(0).length;
        Perceptron perceptron = new Perceptron(lineLength, 0.3);

        Trainer trainer = new Trainer(perceptron, data);
        trainer.train();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Wybierz opcję:");
            System.out.println("1. Dokonaj predykcji na danych wprowadzonych przez użytkownika");
            System.out.println("2. Ewaluacja modelu na podstawie danych testowych");
            System.out.println("3. Ewaluacja modelu na podstawie pliku podanego przez użytkownika");
            System.out.println("0. Wyjście");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    predictUserInput(lineLength, perceptron);
                    break;
                case 2:
                    evaluate(lineLength, secfile, perceptron);
                    break;
                case 3:
                    System.out.println("Podaj ścieżkę do pliku:");
                    String userFile = scanner.nextLine();
                    evaluateModel(readData(userFile), lineLength, perceptron);
                    break;
                case 0:
                    System.out.println("Zamykanie programu...");
                    System.exit(0);
                default:
                    System.out.println("Niepoprawna opcja. Wybierz ponownie.");
            }
        }
    }

    public static void predictUserInput(int lineLength, Perceptron perceptron) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("podaj parametry: ");
        double[] parameters = new double[lineLength];
        for (int i = 0; i < parameters.length - 1; i++) {
            parameters[i] = scanner.nextDouble();
            if (parameters[i] == -111) {
                throw new RuntimeException("bledne parametry");
            }
        }
        parameters[lineLength - 1] = perceptron.getTheta();
        double re = 0;
        for (int i = 0; i < parameters.length; i++) {
            re += parameters[i] * perceptron.getWeightVector()[i];
        }
        System.out.print("typ: ");
        System.out.println(re >= 0 ? "Iris-setosa" : "Iris-versicolor");
    }

    public static void evaluateModel(ArrayList<String[]> data, int lineLength, Perceptron perceptron) {
        Collections.shuffle(data);
        int trainingSize = (int) (data.size() * 0.7);
        ArrayList<String[]> trainingData = new ArrayList<>(data.subList(0, trainingSize));
        ArrayList<String[]> testData = new ArrayList<>(data.subList(trainingSize, data.size()));

        Trainer trainer = new Trainer(perceptron, trainingData);
        trainer.train();

        int count = 0;
        for (String[] instance : testData) {
            double[] inputVec = new double[lineLength];
            for (int j = 0; j < lineLength - 1; j++) {
                inputVec[j] = Double.parseDouble(instance[j]);
            }
            inputVec[lineLength - 1] = perceptron.getTheta();
            double result = perceptron.compute(inputVec);
            double expected = instance[lineLength - 1].equals("Iris-setosa") ? 1.0 : 0.0;
            if (result == expected) {
                count++;
            }
        }
        System.out.println("Prawidłowo zaklasyfikowane przykłady: " + count + "/" + testData.size());
        System.out.println("Dokładność eksperymentu: " + ((double) count / testData.size()) * 100 + "%");
    }

    public static void evaluate(int lineLength, String file, Perceptron perceptron) {
        ArrayList<String[]> checkdata = readData(file);
        int count = 0;
        for (String[] instance : checkdata) {
            double[] inputVec = new double[lineLength];
            for (int j = 0; j < lineLength - 1; j++) {
                inputVec[j] = Double.parseDouble(instance[j]);
            }
            inputVec[lineLength - 1] = perceptron.getTheta();
            double result = perceptron.compute(inputVec);
            double expected = instance[lineLength - 1].equals("Iris-setosa") ? 1.0 : 0.0;
            if (result == expected) {
                count++;
            }
        }
        System.out.println("Prawidłowo zaklasyfikowane przykłady: " + count + "/" + checkdata.size());
        System.out.println("Dokładność eksperymentu: " + ((double) count / checkdata.size()) * 100 + "%");
    }

    public static ArrayList<String[]> readData(String file) {
        ArrayList<String[]> data = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(",", ".");
                line = line.replaceFirst("^\\s+", "");
                String[] atr = line.split("\\s+");
                data.add(atr);
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
