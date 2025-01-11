package Hashing;
import java.util.*;
/*
 * @aurthor Chuong Dang
 * This the hash table class.
 * Its job is to store the items with the appropriate hash codes.
 * It is also being able to retreate the value with the input key.
 * The list keeps track of its size and resize when the list has too many items.
 * 
 */
public class ChainHashing<Key extends Comparable<Key>, Value> {
    private ArrayList<LinkedList<HashItem<Key, Value>>> map;
    private int[] mapSize = {11, 47, 97, 197, 379, 691, 1259, 2557, 5051, 7919, 14149, 28607, 52817, 102149, 209939, 461017, 855683, 1299827};
    private int currentMapSize;
    private int numberOfItem;
    private double  loadFactor;
	HashCode<Key> hashCode;

	/**
	 * @param hashCode takes in a variable that will be called to generate the hash code.
	 * This method is the instructor.
	 */
    ChainHashing(HashCode<Key> hashCode){
		this.hashCode = hashCode;
        currentMapSize = 0;
        map = new ArrayList<>();
        numberOfItem = 0;
        for(int i = 0; i <mapSize[currentMapSize]; i++){
            map.add(new LinkedList<>());
        }
		loadFactor = 0.75 *(double)mapSize[currentMapSize];
    }

	/**
	 * 
	 * @param currentSize allow to specify the size of the table
	 * @param hashCode takes in a variable that will be called to generate the hash code.
	 */

	ChainHashing(int currentSize,HashCode<Key> hashCode){
		this.hashCode = hashCode;
        currentMapSize = currentSize;
        map = new ArrayList<>();
        numberOfItem = 0;
        for(int i = 0; i < mapSize[currentMapSize]; i++){
            map.add(new LinkedList<>());
        }
		loadFactor = 0.75 *(double)mapSize[currentMapSize];
	}

	/**
	 * 
	 * @return the hash table.
	 */
    protected ArrayList<LinkedList<HashItem<Key, Value>>> getMap(){
        return map;
    }

	/**
	 * 
	 * @return return true if the number of item in the table is greate enough to rehash.
	 */
    private boolean needsResize(){
		return numberOfItem > loadFactor;
	}	

	/*
	 * check if the method is needed to be rehash.
	 */
    private void resizeCheck() {		

		if (needsResize())
		{
			currentMapSize++;
			loadFactor = 0.75 *(double)mapSize[currentMapSize];
			ChainHashing<Key, Value> newMap = new ChainHashing<>(currentMapSize,hashCode);
			
			for (LinkedList<HashItem<Key, Value>> chain: map){

				for (HashItem<Key, Value> item: chain){
                    newMap.add(item.key, item.value);
				}
			}
			
			map = newMap.getMap();
		}
	}

	/**
	 * 
	 * @param key the key to be searched.
	 * @param list list to search for key
	 * @return return the index of key with list if found, return -1 otherwise.
	 */
    private int getIndexOfItem(Key key, LinkedList<HashItem<Key,Value>> list){
		for(int i = 0; i < list.size(); i++){
			if(key.compareTo(list.get(i).key) ==0){
				return i;
			}
		}

		return -1;

	}

	/**
	 * 
	 * @param ID the key to get its hash code
	 * @return the hash ode of the ID
	 */

    private int getHash(Key ID){
        return hashCode.getHash(ID, mapSize[currentMapSize]);
    }

	/**
	 * 
	 * @param item the HashItem to be added to the table
	 */

	public void add(HashItem<Key, Value> item){
		int index = getHash(item.key);
		LinkedList<HashItem<Key,Value>> list =map.get(index);
		index = getIndexOfItem(item.key,list);
		if(index == -1){
			list.add(item);
			numberOfItem++;
			resizeCheck();
		} else {
			list.get(index).value = item.value ;
		}
	}

	/**
	 * 
	 * @param key the key of a HashItem
	 * @param value the value to be stored with key
	 * This method adds new item.
	 */

    public void add(Key key, Value value) {	

		HashItem<Key,Value> item = new HashItem<Key,Value>(key,value);
		int index = getHash(key);
		LinkedList<HashItem<Key,Value>> list =map.get(index);
		index = getIndexOfItem(key,list);
		if(index == -1){
			list.add(item);
			numberOfItem++;
			resizeCheck();
		} else {
			list.get(index).value = value ;
		}
			
	}

	/**
	 * 
	 * @param key of an HashItem to be removed from table.
	 * This method removes a HashItem.
	 */

    public void remove(Key key) {
		for(LinkedList<HashItem<Key,Value>> list : map){
			for(int i = 0; i < list.size();i++){
				if(key.compareTo(list.get(i).key)==0){
					list.remove(i);
					numberOfItem--;
					return;
				}
			}
		}
	}

	/**
	 * This method prints the content of the hash table.
	 */
    public void print(){
		for(int i =0; i < map.size();i++){
			System.out.print(i+": ");
			LinkedList<HashItem<Key, Value>> list = map.get(i);
			for(HashItem<Key, Value> item : list){
				System.out.print(item.key +":"+item.value+"		");
			}
			System.out.println();
		}
    }

	/**
	 * 
	 * @param key is the key to be check if exists with the table.
	 * @return true if key is found.
	 */
	public boolean contain(Key key) {
		//To Be Implemented
		int index = getHash(key);
		LinkedList<HashItem<Key,Value>> list = map.get(index);
		for(HashItem<Key,Value> item : list){
			if(key.compareTo(item.key)==0){
				return true;
			}
		}
		//placeholder code to prevent errors
    	return false;
	}

	/**
	 * 
	 * @param key is the key to be checked.
	 * @return if key is found, the its value will be returned.
	 */

    public Value getValue(Key key) {
		if(getItem(key)!= null){
			return getItem(key).value;
		}
		return null;
	}

	/**
	 * 
	 * @param key is the key to be checked.
	 * @return if key is found, the its HashItem will be returned.
	 */
	public HashItem<Key,Value> getItem(Key key){
		int index = getHash(key);
		LinkedList<HashItem<Key,Value>> list = map.get(index);
		
		for(HashItem<Key,Value> item : list){
			if(key.compareTo(item.key)==0){
				return item;
			}
		}
    	return null;
	}

	/**
	 * 
	 * @return the number of item within table
	 * 
	 */
	public int getNumberOfItem(){
		return numberOfItem;
	}
}
