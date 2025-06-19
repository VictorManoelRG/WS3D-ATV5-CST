/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.representation.idea.Idea;
import java.util.HashMap;
import java.util.Map;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;

/**
 *
 * @author victor
 */
public class GoToDeliverySpot extends Codelet {

    private Creature c;
    private MemoryContainer legsMO;
    private double reachDistance;
    private double creatureBasicSpeed;

    public GoToDeliverySpot(Creature creature, int reachDistance, int creatureBasicSpeed) {
        this.name = "GoToDeliverySpot";
        this.reachDistance = reachDistance;
        this.creatureBasicSpeed = creatureBasicSpeed;
        this.c = creature;
    }

    @Override
    public void accessMemoryObjects() {
        legsMO = (MemoryContainer) this.getOutput("LEGS");
    }

    @Override
    public void calculateActivation() {
    }

    @Override
    public void proc() {
        c.updateState();
        Idea message = Idea.createIdea("message", "", Idea.guessType("Property", null, 1.0, 0.5));

        boolean canComplete = false;
        for (Leaflet l : c.getLeaflets()) {
            if (isLeafletCompleted(l)) {
                canComplete = true;
                System.out.println("PODE COMPLETAR");
                break;
            }
        }

        if (canComplete) {
            message.add(Idea.createIdea("ACTION", "GOTO", Idea.guessType("Property", null, 1.0, 0.5)));
            message.add(Idea.createIdea("X", (int) environment.Environment.deliverySpotObj.getCenterPosition().getX(), Idea.guessType("Property", null, 1.0, 0.5)));
            message.add(Idea.createIdea("Y", (int) environment.Environment.deliverySpotObj.getCenterPosition().getY(), Idea.guessType("Property", null, 1.0, 0.5)));
            message.add(Idea.createIdea("SPEED", creatureBasicSpeed, Idea.guessType("Property", null, 1.0, 0.5)));
            activation = 0.7;
            legsMO.setI(toJson(message), activation, name);
        } else {
            activation = 0;
            legsMO.setI("", activation, name);
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

    String toJson(Idea i) {
        String q = "\"";
        String out = "{";
        String val;
        int ii = 0;
        for (Idea il : i.getL()) {
            if (il.getL().isEmpty()) {
                if (il.isNumber()) {
                    val = il.getValue().toString();
                } else {
                    val = q + il.getValue() + q;
                }
            } else {
                val = toJson(il);
            }
            if (ii == 0) {
                out += q + il.getName() + q + ":" + val;
            } else {
                out += "," + q + il.getName() + q + ":" + val;
            }
            ii++;
        }
        out += "}";
        return out;
    }

}
