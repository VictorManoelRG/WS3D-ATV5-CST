package environment;


/** ***************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 **************************************************************************** */

import support.ResourcesGenerator;
import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;
import ws3dproxy.model.World;
import ws3dproxy.util.Constants;
import ws3dproxy.util.Logger;

/**
 *
 * @author rgudwin
 */
public final class Environment {

    public String host = "localhost";
    public int port = 4011;
    public String robotID = "r0";
    public Creature c = null;
    public static Thing deliverySpotObj;

    public Environment() {
        WS3DProxy proxy = new WS3DProxy();
        try {
            World w = World.getInstance();
            w.reset();
            World.createFood(0, 350, 75);
            World.createFood(0, 100, 220);
            World.createFood(0, 250, 210);
            World.createDeliverySpot(300, 300);
            getDeliverySpotObj();
            c = proxy.createCreature(100, 450, 0, 0);
            c.start();
            grow(w, 7);
        } catch (CommandExecException e) {

        }
        System.out.println("Robot " + c.getName() + " is ready to go.");
    }

    public synchronized void grow(World w, int time) {
        try {
            if (time <= 0) {
                time = Constants.TIMEFRAME;
            }
            w.getDimensionAndDeliverySpot();
            ResourcesGenerator rg = new ResourcesGenerator(time, w.getEnvironmentWidth(), w.getEnvironmentHeight(), w.getDeliverySpot().getX(), w.getDeliverySpot().getY());
            rg.start();
        } catch (CommandExecException ex) {
            Logger.logException(World.class.getName(), ex);
        }
    }

    private void getDeliverySpotObj() {
        try {
            for (Thing item : World.getWorldEntities()) {
                if (item.getCategory() == Constants.categoryDeliverySPOT) {
                    deliverySpotObj = item;
                    break;
                }
            }
        } catch (CommandExecException ex) {
            System.getLogger(Environment.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
