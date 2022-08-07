package com.pse.thinder.backend.services.swipestrategy;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;
import com.pse.thinder.backend.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ThesisSelectRandom implements ThesisSelectionStrategy {

    @Autowired
    StudentRepository studentRepository;

    List<Integer> testList = Arrays.asList(2,35,6,1,3,7,8,9,34,56,67);

    @Override
    public ArrayList<Thesis> getThesesForSwipe(ArrayList<Thesis> theses) {
        List<Integer> list = testList;
        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(testList);
            newList.add(testList.remove(0));
        }
        return null;
    }
}
