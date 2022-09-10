package com.pse.thinder.backend.services.swipestrategy;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import java.util.ArrayList;

/**
 * Interface to define different selection algorithms for the spwipe functionality
 */
public interface ThesisSelectionStrategy {

	/**
	 * Selects a list of theses from the supplied possible thesis
	 * @param theses the possible theses
	 * @return the selected theses
	 */
    public ArrayList<Thesis> getThesesForSwipe(ArrayList<Thesis> theses);
}
