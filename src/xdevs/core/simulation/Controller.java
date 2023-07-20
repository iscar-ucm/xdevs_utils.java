/*
 * Copyright (C) 2014-2015 José Luis Risco Martín <jlrisco@ucm.es> and 
 * Saurabh Mittal <smittal@duniptech.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * http://www.gnu.org/licenses/
 *
 * Contributors:
 *  - José Luis Risco Martín <jlrisco@ucm.es>
 *  - Saurabh Mittal <smittal@duniptech.com>
 */
package xdevs.core.simulation;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;

/**
 *
 * @author José Luis Risco Martín
 */
public class Controller extends Thread {

    protected Coordinator coordinator;
    private boolean suspended = false;
    protected double tF = Constants.INFINITY;

    public Controller(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void startSimulation(double timeInterval) {
        coordinator.initialize();
        coordinator.getClock().setTime(coordinator.getTN());
        tF = coordinator.getClock().getTime() + timeInterval;
    }

    public void runSimulation() {
        super.start();
    }

    public void pauseSimulation() {
        suspended = true;
    }

    public void resumeSimulation() {
        suspended = false;
        synchronized (this) {
            notify();
        }
    }

    public void stepSimulation() {
        if (coordinator.getClock().getTime() < tF) {
            coordinator.lambda();
            coordinator.deltfcn();
            coordinator.clear();
            coordinator.getClock().setTime(coordinator.getTN());
        }
    }

    public void terminateSimulation() {
        super.interrupt();
    }

    public void simInject(double e, Port<Object> port, Collection<Object> values) {
        coordinator.simInject(e, port, values);
    }

    public void simInject(Port<Object> port, Collection<Object> values) {
        coordinator.simInject(port, values);
    }

    public void simInject(double e, Port<Object> port, Object value) {
        coordinator.simInject(e, port, value);
    }

    public void simInject(Port<Object> port, Object value) {
        coordinator.simInject(port, value);
    }

    public SimulationClock getSimulationClock() {
        return coordinator.getClock();
    }

    public void run() {
        try {
            while (!super.isInterrupted() && coordinator.getClock().getTime() < Constants.INFINITY && coordinator.getClock().getTime() < tF) {
                synchronized (this) {
                    while (suspended) {
                        wait();
                    }
                }
                coordinator.lambda();
                coordinator.deltfcn();
                coordinator.clear();
                coordinator.getClock().setTime(coordinator.getTN());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
