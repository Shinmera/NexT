/**********************\
  file: OptionSet
  package: util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * OptionSet to easily manage variables in one object.
 * Works especially nice in combination with ConfigManager.
 */
public class SimpleSet<K extends Object,V extends Object> implements Iterable<V>{
    private HashMap<K,V> map;
    private ArrayList<K> list;

    public SimpleSet(){map = new HashMap();list = new ArrayList();}
    public SimpleSet(HashMap<K,V> map2){
        map=map2;
        list = new ArrayList();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            list.add((K) pairs.getKey());
        }
    }
    public SimpleSet(ArrayList<V> list2){
        map = new HashMap();
        list = new ArrayList();
        for(int i=0;i<list2.size();i++){
            map.put((K) (i + ""), list2.get(i));
            list.add((K) (i + ""));
        }
    }
    
    public void clear(){map.clear();list.clear();}
    public void sort(){Collections.sort((List) list);}

    public int size(){return list.size();}

    public boolean isEmpty(){return list.isEmpty();}

    public boolean containsValue(V value){return map.containsValue(value);}
    public boolean containsKey(K key){return map.containsKey(key);}

    public int indexOfValue(V value){
        if(!map.containsValue(value))return -1;int tmp=0;
        for(int i=0;i<list.size();i++){if(map.get(list.get(i)).equals(value))break;tmp++;}
        return tmp;
    }
    public int indexOfKey(K key){return list.indexOf(key);}

    public void remove(K key){list.remove(key);map.remove(key);}
    public void removeAt(int i){map.remove(list.get(i));list.remove(i);}

    public V get(K key){return map.get(key);}
    public V getAt(int i){return map.get(list.get(i));}
    public K    getKey(int i){return list.get(i);}
    public HashMap   getMap(){return map;}
    public ArrayList getList(){return list;}

    public void put(K key,V value){if(!list.contains(key))list.add(key);map.put(key, value);}
    public void putAt(int i,V value){map.put(list.get(i), value);}
    public void setMap(HashMap map){this.map=map;}
    public void setList(ArrayList list){this.list=list;}

    public void add(SimpleSet set){
        map.putAll(set.getMap());
        list.addAll(set.getList());
    }

    public OptionSet asOptionSet(){
        OptionSet<K,ArrayList<V>> set = new OptionSet<K,ArrayList<V>>();
        for(int i=0;i<size();i++){
            ArrayList<V> temp = new ArrayList();
            temp.add(getAt(i));
            set.put(getKey(i),temp);
        }
        return set;
    }

    public String toString(){
        String s = "";
        for(int i=0;i<size();i++){
            s+=getKey(i)+": "+getAt(i)+"\n";
        }
        return s;
    }

    public Iterator iterator() {
        return map.values().iterator();
    }
}
