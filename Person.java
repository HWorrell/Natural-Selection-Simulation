/*
Copyright 2018 Christopher Heath Worrell
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.Random;

class Person{
    private String sex;
    private String alleleOne;
    private String alleleTwo;
    private boolean alive;
    private boolean diedfromsickle;
    private boolean diedfrommalaria;
    //private static final double chanceOfDeathFromSickleCell = .25;//chance that a heterozygous sickle carrier will die
    //private static final double initialRateofSickleCell = .10;//rate of sickle cell in initial population.  On a per-allele basis
    //private static final double chanceofDeathFromMalaria = .5;//chance for a non-sickle to die from malaria.  Chance for hetere is .25 this rate

    Person(){
        Random rand = new Random();
        if(rand.nextDouble() < .5){
            this.sex = "M";
        }
        else{
            this.sex = "F";
        }
        if(rand.nextDouble() < MultiGenerationalInheritance.initialRateofSickleCell){
            this.alleleOne = "S";
        }
        else{
            this.alleleOne = "N";
        }
        if(rand.nextDouble() < MultiGenerationalInheritance.initialRateofSickleCell){
            this.alleleTwo = "S";
        }
        else{
            this.alleleTwo = "N";
        }
        if(this.alleleOne.equalsIgnoreCase("S") && this.alleleTwo.equalsIgnoreCase("S")){
            this.alive = false;
            this.diedfromsickle = true;
            this.diedfrommalaria = false;
        }
        else if(this.alleleOne.equalsIgnoreCase("S") || this.alleleTwo.equalsIgnoreCase("S")){
            if(rand.nextDouble() < MultiGenerationalInheritance.chanceOfDeathFromSickleCell){
                this.alive = false;
                this.diedfromsickle = true;
                this.diedfrommalaria = false;
            }
            else {
                this.alive = true;
                this.diedfromsickle = false;
                this.diedfrommalaria = false;
            }
        }
        else {
            this.alive = true;
            this.diedfromsickle = false;
            this.diedfrommalaria = false;
        }
    }

    /*
    Person(String s, String a1, String a2){
        this.sex = s;
        this.alleleOne = a1;
        this.alleleTwo = a2;
    }
    */

    Person(Person f, Person m){
        Random rand = new Random();
        if(rand.nextDouble() < .5){
            this.sex = "M";
        }
        else{
            this.sex = "F";
        }

        if(rand.nextDouble() < .5){
            this.alleleOne = f.getAllele(1);
        }
        else{
            this.alleleOne = m.getAllele(1);
        }

        if(rand.nextDouble() < .5){
            this.alleleTwo = f.getAllele(2);
        }
        else{
            this.alleleTwo = m.getAllele(2);
        }

        if(this.alleleOne.equalsIgnoreCase("S") && this.alleleTwo.equalsIgnoreCase("S")){
            this.alive = false;
            this.diedfromsickle = true;
            this.diedfrommalaria = false;
        }
        else if(this.alleleOne.equalsIgnoreCase("S") || this.alleleTwo.equalsIgnoreCase("S")) {
            if (rand.nextDouble() < .25) {
                this.alive = false;
                this.diedfromsickle = true;
                this.diedfrommalaria = false;
            } else {
                this.alive = true;
                this.diedfromsickle = false;
                this.diedfrommalaria = false;
            }
        }
        else {
            this.alive = true;
            this.diedfromsickle = false;
            this.diedfrommalaria = false;
        }

    }

    private String getAllele(int choice){
        if(choice == 1){
            return this.alleleOne;
        }
        else{
            return this.alleleTwo;
        }
    }

    boolean isMale(){
        return this.sex.equalsIgnoreCase("M");
    }
    boolean isFemale() { return this.sex.equalsIgnoreCase("F"); }

    boolean isAlive(){
        return this.alive;
    }

    void malariaCheck(){
        Random rand = new Random();
        if(this.alleleOne.equalsIgnoreCase("N") && this.alleleTwo.equalsIgnoreCase("N") && this.isAlive()){
            if(rand.nextDouble() < MultiGenerationalInheritance.chanceofDeathFromMalaria){
                this.alive = false;
                this.diedfrommalaria = true;
            }
        }
        else if(this.isAlive()){
            if(rand.nextDouble() < MultiGenerationalInheritance.chanceofDeathFromMalaria * .25){
                this.alive = false;
                this.diedfrommalaria = true;
            }
        }
    }

    boolean sickleCarrier(){
        if(this.alleleOne.equalsIgnoreCase("S") || this.alleleTwo.equalsIgnoreCase("S")){
            return true;
        }
        else{
            return false;
        }
    }

    boolean getDiedFromSickle(){
        return this.diedfromsickle;
    }

    boolean getDiedFromMalaria(){
        return this.diedfrommalaria;
    }

}