/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codelets.planning;
import br.unicamp.cst.bindings.soar.JSoarCodelet;
import br.unicamp.cst.bindings.soar.SOARPlugin;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.representation.owrl.AbstractObject;
import java.io.File;

/**
 *
 * @author rgudwin
 */
public class TestSoarCodelet extends JSoarCodelet {
    
    AbstractObject il;
    AbstractObject ol;
    SOARPlugin soar;
    Memory mil;
    Memory mol;
    
    public TestSoarCodelet(String agentName, String rulesPath, boolean initdebug) {
        name="soarCodelet";
        this.SilenceLoggers();
        super.initSoarPlugin(agentName, new File (rulesPath), initdebug);
        soar = getJsoar();
    }

    @Override
    public void accessMemoryObjects() {
        mil = this.getInput("inputLink");
        mol = this.getOutput("outputLink");
        il = (AbstractObject) mil.getI();
        ol = (AbstractObject) mol.getI();
    }

    @Override
    public void calculateActivation() {
        
    }

    @Override
    public void proc() {
        setInputLinkAO(il);
        soar.step();
        ol = soar.getOutputLinkAO();
        mol.setI(ol);
    }
    
}
