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

        String input;

        Vector<String> generationLog = new Vector<>();

        String fileName;

        fileName = JOptionPane.showInputDialog("Please enter a file name for this run to be saved under.\nIt will be saved as <your_entry>.txt");

        input = JOptionPane.showInputDialog("Please enter a size for the initial generation:");

        int sizeOfInitialGeneration = Integer.parseInt(input);  //size of first generation.  Subsequent generations based on number of children

        input = JOptionPane.showInputDialog("Please enter the maximum nuber of children that couples can have.\nPlease note that the size of families is an even distribution of all possible values.");

        int maxNumberOfChildren = Integer.parseInt(input);  //max number of children each couple can have.  < 4 will likely drive population extinct

        input = JOptionPane.showInputDialog("Please enter Yes for Malaria, or No for none.");

        boolean malariaPresent;

        //is malaria present
        malariaPresent = input.equalsIgnoreCase("yes");

        input = JOptionPane.showInputDialog("Please enter the chance of death from Sickle Cell for a Heterozygous carrier.\nPlease enter it as an integer.");

        int parse = Integer.parseInt(input);

        chanceOfDeathFromSickleCell = (double)parse/(double)100;//chance that a heterozygous sickle carrier will die

        input = JOptionPane.showInputDialog("Please enter the chance of each allele being a sickle cell allele.\nPLEASE NOTE: This only applies to the first generation.");

        parse = Integer.parseInt(input);

        initialRateofSickleCell = (double)parse/(double)100;//rate of sickle cell in initial population.  On a per-allele basis

        if(malariaPresent) {

            input = JOptionPane.showInputDialog("Please enter the chance for a Homozygous non-sickle cell individual to die from Malaria");

            parse = Integer.parseInt(input);

            chanceofDeathFromMalaria = (double) parse / (double) 100;//chance for a non-sickle to die from malaria.  Chance for hetere is .25 this rate

        }

        input = JOptionPane.showInputDialog("Please enter the maximum number of generations for this simulation.\nNote that in large population simulations,\nrunning time may be very long.");

        Generation[] generations = new Generation[Integer.parseInt(input)];  //how many generations to run for at most

        double[] percentCarriers = new double[Integer.parseInt(input)];  //keep this equal to the previous lines number

        generations[0] = new Generation(sizeOfInitialGeneration, malariaPresent);

        percentCarriers[0] = generations[0].percentOfPopulationWithSickleGene();

        Object[] options = { "Continue", "Exit" };

        int selection;

        generationLog.add("Generation 1 has " + generations[0].getNumMales() + " Living males\n" +
                "Generation 1 has " + generations[0].getNumFemales() + " Living females\n" +
                "Generation 1 has " + generations[0].getTotalDeaths() + " Deaths, " + generations[0].getDeathsFromSickle() +
                " From sickle cell, and " + generations[0].getDeathsFromMalaria() + " From malaria\n" +
                "Generation 1 has " + 100 * percentCarriers[0] + " percent of population with sickle gene");

        selection = JOptionPane.showOptionDialog(null,generationLog.elementAt(0), "Result",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if(selection == 1){
            System.exit(0);
        }

        //System.out.println("Generation 1 has " + generations[0].getNumMales() + " Living males");

        //System.out.println("Generation 1 has " + generations[0].getNumFemales() + " Living females");

        //System.out.println("Generation 1 has " + generations[0].getTotalDeaths() + " Deaths, " + generations[0].getDeathsFromSickle()
        // + " From sickle cell, and " + generations[0].getDeathsFromMalaria() + " From malaria");

        //System.out.println("Generation 1 has " + 100 * percentCarriers[0] + " percent of population with sickle gene\n\n");

        for(int i = 1; i < generations.length; i++){
            generations[i] = new Generation(generations[i - 1], maxNumberOfChildren, malariaPresent);

            percentCarriers[i] = generations[i].percentOfPopulationWithSickleGene();

            generationLog.add("Generation " + (i + 1) + " had " + generations[i].getTotalPopulation() + " born into it.\n" +
                    "Generation " + (i + 1) + " has " + generations[i].getNumMales() + " Living " +
                    "males\n" + "Generation " + (i + 1) + " has " + generations[i].getNumFemales() + " Living females\n" +
                    "Generation " + (i + 1) + " has " + generations[i].getTotalDeaths() + " Deaths, " + generations[i].getDeathsFromSickle() +
                    " From sickle cell, and " + generations[i].getDeathsFromMalaria() + " From malaria\n" +
                    "Generation " + (i + 1) + " has " + 100 * percentCarriers[i] + " percent of population with sickle gene");

            selection = JOptionPane.showOptionDialog(null,generationLog.elementAt(i), "Result",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if(selection == 1){

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

                System.exit(0);
            }

            //System.out.println("Generation " + (i + 1) + " has " + generations[i].getNumMales() + " Living males");

            //System.out.println("Generation " + (i + 1) + " has " + generations[i].getNumFemales() + " Living females");

            //System.out.println("Generation " + (i + 1) + " has " + generations[i].getTotalDeaths() + " Deaths, " + generations[i].getDeathsFromSickle()
            //+ " From sickle cell, and " + generations[i].getDeathsFromMalaria() + " From malaria");

            //System.out.println("Generation " + (i + 1) + " has " + 100 * percentCarriers[i] + " percent of population with sickle gene\n\n");

            if(generations[i].percentOfPopulationWithSickleGene() == 0 && !(generations[i].getNumMales() == 0 && generations[i].getNumFemales() == 0)){

                JOptionPane.showMessageDialog(null, "The sickle cell gene has become extinct after " + (i + 1) + " generations.");

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
                    output.println("The sickle cell gene has become extinct after " + (i + 1) + " generations.");
                    output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                System.exit(0);

                //System.out.println("The sickle cell gene has become extinct after " + (i + 1) + " Generations");

                break;
            }
            else if(generations[i].getNumMales() == 0 && generations[i].getNumFemales() == 0){

                JOptionPane.showMessageDialog(null, "The human race is extinct after " + (i + 1) + " generations");

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
                    output.println("The human race is extinct after " + (i + 1) + " generations");
                    output.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                System.exit(0);

                break;
            }

        }

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
            //output.println("The human race is extinct after " + (generations.length) + " generations");
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.exit(0);

    }

}