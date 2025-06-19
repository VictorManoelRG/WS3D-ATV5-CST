/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;

/**
 *
 * @author v213307@dac.unicamp.br
 */
public class ClosestJewelDetector extends Codelet {

    private Memory knownMO;
    private Memory closestJewelMO;
    private Memory innerSenseMO;

    private List<Thing> known;

    public ClosestJewelDetector() {
        this.name = "ClosestJewelDetector";
    }

    @Override
    public void accessMemoryObjects() {
        this.knownMO = (MemoryObject) this.getInput("KNOWN_JEWELS");
        this.innerSenseMO = (MemoryObject) this.getInput("INNER");
        this.closestJewelMO = (MemoryObject) this.getOutput("CLOSEST_JEWEL");
    }

    @Override
    public void proc() {
        Thing closestJewel = null;
        double closestDistance = Double.MAX_VALUE;

        known = Collections.synchronizedList((List<Thing>) knownMO.getI());
        Idea cis = (Idea) innerSenseMO.getI();

        double selfX = (double) cis.get("position.x").getValue();
        double selfY = (double) cis.get("position.y").getValue();

        synchronized (known) {
            for (Thing t : known) {
                if (t.getCategory() != Constants.categoryJEWEL) {
                    continue;
                }

                double jewelX = t.getCenterPosition().getX();
                double jewelY = t.getCenterPosition().getY();

                double distance = calculateDistance(jewelX, jewelY, selfX, selfY);

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestJewel = t;
                }
            }
        }

        closestJewelMO.setI(closestJewel);
    }

    @Override
    public void calculateActivation() {

    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

}
