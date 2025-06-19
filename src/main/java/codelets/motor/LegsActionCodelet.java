package codelets.motor;

import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import java.util.logging.Logger;
import org.json.JSONException;
import ws3dproxy.model.Creature;

/**
 * Legs Action Codelet - executa comandos de movimentação recebidos da memória.
 * Essa versão envia comandos repetidos SEM checar se são iguais ao anterior.
 */
public class LegsActionCodelet extends Codelet {

    private Memory legsActionMO;
    private Creature c;
    static Logger log = Logger.getLogger(LegsActionCodelet.class.getCanonicalName());

    public LegsActionCodelet(Creature nc) {
        this.c = nc;
        this.name = "LegsActionCodelet";
    }

    @Override
    public void accessMemoryObjects() {
        legsActionMO = (MemoryContainer) this.getInput("LEGS");
    }

    @Override
    public void proc() {
        String comm = (String) legsActionMO.getI();

        if (comm == null || comm.isEmpty()) {
            return; // Nada para fazer
        }

        try {
            JSONObject command = new JSONObject(comm);

            if (command.has("ACTION")) {
                String action = command.getString("ACTION");

                if (action.equals("FORAGE")) {
                    log.info("Sending Forage command to agent");
                    try {
                        c.rotate(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (action.equals("GOTO")) {
                    double speed = command.getDouble("SPEED");
                    double targetX = command.getDouble("X");
                    double targetY = command.getDouble("Y");

                    log.info("Sending GOTO command to agent: [" + targetX + "," + targetY + "] speed=" + speed);
                    try {
                        c.moveto(speed, targetX, targetY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (action.equals("STOP")) {
                    log.info("Sending STOP command to agent");
                    try {
                        c.moveto(0, c.getPosition().getX(), c.getPosition().getY());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calculateActivation() {
        // Não utilizado
    }
}
