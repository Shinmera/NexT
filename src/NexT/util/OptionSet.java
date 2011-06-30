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

/*
 * OptionSet to easily manage variables in one object.
 * Works especially nice in combination with ConfigManager.
 */
public class OptionSet<K extends Object,V extends ArrayList> {
    private HashMap<K,V> map;
    private ArrayList<K> list;

    public OptionSet(){map = new HashMap();list = new ArrayList();}
    
    public void clear(){map.clear();list.clear();}

    public int size(){return list.size();}
    public int sizeOf(K key){
        if(list.contains(key))return map.get(key).size();
        else return -1;
    }

    public boolean isEmpty(){return list.isEmpty();}

    public boolean containsValue(V value){return map.containsValue(value);}
    public boolean containsKey(K key){return map.containsKey(key);}
    public boolean containsValue(K value,Object o){return map.get(value).contains(o);}

    public int indexOfValue(V value){
        if(!map.containsValue(value))return -1;int tmp=0;
        for(int i=0;i<list.size();i++){if(map.get(list.get(i)).equals(value))break;tmp++;}
        return tmp;
    }
    public int indexOfKey(K key){return list.indexOf(key);}

    public void remove(K key){list.remove(key);map.remove(key);}
    public void remove(K key,Object o){map.get(key).remove(o);}
    public void remove(K key,int i){map.get(key).remove(i);}
    public void remove(int i){map.remove(list.get(i));list.remove(i);}
    public void removeAllAt(int j){
        for(int i=0;i<size();i++)get(i).remove(j);
    }

    public ArrayList get(K key){return map.get(key);}
    public ArrayList get(int i){return map.get(list.get(i));}
    public Object getF(K key){return map.get(key).get(0);}
    public Object getF(int i){return map.get(list.get(i)).get(0);}
    public K        getKey(int i){return list.get(i);}
    public HashMap   getMap(){return map;}
    public ArrayList getList(){return list;}

    /**
     * Gog damn I don't need to explain this shit, it's obvious.
     * @param key
     * @param value
     */
    public void put(K key,V value){list.add(key);map.put(key, value);}

    /**
     * Sets the value at position i.
     * @param i The position.
     * @param value The value.
     */
    public void put(int i,V value){map.put(list.get(i), value);}

    /**
     * Set the internal values list.
     * @param map
     */
    public void setMap(HashMap map){this.map=map;}

    /**
     * Set the internal key list.
     * @param list
     */
    public void setList(ArrayList list){this.list=list;}

    public void setF(K key,Object value){map.get(key).set(0, value);}
    public void setF(int i,Object value){map.get(list.get(i)).set(0,value);}

    /**
     * Merges the two option sets.
     * @param set The set to add.
     */
    public void add(OptionSet set){
        for(int i=0;i<set.size();i++){
            K key = (K) set.getKey(i);
            if(containsKey(key)){
                addList(key,(V)set.get(key));
                set.remove(i);
            }
        }
        map.putAll(set.getMap());
        list.addAll(set.getList());
    }

    /**
     * Saves {@link value} as an entry in the {@link key} list.
     * @param key The key to save the value under
     * @param value The value to save
     */
    public void add(K key,Object value){
        if(!containsKey(key)){
            list.add(key);
            map.put(key, (V) new ArrayList());
            map.get(key).add(value);
        }else{
            map.get(key).add(value);
        }
    }

    /**
     * Adds the specified list of elements to the {@link key}.
     * @param key The key under which to save the list.
     * @param list The list to add.
     */
    public void addList(K key,V list){
        if(!containsKey(key)){
            list.add(key);
            map.put(key,list);
        }else{
            map.get(key).addAll(list);
        }
    }

    /**
     * Converts this to a SimpleSet. The last entry for a key will be used.
     * @return The converted Set.
     */
    public SimpleSet asSimpleSet(){
        SimpleSet set = new SimpleSet();
        for(int i=0;i<size();i++){
            set.put(getKey(i),get(i).get(0));
        }
        return set;
    }

    /**
     * Glues together all saved fields as a string.
     * @return The complete list.
     */
    public String toString(){
        String s = "";
        for(int i=0;i<size();i++){
            for(int j=0;j<get(i).size();j++){
                s+=getKey(i)+": "+get(i).get(j)+"\n";
            }
        }
        return s;
    }
}
