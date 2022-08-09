package com.pse.thinder.backend.services.swipestrategy;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ThesisSelectionStrategy {



    public ArrayList<Thesis> getThesesForSwipe(ArrayList<Thesis> theses);
}
