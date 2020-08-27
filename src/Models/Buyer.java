package Models;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Buyer {
    public static ArrayList<Buyer> list = new ArrayList<Buyer>();
    private float price;
    private ArrayList<Integer> items = new ArrayList<Integer>();

    public Buyer(float p) { price = p; }

    public Buyer(String p) { price = Float.valueOf(p); }

    public float price() {return price; }

    public ArrayList<Integer> items() { return items; }

    public int size() {return items.size(); }

    public void add(int a) {items.add(a); }

    public void add(String a) {
        items.add(Integer.valueOf(a)); }

    public int get(int a) {return items.get(a); }

    public static Buyer getB(int id) { return list.get(id); }

    public boolean isOk(Buyer b) {
        for (int i = 0; i < items.size(); i++) {
            //System.out.println(items.get(i));
            if (b.items.contains(items.get(i))){
                //System.out.println("it has " + items.get(i));
                return false;
            }
        }
        return true;
    }
    public static void parse(String file) {
        try {
            //the file to be opened for reading
            FileInputStream fis=new FileInputStream("C:\\Users\\ali25\\Desktop\\GA\\instance\\" + file);
            Scanner sc=new Scanner(fis);    //file to be scanned
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] tmp = sc.nextLine().split(" ");
                Buyer b = new Buyer(tmp[0]);
                for (int i = 1; i < tmp.length; i++) {
                    b.add(tmp[i]);
                }
                list.add(b);
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Buyers: " + list.size());
    }
}
