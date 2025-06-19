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
        Thing closest_jewel = null;
        known = Collections.synchronizedList((List<Thing>) knownMO.getI());
        Idea cis = (Idea) innerSenseMO.getI();

        synchronized (known) {
            if (known.size() != 0) {
                CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                for (Thing t : myknown) {
                    if (t.getCategory() != Constants.categoryJEWEL) {
                        continue;
                    }

                    if (closest_jewel == null) {
                        closest_jewel = t;
                    } else {
                        double Dnew = calculateDistance(t.getX1(), t.getY1(), (double) cis.get("position.x").getValue(), (double) cis.get("position.y").getValue());
                        double Dclosest = calculateDistance(closest_jewel.getX1(), closest_jewel.getY1(), (double) cis.get("position.x").getValue(), (double) cis.get("position.y").getValue());
                        if (Dnew < Dclosest) {
                            closest_jewel = t;
                            System.out.println("joia  mais perto: " +  t.getMaterial().getColorName());
                        }
                    }
                }
            }
        }

        closestJewelMO.setI(closest_jewel);
    }

    @Override
    public void calculateActivation() {

    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

}
