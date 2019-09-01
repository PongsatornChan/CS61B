

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
    for(int i = 0; i < numlst.length; i++) {
      for(int j = 0; j < numlst.length; j++) {
        for(int m = 0; m < numlst.length; m++) {
          if(numlst[i]+numlst[j]+numlst[m] == 0) {
            return true;
          }
        }
      }
    }
    return false;
  } 

  public static boolean threeSumDistinct(int[] numlst) {
    for(int i = 0; i < numlst.length; i++) {
      for(int j = 0; j < numlst.length; j++) {
        for(int m = 0; m < numlst.length; m++) {
          if(i!=j && i!=m && j!=m) {
            if(numlst[i]+numlst[j]+numlst[m] == 0) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public static void main(String[] args) {
    int[] numlst1 = {1, 2, 4, 25, -1, -2};
    System.out.println(max(numlst1));
    System.out.println(threeSum(numlst1));
    System.out.println(threeSum(new int[] {9, 2 , -7}));
    System.out.println(threeSumDistinct(new int[] {6, -3, -2}));
  }

}
