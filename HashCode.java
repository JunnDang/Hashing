package Hashing;
/**
 * @author Chuong Dang
 * This is the HashCode class, it will retrun the hash code if the input item.
 */
abstract class HashCode<Type> {
        /**
         * This an abstract class.
         * @param item is the key.
         * @param mod_by is the table size.
         * @return the hash code of the key.
         */
        public abstract int getHash(Type item, int mod_by);
}
