package com.pse.thinder.backend.services.swipestrategy;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class implements the {@link ThesisSelectionStrategy} with random selection
 *
 */
@Service
public class ThesisSelectRandom implements ThesisSelectionStrategy {

    @Override
    public ArrayList<Thesis> getThesesForSwipe(ArrayList<Thesis> theses) {
        int size = Math.min(theses.size(), 10);
        Collections.shuffle(theses);
        return new ArrayList<>(theses.subList(0, size));
    }
}
