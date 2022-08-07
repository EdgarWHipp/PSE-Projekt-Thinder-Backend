package com.pse.thinder.backend.services.swipestrategy;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ThesisSelectRandom implements ThesisSelectionStrategy {




    @Override
    public ArrayList<Thesis> getThesesForSwipe(ArrayList<Thesis> theses) {
        ArrayList<Thesis> swipeStack = new ArrayList<>();
        int size = theses.size() < 10 ? theses.size() : 10;
        Collections.shuffle(theses);
        for (int i = 0; i < size; i++) {
            swipeStack.add(theses.get(i));
        }
        return swipeStack;
    }
}
