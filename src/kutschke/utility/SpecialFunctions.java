package kutschke.utility;

public class SpecialFunctions {

    /**
     * bijective Mapping from Z to N;
     * 
     * @param a
     * @return
     */
    public int integerMap(int a) {
        if (a >= 0) {
            return 2 * a;
        } else
            return -2 * a - 1;
    }

    /**
     * bijective Mapping from N to Z
     * 
     * @param a
     * @return
     */
    public int inverseIntegerMap(int a) {
        if (a % 2 == 0) {
            return a / 2;
        } else
            return -(a + 1) / 2;
    }

    /**
     * bijective Mapping from NxN to N (including the zero)
     * 
     * @param a
     * @param b
     * @return
     */
    public int eulerMap(int a, int b) {
        return (int) (Math.pow(2, a) * (2 * b + 1)) - 1;
    }

    /**
     * bijective Mapping from N to NxN (including the zero)
     * 
     * @param a
     * @return
     */
    public Pair<Integer, Integer> eulerMapInverse(int a) {
        int v = 0;
        if (a == 0)
            a = -1;
        while (a % 2 == 0) {
            v++;
            a /= 2;
        }
        return new Pair<Integer, Integer>(v, (a + 1) / 2);
    }
}
