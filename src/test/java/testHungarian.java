import cross.language.algorithm.HungarianAlgorithmKt;
import kotlin.Pair;

import java.util.Arrays;
import java.util.List;

import static cross.language.algorithm.HungarianAlgorithmKt.hungarianAlgorithm;

public class testHungarian {

    public static void main(String[] args) {
//        DemoKt.run();
        int n = 3;
        List<Integer> list = Arrays.asList(1500, 4000, 4500, 2000, 6000, 3500, 2000, 4000, 2500);
    /*1500 4000 4500
	  2000 6000 3500
	  2000 4000 2500*/
        Pair ob = hungarianAlgorithm(list);
        System.out.println(ob.component1());
        System.out.println(ob.component2());
    }
}
