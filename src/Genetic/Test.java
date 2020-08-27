package Genetic;

import Models.Buyer;
import Models.Individual;

import java.util.*;

public class Test {
    public static void main(String[] args){
        Buyer.parse("in621");
        ArrayList<Individual> pop = new ArrayList<>();
        Individual c = new Individual();
        c.add(Buyer.getB(0));
        pop.add(c);
        Buyer bbbbbb = Buyer.getB(0);
        for (Buyer e : Buyer.list) {
            boolean isNotAdded;
            isNotAdded = true;
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
        Random rn = new Random();
        Individual    e1 = pop.get(rn.nextInt(pop.size())), e2 = pop.get(rn.nextInt(pop.size()));

        float price =0;
        for (int i = 0; i < e1.size(); i++) {
            price = price + e1.get(i).price();
        }
        System.out.println("" + e1.size() + " | " + price);
        for (int i = 0; i < e2.size(); i++) {
            price = price + e2.get(i).price();
        }
        System.out.println("" + e2.size() + " | " + price);
        Individual test = crossover(e1, e2);
        price =0;
        for (int i = 0; i < test.size(); i++) {
            price = price + test.get(i).price();
        }
        System.out.println(test.size() + " | " + price);
    }
    public static boolean checkB(Buyer b, Individual c) {
        if (c.contains(b)) return false;
        for (int i = 0; i < c.size(); i++) {
            if (b == c.get(i) || !c.get(i).isOk(b)) return false;
        }
        return true;
    }

    public static Individual crossover( Individual ...a) {
        Individual c = new Individual();
        for (Individual el : a) {
            c.addAll(el);
        }


        Collections.sort(c.genes, new SortBuyer("high-price"));
        //Collections.shuffle(c.genes);
        //Collections.shuffle(c.genes);
        Individual d = new Individual();

        for (int i = 0; i < c.size(); i++) {
            if (checkB( c.get(i), d)) d.add(c.get(i));
        }
        return d;
    }

}
class SortBuyer implements Comparator<Buyer>
{
    String type = "high-price"; // high-price, low-price, high-num-items or low-num-items
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