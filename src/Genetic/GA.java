package Genetic;
import java.util.*;

import Models.*;

import java.io.*;

public class GA  implements Runnable{

    //private Population population = new Population();
    private ArrayList<Individual> pop = new ArrayList<>();
    private int generationCount = 0;
    private Individual goat;
    private int bestGen = 0;

    public static int[][] clauses;
    public static int[] pram = {0,0};
    private static Random rn = new Random();

    public static int geneCount = 0;
    public static float geneFit = 0;
    public static float wrsrFit = 0;


    // Parameters
    private static String file = "in605"; // cnf file
    private static int sleepTime = 100; // time between each two iterations
    private static int maxIteration = 10; // maximum number of generations

    private static int popSize = 1000;  // size of population pool
    private static int eliteSize = 100;  // size of elites pool
    private static int crossoverSize = 20;  // size of crossover pool aka the parents mating pool size
    private static String crossoverSortingType = "high-price";  // high-price, low-price, high-num-items or low-num-items

    private static boolean elitism = true; // don't worry about this

    private static int dco = 0; // Dynamic crossover, 0 for false, 1 for increasing, -1 for decreasing
    private static double crossoverRate = 1.0;  // crossover rate, it will not be taken in consideration if the dco is activated

    //private static Double mutationPercentage = .1; // percentage of the number of mutated individuals
    //private static Double mutationRate = 0.9;   // mutation rate


    private class SortPop implements Comparator<Individual> {
        public int compare(Individual a, Individual b){
            return (int) (b.fitness - a.fitness);
        }
    }

    private class SortBuyer implements Comparator<Buyer> {
        String type = GA.crossoverSortingType; // high-price, low-price, high-num-items or low-num-items
        public SortBuyer(String type) {this.type = type;}
        public int compare(Buyer a, Buyer b)
        {
            if (type.equals("high-price")) {
                return (int)(- a.price() + b.price());
            } else if (type.equals("low-price")) {
                return (int)(a.price() - b.price());
            } else if (type.equals("high-num-items")) {
                return (- a.size() + b.size());
            } else if (type.equals("low-num-items")) {
                return (a.size() - b.size());

            }
            return (int)(- a.price() + b.price());
        }
    }
    public static void main(String[] args) {
        new GA().run();
    }

    private void randomPop() {
        Individual c = new Individual();
        c.add(Buyer.getB(0));
        pop.add(c);
        Buyer bbbbbb = Buyer.getB(0);
        for (Buyer e : Buyer.list) {
            boolean isNotAdded = true;
            for (int i = 0; i < pop.size(); i++) {
                if (checkB(e, pop.get(i))) {
                    pop.get(i).add(e);
                    isNotAdded = false;
                }
            }
            if (isNotAdded) {
                c = new Individual();
                c.add(e);
                pop.add(c);
            }
        }
        for (Buyer e : Buyer.list) {
            for (int i = 0; i < pop.size(); i++) {
                if (checkB(e, pop.get(i))) {
                    pop.get(i).add(e);
                }
            }
        }
    }

    public static boolean checkB(Buyer b, Individual c) {
        if (c.contains(b)) return false;
        for (int i = 0; i < c.size(); i++) {
            if (b == c.get(i) || !c.get(i).isOk(b)) return false;
        }
        return true;
    }

    private void fit() {
        pop.sort(new SortPop());
        geneCount = generationCount;
        geneFit = pop.get(0).fitness;
        wrsrFit = pop.get(pop.size()-1).fitness;
    }

    public void run() {
        Buyer.parse(file);

        //Initialize population
        randomPop();
        ArrayList<Buyer> a = Buyer.list;
        System.out.println();
        //Calculate fitness of each individual
        fit();

        System.out.println("Generation: " + generationCount + " Fittest: " + pop.get(0).fitness + " / " + pram[1]);
        System.out.println("Best: ");
        System.out.print("\nf: " + pop.get(0).fitness + "\t|\t");
        //While population gets an individual with maximum fitness
        int co = 0;
        while (co < maxIteration) {
            co++;

            ++generationCount;

            System.out.println();
            //Do crossover
            reproduce();

            //Do mutation under a random probability
            //mutation(getMR());

            //Do selection
            fit();

            System.out.println("Generation: " + generationCount + " Fittest: " + pop.get(0).fitness + " / " + pram[1]);
/*            System.out.println("Elites: " + eliteSize);
            for (int i = 0; i < eliteSize; i++) {
                System.out.print("\nf: " + pop.get(i).fitness + "\t|\t");
                for (int j = 0; j < pop.get(i).geneLength; j++) {
                    System.out.print(pop.get(i).genes[j] + "\t ");
                }
            }*/
            try { Thread.sleep(sleepTime); } catch (Exception e) { System.out.println(e.getMessage());}

            if (goat == null || pop.get(0).fitness > goat.fitness) {
                goat = pop.get(0);
                bestGen = generationCount;
            }
        }
        System.out.println("Best generation: " + bestGen);
        System.out.print("f: " + goat.fitness + "\t|\t");
    }

