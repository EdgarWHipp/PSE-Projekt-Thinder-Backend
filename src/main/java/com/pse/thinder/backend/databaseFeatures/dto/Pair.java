package com.pse.thinder.backend.databaseFeatures.dto;

/**
 * This custom pair class is used to receive the ratings of theses from the frontend.
 * @param <T> T is of type UUID. The id represents a {@link com.pse.thinder.backend.databaseFeatures.thesis.Thesis}
 * @param <E> E is of type Boolean. Decides whether the rating is positive or negative.
 */
public class Pair<T, E> {
    private T first;
    private E second;


    /**
     *
     * @param first the id of the rated {@link com.pse.thinder.backend.databaseFeatures.thesis.Thesis}
     * @param second boolean value deciding if the rating is positive
     */
    public Pair(T first, E second){
        this.first = first;
        this.second = second;
    }

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    public Pair(){}

    public T getFirst(){
        return this.first;
    }

    public E getSecond(){
        return this.second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(E second) {
        this.second = second;
    }
}
