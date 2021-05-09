package it.polimi.ingsw.serializableModel;

import it.polimi.ingsw.model.game.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableStock implements Serializable {

    private ArrayList<Resource[]> boxes;//standard boxes
    private ArrayList<Resource[]> boxPlus;//added boxes due to leader card ability
    private ArrayList<Resource> resourcesPlus;//this arrayList contains the allowed resources in boxPlus, at the same position

    public SerializableStock(ArrayList<Resource[]> boxes, ArrayList<Resource[]> boxPlus, ArrayList<Resource> resourcesPlus) {
        this.boxes = boxes;
        this.boxPlus = boxPlus;
        this.resourcesPlus = resourcesPlus;
    }

    public ArrayList<Resource[]> getBoxes() {
        return boxes;
    }

    public void setBoxes(ArrayList<Resource[]> boxes) {
        this.boxes = boxes;
    }

    public ArrayList<Resource[]> getBoxPlus() {
        return boxPlus;
    }

    public void setBoxPlus(ArrayList<Resource[]> boxPlus) {
        this.boxPlus = boxPlus;
    }

    public ArrayList<Resource> getResourcesPlus() {
        return resourcesPlus;
    }

    public void setResourcesPlus(ArrayList<Resource> resourcesPlus) {
        this.resourcesPlus = resourcesPlus;
    }
}
