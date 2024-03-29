package it.polimi.ingsw.model.osservables;

import it.polimi.ingsw.model.listeners.PopeTrackListener;
import it.polimi.ingsw.model.popeTrack.PopeTrack;

import java.util.ArrayList;

/**
 * class that implements the observable/listener patter for the pope track
 */

public abstract class PopeTrackObservable {

    private ArrayList<PopeTrackListener> popeTrackListeners = new ArrayList<>();

    public void notifyPopeTrackListener(PopeTrack popeTrack) {
        for (PopeTrackListener popeTrackListener : popeTrackListeners) {
            popeTrackListener.update(popeTrack);
        }
    }

    public void addPopeTrackListener(PopeTrackListener popeTrackListener){
        popeTrackListeners.add(popeTrackListener);
    }

}
