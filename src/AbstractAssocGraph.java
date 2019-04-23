import java.util.*;

/**
 * Abstract class for Association graph that implements some of the common functionality.
 * <p>
 * Note, you should not need to modify this but can if need to.  Just make sure to test to make sure everything works.
 *
 * @author Jeffrey Chan, 2019.
 * updated by Zéro & Anna 31 March 2019
 */
public abstract class AbstractAssocGraph implements AssociationGraph {

    protected static final int EDGE_NOT_EXIST = -1;

    /**
     * resize the k nearest neighbours result list under constraint
     *
     * @param k is the expected number of the result list
     * @param neighbours all the neighbours of the node
     * @return the resized k nearest neighbours
     */
    protected ArrayList<MyPair> resizeList(int k, ArrayList<MyPair> neighbours) {
        if (neighbours.size() == 0) {
            return new ArrayList<>();
        }
        if (k >= neighbours.size()) {
            return neighbours;
        } else {
            int end = neighbours.size() - k;
            for (int i = 0; i < end; i++) {
                int tempLowestWeightIndex = 0;
                for (int j = 0; j < neighbours.size(); j++) {
                    if (neighbours.get(j).getValue() < neighbours.get(tempLowestWeightIndex).getValue()) {
                        tempLowestWeightIndex = j;
                    }
                }
                neighbours.remove(tempLowestWeightIndex);
            }
            return neighbours;
        }
    }

} // end of abstract graph AbstractAssocGraph
