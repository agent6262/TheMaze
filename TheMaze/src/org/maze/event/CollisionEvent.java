/**
 * 
 */
package org.maze.event;

import org.ajgl.event.Event;
import org.ajgl.primary.GameObject;


/**
 * @author Tyler
 *
 */
public class CollisionEvent extends Event {
    
    private boolean canceled;   // States weather the event has been canceled
    private GameObject collider;
    private GameObject collidie;
    
    public CollisionEvent(GameObject collider, GameObject collidie) {
        this.collider = collider;
        this.collidie = collidie;
    }
    
    /**
     * @return the collider
     */
    public GameObject getCollider() {
        return collider;
    }
    
    /**
     * @return the collidie
     */
    public GameObject getCollidie() {
        return collidie;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }
    
    @Override
    public String getName() {
        return "CollisionEvent";
    }
    
}
