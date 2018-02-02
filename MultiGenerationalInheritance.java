/*
Copyright 2018 Christopher Heath Worrell
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
This program simulates a series of generations of individuals, some carrying the Allele for Sickle-Cell, some not.
The program asks 7-8 questions to determine the parameters to be used in the simulation, then runs.
Available Parameters:
Initial Population Size
Maximum number of children each "couple" is permitted
Presence or abscense of Malaria
Probablity of death for a Heterozygous Sickle-Cell carrier
Probability of each allele in the initial population being a sickle-cell allele
Probability of a Homozygous non-sickle-cell individual dying from malaria.  Sickle-Cell Heterozygous individuals have only 25% of this risk.
Maximum number of generations to run.
On termination of the run, a report is generated, allowing in-depth comparison between generations.
The run can terminate in four ways:
1) User termination.
2) Reaching Pre-Determined number of Generations
3) Extinction of the Sickle-Cell allele
4) Extinction of the Human Race
User Termination may occur due to running time.  The complexity for the creation of each new generation is approximatly O(n^3), leading to very log calculation times if the population grows to very large numbers.
 */

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

class MultiGenerationalInheritance{
    static double chanceOfDeathFromSickleCell;
    static double initialRateofSickleCell;
    static double chanceofDeathFromMalaria;
    public static void main(String[] args) {

        //variables
        String input;
        String fileName;
        int parse;
        int numGenerationsToRun;
        boolean valid = false;
        boolean malariaPresent = false;
        int selection;
        Vector<String> generationLog = new Vector<>();

        //setup
        fileName = JOptionPane.showInputDialog("Please enter a file name for this run to be saved under.\nIt will be saved as <your_entry>.txt");

        if (fileName == null) {
            shutdownWithError("There was an error in choosing a file name. The prvided file name, " + null + ", is invalid. The program will exit.");
        }

        int sizeOfInitialGeneration = getAPositiveInt("Please enter a size for the initial generation:", 1);  //size of first generation.  Subsequent generations based on number of children

        int maxNumberOfChildren = getAPositiveInt("Please enter the maximum nuber of children that couples can have.\nPlease note that the size of families is an even distribution of all possible values.", 0);  //max number of children each couple can have.  < 4 will likely drive population extinct

        do {

            input = JOptionPane.showInputDialog("Please enter Yes for Malaria, or No for none.");

            //is malaria present
            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no") || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n")) {
                if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                    malariaPresent = true;
                }
                valid = true;
            }

        }while(!valid);

        parse = getABoundedInt("Please enter the chance of death from Sickle Cell for a Heterozygous carrier.\nPlease enter it as an integer from 0 and 100.", 0, 100);

        chanceOfDeathFromSickleCell = (double)parse/(double)100;//chance that a heterozygous sickle carrier will die

        parse = getABoundedInt("Please enter the chance of each allele being a sickle cell allele.\nPLEASE NOTE: This only applies to the first generation.", 0, 100);

        initialRateofSickleCell = (double)parse/(double)100;//rate of sickle cell in initial population.  On a per-allele basis

        if(malariaPresent) {

            parse = getABoundedInt("Please enter the chance for a Homozygous non-sickle cell individual to die from Malaria", 0, 100);

            chanceofDeathFromMalaria = (double) parse / (double) 100;//chance for a non-sickle to die from malaria.  Chance for hetere is .25 this rate

        }

        numGenerationsToRun = parse = getAPositiveInt("Please enter the maximum number of generations for this simulation.Note that in large population simulations,\nrunning time may be very long.", 0);

        Generation[] generations = new Generation[parse];  //how many generations to run for at most

        double[] percentCarriers = new double[parse];  //keep this equal to the previous lines number

        //end setup

        //begin simulation

        generations[0] = new Generation(sizeOfInitialGeneration, malariaPresent);

        percentCarriers[0] = generations[0].percentOfPopulationWithSickleGene();

        Object[] opt = {"Show each Step", "Autorun"};

        Object[] options = { "Continue", "Exit" };

        generationLog.add("Generation 1 has " + generations[0].getNumMales() + " Living males\n" +
                "Generation 1 has " + generations[0].getNumFemales() + " Living females\n" +
                "Generation 1 has " + generations[0].getTotalDeaths() + " Deaths, " + generations[0].getDeathsFromSickle() +
                " From sickle cell, and " + generations[0].getDeathsFromMalaria() + " From malaria\n" +
                "Generation 1 has " + 100 * percentCarriers[0] + " percent of population with sickle gene");

            selection = JOptionPane.showOptionDialog(null, generationLog.elementAt(0), "Result",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (selection == 1) {
            System.exit(0);
        }

        for(int i = 1; i < generations.length; i++){
            generations[i] = new Generation(generations[i - 1], maxNumberOfChildren, malariaPresent);

            percentCarriers[i] = generations[i].percentOfPopulationWithSickleGene();

            generationLog.add("Generation " + (i + 1) + " had " + generations[i].getTotalPopulation() + " born into it.\n" +
                    "Generation " + (i + 1) + " has " + generations[i].getNumMales() + " Living " +
                    "males\n" + "Generation " + (i + 1) + " has " + generations[i].getNumFemales() + " Living females\n" +
                    "Generation " + (i + 1) + " has " + generations[i].getTotalDeaths() + " Deaths, " + generations[i].getDeathsFromSickle() +
                    " From sickle cell, and " + generations[i].getDeathsFromMalaria() + " From malaria\n" +
                    "Generation " + (i + 1) + " has " + 100 * percentCarriers[i] + " percent of population with sickle gene");


                selection = JOptionPane.showOptionDialog(null, generationLog.elementAt(i), "Result",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (selection == 1) {

                generationLog.add("Simulation was ended manually by user");

                printToFile(fileName, generationLog);

                System.exit(0);
            }


            if(generations[i].percentOfPopulationWithSickleGene() == 0 && !(generations[i].getNumMales() == 0 && generations[i].getNumFemales() == 0)){


                JOptionPane.showMessageDialog(null, "The sickle cell gene has become extinct after " + (i + 1) + " generations.");

                generationLog.add("The sickle cell gene has become extinct after " + (i + 1) + " generations.");

                printToFile(fileName, generationLog);

                System.exit(0);

                break;
            }
            else if(generations[i].getNumMales() == 0 && generations[i].getNumFemales() == 0){


                JOptionPane.showMessageDialog(null, "The human race is extinct after " + (i + 1) + " generations");

                generationLog.add("The human race is extinct after " + (i + 1) + " generations");

                printToFile(fileName, generationLog);

                System.exit(0);

                break;
            }

        }

        generationLog.add("Simulation has ended after " + numGenerationsToRun);

        printToFile(fileName, generationLog);

        System.exit(0);

    }

    private static void shutdownWithError(String message){
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    private static int getABoundedInt(String message, int bottom, int top){
        int choice = 0;
        String input;
        boolean valid = false;
        do {

            input = JOptionPane.showInputDialog(message);

            try{
                choice = Integer.parseInt(input);
                valid = true;
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Please choose a valid value");
            }
            if(choice < bottom || choice > top){
                JOptionPane.showMessageDialog(null, "The number must be greater than or equal to " + bottom + " and less than or equal to " + top);
            }

        }while(!valid || choice < bottom || choice > top);

        return choice;
    }

    private static int getAPositiveInt(String message, int bottom){
        int choice = 0;
        String input;
        boolean valid = false;
        do {

            input = JOptionPane.showInputDialog(message);

            try{
                choice = Integer.parseInt(input);
                valid = true;
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Please choose a valid value");
            }
            if(choice < bottom){
                JOptionPane.showMessageDialog(null, "The number must be greater than or equal to " + bottom);
            }

        }while(!valid || choice < bottom);

        return choice;
    }

    private static void printToFile(String fileName, Vector<String> generationLog){
        try {
            PrintWriter output = new PrintWriter(new File(fileName + ".txt"));

            output.println("Settings used:\n");
            output.println("Chance of sickle allele: " + initialRateofSickleCell);
            output.println("Chance of death for Homo-Nonsickle: " + chanceofDeathFromMalaria);
            output.println("Chance of death for sickle-cell heterozygous: " + chanceOfDeathFromSickleCell);
            output.println("\n");
            output.println("Generational Data:");

            for(int j = 0; j < generationLog.size(); j++){
                output.println(generationLog.elementAt(j));
                output.println("\n");
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}