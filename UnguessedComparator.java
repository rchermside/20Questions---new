import java.util.*;

public class UnguessedComparator implements Comparator<Question> {
    @Override
    public int compare(Question a, Question b) {
        return a.numGuessesKnown() < b.numGuessesKnown() ? -1 : a.numGuessesKnown() == b.numGuessesKnown() ? 0 : 1;
    }
}
