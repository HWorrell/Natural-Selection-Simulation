/*

Copyright 2018 Christopher Heath Worrell

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

class Generation {


    private Vector<Person> population;

    Generation(int size, boolean malaria){
        population = new Vector<>();
        for(int i = 0; i < size; i++){
            population.add(new Person());
            if(malaria) {
                population.elementAt(i).malariaCheck();
            }
        }

    }

    Generation(Generation prev, int maxNumChildren, boolean malaria){
        Random rand = new Random();
        Stack<Person> females = new Stack<>();
        Stack<Person> males = new Stack<>();
        population = new Vector<>();
        int numCouples;

        int numMales = prev.getNumMales();

        int numFemales = prev.getNumFemales();
        
        Collections.shuffle(prev.population);


        if(numMales > numFemales){
            numCouples = numFemales;
        }
        else{
            numCouples = numMales;
        }
        int index = 0;
        while(females.size() < numCouples){
            if(prev.population.elementAt(index).isAlive() && prev.population.elementAt(index).isFemale()){
                females.push(prev.population.elementAt(index));
                prev.population.remove(index);
            }
            else{
                index++;
            }
        }

        //System.out.println("Females in stack: " + females.size());

        index = 0;
        while(males.size() < numCouples){
            if(prev.population.elementAt(index).isAlive() && prev.population.elementAt(index).isMale()){
                males.push(prev.population.elementAt(index));
                prev.population.remove(index);
            }
            else{
                index++;
            }
        }

        //System.out.println("Males in stack: " + males.size());

        int numChilren;
        //int tally = 0;

        for(int i = 0; i < numCouples; i++){
            numChilren = (int)((maxNumChildren + 1) * rand.nextDouble());
            for(int j = 0; j < numChilren; j++){
                population.add(new Person(males.peek(), females.peek()));
                if(malaria) {
                    population.lastElement().malariaCheck();
                }
                //tally++;
            }
            males.pop();
            females.pop();
        }
        //System.out.println("Total: " + tally);

    }

    int getTotalDeaths(){
        int deaths = 0;

        for(int i = 0; i < population.size(); i++){
            if(!population.elementAt(i).isAlive()){
                deaths++;
            }
        }

        return deaths;
    }
    int getNumMales(){
        int males = 0;
        for(int i = 0; i < population.size(); i++){
            if(population.elementAt(i).isMale() && population.elementAt(i).isAlive()){
                males++;
            }
        }
        return males;
    }
    int getNumFemales(){
        int females = 0;
        for(int i = 0; i < population.size(); i++){
            if(population.elementAt(i).isFemale() && population.elementAt(i).isAlive()){
                females++;
            }
        }

        return females;
    }

    int getDeathsFromMalaria(){
        int deathfrommalaria = 0;
        for(int i = 0; i < population.size(); i++){
            if(population.elementAt(i).getDiedFromMalaria()){
                deathfrommalaria++;
            }
        }
        return deathfrommalaria;
    }
    int getDeathsFromSickle(){
        int deathfromsickle = 0;
        for(int i = 0; i < population.size(); i++){
            if(population.elementAt(i).getDiedFromSickle()){
                deathfromsickle++;
            }
        }
        return deathfromsickle;
    }

    double percentOfPopulationWithSickleGene(){
        int hasSickle = 0;
        int live = 0;
        for(int i = 0; i < population.size(); i++){
            if(population.elementAt(i).isAlive() && (population.elementAt(i).sickleCarrier())){
                hasSickle++;
            }
            if(population.elementAt(i).isAlive()){
                live++;
            }
        }

        return ((double)hasSickle/(double)live);
    }
}
