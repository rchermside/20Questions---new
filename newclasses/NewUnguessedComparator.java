package newclasses;

import java.util.*;

public class NewUnguessedComparator implements Comparator<NewQuestion> {
    @Override
    public int compare(NewQuestion a, NewQuestion b) {
        return a.numGuessesKnown() < b.numGuessesKnown() ? -1 : a.numGuessesKnown() == b.numGuessesKnown() ? 0 : 1;
    }
}
