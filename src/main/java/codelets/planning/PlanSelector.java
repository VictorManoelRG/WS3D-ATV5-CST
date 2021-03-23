/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codelets.planning;

import br.unicamp.cst.bindings.soar.Plan;
import br.unicamp.cst.bindings.soar.PlanSelectionCodelet;
import java.util.HashMap;

/**
 *
 * @author rgudwin
 */
public class PlanSelector extends PlanSelectionCodelet {
    
    public PlanSelector(String s) {
        super(s);
    }

    @Override
    public Plan selectPlanToExecute(HashMap<Integer, Plan> hm) {
        return(new Plan(""));
    }

    @Override
    public boolean verifyIfPlanWasFinished(Plan plan) {
        return(true);
    }

    @Override
    public boolean verifyExistPlan(Plan plan) {
        return(true);
    }
    
}
