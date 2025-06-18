/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;

/**
 *
 * @author v213307@dac.unicamp.br
 */
public class JewelDetector extends Codelet {

    private Memory visionMO;
    private Memory knownJewelsMO;
    private Creature c;

    public JewelDetector(Creature creature) {
        this.name = "JewelDetector";
        this.c = creature;
    }

    @Override
    public void accessMemoryObjects() {
        synchronized (this) {
            this.visionMO = (MemoryObject) this.getInput("VISION");
        }
        this.knownJewelsMO = (MemoryObject) this.getOutput("KNOWN_JEWELS");
    }
    
    @Override
	public void proc() {
            CopyOnWriteArrayList<Thing> vision;
            List<Thing> known;
            synchronized (visionMO) {
               //vision = Collections.synchronizedList((List<Thing>) visionMO.getI());
               vision = new CopyOnWriteArrayList((List<Thing>) visionMO.getI());    
               known = Collections.synchronizedList((List<Thing>) knownJewelsMO.getI());
               //known = new CopyOnWriteArrayList((List<Thing>) knownApplesMO.getI());    
               synchronized(vision) {
                 for (Thing t : vision) {
                    boolean found = false;
                    synchronized(known) {
                       CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                       for (Thing e : myknown)
                          if (t.getName().equals(e.getName())) {
                            found = true;
                            break;
                          }
                       if (!found && isJewelFromLeaflet(t)) {
                           known.add(t);
                           System.out.println("achou joia de leaflet");
                       }
                    }
               
                 }
               }
            }
	}// end proc
        
        private boolean isJewelFromLeaflet(Thing t){
            for(Leaflet leaflet : c.getLeaflets()){
                if(leaflet.ifInLeaflet(t.getMaterial().getColorName())){
                    return true;
                }
            }
            
            return false;
        }
        
        @Override
        public void calculateActivation() {
        
        }

}
