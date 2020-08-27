package Models;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Individual implements Collection{

    public float fitness = 0;
    public ArrayList<Buyer> genes = new ArrayList<>();
    public int geneLength = 15;
    public static Random rn = new Random();

    public Buyer get(int id) {
        return genes.get(id);
    }
    @Override
    public int size() {
        return genes.size();
    }
    @Override
    public boolean isEmpty() {
        return genes.isEmpty();
    }
    @Override
    public boolean contains(Object o) {
        return genes.contains(o);
    }
    @Override
    public Iterator iterator() {
        return null;
    }
    @Override
    public Object[] toArray() {
        Object[] o = {fitness, genes.toArray()};
        return o;
    }
    @Override
    public boolean add(Object o) {
        fitness = fitness + ((Buyer)o).price();
        return genes.add((Buyer) o);
    }
    @Override
    public boolean remove(Object o) {
        return genes.remove((Buyer)o);
    }
    @Override
    public boolean addAll(Collection c) {
        return genes.addAll(c);
    }
    @Override
    public void clear() {
        genes.clear();
    }
    @Override
    public boolean retainAll(Collection c) {
        return genes.retainAll(c);
    }
    @Override
    public boolean removeAll(Collection c) {
        return genes.retainAll(c);
    }
    @Override
    public boolean containsAll(Collection c) {
        return genes.containsAll(c);
    }
    @Override
    public Object[] toArray(Object[] a) {
        return genes.toArray(a);
    }
}