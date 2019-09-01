

/** This class contains methods that are solution to hw0 */

public class Solutions {

  public static int max(int[] numlst) {
    int highest = numlst[0];
    for(i = 1; i < numlst.length; i++) {
      if(highest < numlst[i]) {
        highest = numlst[i];
      }
    }
    return highest;
  }
}
