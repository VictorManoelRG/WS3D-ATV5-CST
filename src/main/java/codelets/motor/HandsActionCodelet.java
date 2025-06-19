package codelets.motor;

import org.json.JSONException;
import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;

/**
 * Hands Action Codelet - executa comandos recebidos da memória.
 * Essa versão executa comandos repetidos SEM checar se são iguais ao anterior.
 */
public class HandsActionCodelet extends Codelet {

    private Memory handsMO;
    private Creature c;
    static Logger log = Logger.getLogger(HandsActionCodelet.class.getCanonicalName());

    public HandsActionCodelet(Creature nc) {
        this.c = nc;
        this.name = "HandsActionCodelet";
    }

    @Override
    public void accessMemoryObjects() {
        handsMO = (MemoryObject) this.getInput("HANDS");
    }

    @Override
    public void proc() {

        String command = (String) handsMO.getI();
        System.out.println("comando: " + command +" .\n");

        if (command == null || command.isEmpty()) {
            return; // Nada para fazer
        }

        try {
            JSONObject jsonAction = new JSONObject(command);

            if (jsonAction.has("ACTION") && jsonAction.has("OBJECT")) {
                String action = jsonAction.getString("ACTION");
                String objectName = jsonAction.getString("OBJECT");

                switch (action) {
                    case "EATIT":
                        try {
                            c.eatIt(objectName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        log.info("Sending Eat command to agent: " + objectName);
                        break;

                    case "DELIVERLEAFLET":
                        try {
                            for (Leaflet l : c.getLeaflets()) {
                                if (isLeafletCompleted(l)) {
                                    c.deliverLeaflet(l.getID().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        log.info("Sending Deliver Leaflet command to agent.");
                        break;

                    case "GETJEWEL":
                        try {
                            c.putInSack(objectName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        log.info("Sending Get Jewel command to agent: " + objectName);
                        break;

                    case "BURY":
                        try {
                            c.hideIt(objectName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        log.info("Sending Bury command to agent: " + objectName);
                        break;
                }
            }
        handsMO.setI("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calculateActivation() {
        // Não utilizado
    }

    private boolean isLeafletCompleted(Leaflet l) {
        HashMap<String, Integer[]> items = l.getItems();
        for (Map.Entry<String, Integer[]> i : items.entrySet()) {
            Integer[] array = i.getValue();
            if (array[0] > array[1]) {
                return false;
            }
        }
        return true;
    }
}
