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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import ws3dproxy.model.Thing;

/**
 *
 * @author victor
 */
public class GetClosestJewel extends Codelet {

    private Memory closestJewelMO;
    private Memory innerSenseMO;
    private Memory knownMO;
    private int reachDistance;
    private Memory handsMO;
    Thing closestJewel;
    Idea cis;
    List<Thing> known;

    public GetClosestJewel(int reachDistance) {
        setTimeStep(50);
        this.reachDistance = reachDistance;
        this.name = "GetClosestJewel";
    }

    @Override
    public void accessMemoryObjects() {
        closestJewelMO = (MemoryObject) this.getInput("CLOSEST_JEWEL");
        innerSenseMO = (MemoryObject) this.getInput("INNER");
        handsMO = (MemoryObject) this.getOutput("HANDS");
        knownMO = (MemoryObject) this.getOutput("KNOWN_JEWELS");
    }

    @Override
    public void calculateActivation() {
    }

    @Override
    public void proc() {
        String jewelName = "";
        closestJewel = (Thing) closestJewelMO.getI();
        cis = (Idea) innerSenseMO.getI();
        known = (List<Thing>) knownMO.getI();

        if (closestJewel != null) {
            double jewelX = 0;
            double jewelY = 0;
            try {
                jewelX = closestJewel.getCenterPosition().getX();
                jewelY = closestJewel.getCenterPosition().getY();
                jewelName = closestJewel.getName();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            double selfX = (double) cis.get("position.x").getValue();
            double selfY = (double) cis.get("position.y").getValue();

            Point2D pJewel = new Point();
            pJewel.setLocation(jewelX, jewelY);

            Point2D pSelf = new Point();
            pSelf.setLocation(selfX, selfY);

            double distance = pSelf.distance(pJewel);
            JSONObject message = new JSONObject();
            try {
                if (distance <= reachDistance) { //eat it						
                    message.put("OBJECT", jewelName);
                    message.put("ACTION", "GETJEWEL");
                    System.out.println("PEGAR JOIA");
                    handsMO.setI(message.toString());
                    activation = 1.0;
                    DestroyClosestJewel();
                } else {
                    activation = 0.0;
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            activation = 0.0;
        }
    }

    public void DestroyClosestJewel() {
        int r = -1;
        int i = 0;
        synchronized (known) {
            CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
            for (Thing t : known) {
                if (closestJewel != null) {
                    if (t.getName().equals(closestJewel.getName())) {
                        r = i;
                    }
                }
                i++;
            }
            if (r != -1) {
                known.remove(r);
            }
            closestJewel = null;
        }
    }

}
