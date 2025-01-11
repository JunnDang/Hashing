package Hashing;
/*
 * @author Chuong Dang
 */
public class HashCodeInteger extends HashCode<Integer> {

    @Override
    public int getHash(Integer key, int size){
        return key % size;
    }
}
