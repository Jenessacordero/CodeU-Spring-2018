package codeu.model.data;

import java.util.Comparator;

public class DestinationRankComparator implements Comparator<Destination> {

    @Override
    public int compare(Destination destination1, Destination destination2) {
        if (destination1.getVotes() < destination2.getVotes()) {
            return 1;
        }
        if (destination1.getVotes() > destination2.getVotes()) {
            return -1;
        }
        return 0;
    }
}


