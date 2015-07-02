/*
 * Title: CloudSim Toolkit Description: CloudSim (Cloud Simulation) Toolkit for Modeling and
 * Simulation of Clouds Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Vm represents a VM: it runs inside a Host, sharing hostList with other VMs. It processes
 * cloudlets. This processing happens according to a policy, defined by the CloudletScheduler. Each
 * VM has a owner, which can submit cloudlets to the VM to be executed
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class MyVm extends Vm {

	public MyVm()
	{
		
	}

    private int id;

    //@Override
    protected void setId(int id) {
		this.id = id;
	}
}
