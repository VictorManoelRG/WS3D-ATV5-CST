/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

/**
 *
 * @author victor
 */
public class DeliverLeaflet extends Codelet {

    private int reachDistance;
    private Memory innerSenseMO;
    private Memory handsMO;
    private Thing deliverySpot = environment.Environment.deliverySpotObj;
    private Creature c;
    Idea cis;

    public DeliverLeaflet(Creature creature, int reachDistance) {
        this.reachDistance = reachDistance;
        this.name = "DeliverLeaflet";
        c = creature;
    }

    @Override
    public void accessMemoryObjects() {
        innerSenseMO = (MemoryObject) this.getInput("INNER");
        handsMO = (MemoryObject) this.getOutput("HANDS");

    }

    @Override
    public void calculateActivation() {
    }

    @Override
    public void proc() {
        c.updateState();
        boolean isCompleted = false;
        for (Leaflet l : c.getLeaflets()) {
            if (isLeafletCompleted(l)) {
                isCompleted = true;
            }
        }

        if (!isCompleted) {
            activation = 0;
            return;
        }

        cis = (Idea) innerSenseMO.getI();

        double selfX = (double) cis.get("position.x").getValue();
        double selfY = (double) cis.get("position.y").getValue();

        double deliverX = deliverySpot.getCenterPosition().getX();
        double deliverY = deliverySpot.getCenterPosition().getY();

        Point2D pDelivery = new Point();
        pDelivery.setLocation(deliverX, deliverY);

        Point2D pSelf = new Point();
        pSelf.setLocation(selfX, selfY);

        double distance = pSelf.distance(pDelivery);
        JSONObject message = new JSONObject();

        try {
            if (distance <= reachDistance) {
                message.put("OBJECT", deliverySpot.getName());
                message.put("ACTION", "DELIVERLEAFLET");
                handsMO.setI(message.toString());
                activation = 1.0;
            } else {
                handsMO.setI("");
                activation = 0.0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isLeafletCompleted(Leaflet l) {
        boolean completed = true;
        HashMap<String, Integer[]> items = l.getItems();
        for (Map.Entry<String, Integer[]> i : items.entrySet()) {
            Integer[] array = i.getValue();
            int required = array[0];
            int collected = array[1];

            if (required > collected) {
                completed = false;
                break;
            }

        }
        return completed;
    }

}