    private double getCOR() {
        switch (dco) {
            case 0 : System.out.println("Crossover rate : " + crossoverRate); return crossoverRate;
            case 1 : System.out.println("Crossover rate : " + ((double)geneCount / (double)maxIteration)); return ((double)geneCount / (double)maxIteration);
            case -1: System.out.println("Crossover rate : " + (1 - ((double)geneCount / (double)maxIteration)) ); return 1  -  ((double)geneCount / (double)maxIteration);
            default: System.out.println("Crossover rate : " + crossoverRate); return crossoverRate;
        }

    }
    //Crossover
    public Individual crossover( ArrayList<Individual> a) {
        Individual c = new Individual();
        for (int i = 0; i < a.size(); i++) {
            c.addAll(a.get(i).genes);
        }

        Collections.sort(c.genes, new SortBuyer(crossoverSortingType));
        //Collections.shuffle(c.genes);
        //Collections.shuffle(c.genes);
        Individual d = new Individual();

        for (int i = 0; i < c.size(); i++) {
            if (checkB( c.get(i), d)) d.add(c.get(i));
        }
        return d;
    }

    private void reproduce() {
        ArrayList<Individual> newElites = new ArrayList<>();
        ArrayList<Individual> newPop = new ArrayList<>();

        for (int i = 0; i < eliteSize; i++) {
            newElites.add(pop.get(i));
        }
        int start = 0;

        if (elitism) {
            // adding the elites
            for (int i = 0; i < eliteSize; i++) {
                newPop.add(newElites.get(i));
            }
            start = eliteSize;
        }

        Double cor = getCOR();
        // adding the new offspring
        for (int i = start; i < (int)(popSize * cor); i++) { //
            ArrayList<Individual> a = new ArrayList<>();
            for (int j = 0; j < crossoverSize; j++) {
                a.add(pop.get(rn.nextInt(pop.size())));
            }
            newPop.add(crossover(a));
        }

        // fill the rest of old offspring

        for (int i = (int)(popSize * cor); i < popSize; i++) {
            newPop.add( pop.get(    rn.nextInt(pop.size())   )   );
        }
        pop = newPop;
    }

/*    private double getMR() {
        switch (dco) {
            case 0 : System.out.println("Mutation rate : " + mutationRate); return mutationRate;
            case 1 : System.out.println("Mutation rate : " + (1 - ((double)geneCount / (double)maxIteration)) ); return 1  -  ((double)geneCount / (double)maxIteration);
            case -1: System.out.println("Mutation rate : " + ((double)geneCount / (double)maxIteration)); return ((double)geneCount / (double)maxIteration);
            default: System.out.println("Mutation rate : " + mutationRate); return mutationRate;
        }

    }
    //Mutation
    private void mutation(double mtr) {
        // picking random individuals
        int end = (mtr == 0.0) ? 0 : rn.nextInt((int)(popSize * mtr));
        for (int i = 0; i < end; i++) {

            int ind_index = rn.nextInt(popSize);
            if (elitism)
                ind_index=+eliteSize;
            // mutate few genes

            for (int j = 0; j < (int)(pop.get(ind_index).geneLength * mutationPercentage); j++) {
                //Select a random mutation point
                int mutationPoint = rn.nextInt(pop.get(ind_index).geneLength);

                //System.out.println("Muatation to ind: " + ind_index + " at: " + mutationPoint);
                //Flip values at the mutation point
                //pop.get(ind_index).genes[mutationPoint] = (pop.get(ind_index).genes[mutationPoint] == -1) ? 1 : -1;
            }
        }
    }

    private static void parse(String fname) {
        try {
            //the file to be opened for reading
            FileInputStream fis=new FileInputStream("C:\\Users\\ali25\\Desktop\\GA\\src\\cnf\\" + fname + ".cnf");
            Scanner sc=new Scanner(fis);    //file to be scanned

            if (sc.hasNextLine()) {
                String[] tmp = sc.nextLine().split(" ");
                pram[0] = Integer.valueOf( tmp[0]);
                pram[1] = Integer.valueOf( tmp[1]);
                clauses = new int[pram[1]][pram[0]];
                System.out.println("[!] Creating matrix: " + pram[0] + "x" + pram[1]);
                for (int i = 0; i < pram[1]; i++) {
                    String[] vars = sc.nextLine().split(" ");
                    for (String var : vars) {
                        if( ! var.equals("") && !var.equals("\t")) {
                            int iv = Integer.valueOf(var);
                            if (iv != 0){
                                clauses[i][Math.abs(iv)-1] = Integer.signum(iv);
                            }
                        }
                    }
                }
                System.out.println("");
            }
            sc.close();     //closes the scanner
*//*            for (int i = 0; i < pram[1]; i++) {
                System.out.print("\n c:" + i);
                for (int j = 0; j < pram[0]; j++) {
                    System.out.print("\t" + clauses[i][j]);
                }
                System.out.println();
            }*//*
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/
}