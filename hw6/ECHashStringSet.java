import java.util.List;
import java.util.ArrayList;

/** A set of String values.
 *  @author Pongsatorn Chanpanichravee
 */
class ECHashStringSet implements StringSet {

    private ArrayList<ArrayList<String>> buckets = new ArrayList<ArrayList<String>>();

    private int numItem;

    public ECHashStringSet() {
        numItem = 0;
        for (int i = 0; i < 5; i++) {
            buckets.add(new ArrayList<String>());
        }
    }

    private int getKey(String s) {
        int key = s.hashCode();
        if (key < 0) {
            return (key & 0x7fffffff) % buckets.size();
        } else {
            return key % buckets.size();
        }
    }

    @Override
    public void put(String s) {
        int key = getKey(s);
        buckets.get(key).add(s);
        numItem++;
        if (numItem / buckets.size() >= 5) {
            reHash();
        }
    }

    private void reHash() {
        int[] oldBucketSize = new int[buckets.size()];
        for (int i = 0; i < buckets.size(); i++) {
            oldBucketSize[i] = buckets.get(i).size();
        }
        int oldSize = buckets.size();
        for (int i = 0; i < oldSize * 2; i++) {
            buckets.add(new ArrayList<String>());
        }
        for (int i = 0; i < oldSize; i++) {
            for (int j = 0; j < oldBucketSize[i]; j++) {
                String str = buckets.get(i).remove(0);
                put(str);
                numItem--;
            }
        }
    }

    @Override
    public boolean contains(String s) {
        int key = getKey(s);
        return buckets.get(key).contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < buckets.size(); i++) {
            result.addAll(buckets.get(i));
        }
        return result;
    }
}
