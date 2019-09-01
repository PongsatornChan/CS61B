

/** This class contains methods that are solution to hw0 */

public class Solutions {

  public static int max(int[] numlst) {
    int highest = numlst[0];
    for(int i = 1; i < numlst.length; i++) {
      if(highest < numlst[i]) {
        highest = numlst[i];
      }
    }
    return highest;
  }
  public static boolean threeSum(int[] numlst) {
    return true;
  }

  public static boolean threeSumDistinct(int[] numlst) {
    return true;
  }

  public static void main(String[] args) {
    int[] numlst1 = {1, 2, 4, 25, -1, -2};
    System.out.println(max(numlst1));
  }

}
