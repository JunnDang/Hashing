package Hashing;
/**
 * @author Chuong Dang
 */
public class HashCodeString extends HashCode<String> {
    
    @Override
    public int getHash(String key, int tableSize){
        int hash = 0;
        for (char ch: key.toCharArray())
        {
            hash += ch;
        }
        return hash % tableSize;
    }
}