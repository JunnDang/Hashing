package Hashing;
/**
 * @author Chuong Dang
 * Key is the key of the HashItem
 * Value is the value being paired with the key
 */
public class HashItem<Key, Value> {
    Key key;
    Value value;


    
    /**
     * 
     * @param key of the HashItem
     * @param value the value being paired with the key
     */
    public HashItem(Key key, Value value){
        this.key = key;
        this.value = value;
    }
}
