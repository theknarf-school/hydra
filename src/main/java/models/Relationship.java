package models;

import helpers.ConsumerHelper;

import javax.persistence.*;

/**
 * Represents a path an {@link models.Entity entity} can take from a {@link models.Node node} to another node.
 * Has a unique ID, a {@link Consumer consumer} that the {@link models.Node parent node} can send entites to and a
 * value representing the weight of the relationship.
 */
//TODO: Add a check when using dependencies so that it doesn't crash if you make something dependent to itself?
@javax.persistence.Entity
public class Relationship implements Comparable<Relationship>{

    //region attributes

    /**
     * An automatically generated id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Consumer child;

    private double weight;

    //endregion

    //region constructors

    public Relationship(Consumer child, double weight) {
        this.child = child;
        this.weight = weight;
    }

    public Relationship() {

        this(new Consumer(), 0.0);
    }

    //endregion

    //region getters and setters

    public Consumer getChild() {
        return child;
    }

    public void setChild(Consumer child) {
        this.child = child;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Relationship o) {

        if(o == this) return 0;

        ConsumerHelper controller = new ConsumerHelper();
        if(controller.getTotalSentToConsumer(o.getChild()) > controller.getTotalSentToConsumer(this.getChild())) return 1;
        if(o.getWeight() > this.getWeight()) return 1;
        return -1;
    }

    //endregion


}
