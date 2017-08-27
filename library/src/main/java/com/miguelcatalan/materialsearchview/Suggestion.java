package com.miguelcatalan.materialsearchview;

/**
 * Created by yusuphwickama on 8/23/17.
 */

public class Suggestion {
    private String id, name, location;

    public Suggestion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Suggestion)) {
            return false;
        }

        Suggestion that = (Suggestion) other;

        // Custom equality check here.
        return this.getName().equals(that.getName())
                && this.getId().equals(that.getId())
                && this.getLocation().equals(that.getLocation());
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 37 + this.getId().hashCode();
        hashCode = hashCode * 37 + this.getName().hashCode();
        hashCode = hashCode * 37 + this.getLocation().hashCode();

        return hashCode;
    }
}
