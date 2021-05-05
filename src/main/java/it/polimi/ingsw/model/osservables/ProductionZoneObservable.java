package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.board.ProductionZone;
import it.polimi.ingsw.model.board.Stock;
import it.polimi.ingsw.model.listeners.ProductionZoneListener;
import it.polimi.ingsw.model.listeners.StockListener;

import java.util.ArrayList;

public abstract class ProductionZoneObservable {

    private ArrayList<ProductionZoneListener> productionZoneListeners = new ArrayList<>();

    public void notifyProductionZoneListener(ProductionZone productionZone) {
        for (ProductionZoneListener productionZoneListener : productionZoneListeners) {
            productionZoneListener.update(productionZone);
        }
    }

    public void addProductionZoneListener(ProductionZoneListener productionZoneListener){
        productionZoneListeners.add(productionZoneListener);
    }

    public void removeProductionZoneListener(ProductionZoneListener productionZoneListener) {
        productionZoneListeners.remove(productionZoneListener);
    }

    public void removeAll() {
        productionZoneListeners = new ArrayList<>();
    }

}