/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package test;
package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.util.ExecutionTimeMeasurer;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;


/**
 *
 * @author Novin Pendar
 */
public class MyDB extends DatacenterBroker {

    public MyDB(String name) throws Exception {
        super(name);
    }
    @Override
    	protected void submitCloudlets() {
    	        Log.printLine("******* This is my datacenter broker....");
    	        //Log.printLine("LOLOLOLOLOLOLOLOLOLOL");
		int vmIndex = 0;
		for (Cloudlet cloudlet : getCloudletList()) {
			Vm vm;
			vm = getVmsCreatedList().get(vmIndex);

			//the finish or completion time of this Cloudlet or -1 if not finished yet. Gia to getFinishTime()
			//double exectime = cloudlet.getFinishTime() - cloudlet.getExecStartTime(); //
			//Log.printLine(CloudSim.clock() +"test: "+ exectime + " : " +cloudlet.getActualCPUTime());
			for (int i = 0 ; i <= 1; i++)
			{
				Log.printLine(" i =  "+i);
				//EDW PROVLIMA
				vm.setId(i);
				Log.printLine("Cloudlet ID = " + cloudlet.getCloudletId() );

				
					//Log.printLine(">>>>> THE VM ID IS : " + vm.getId());
					double t3 = cloudlet.getCloudletLength() / vm.getMips();
					//if (t3 < 1000)
					//{
						//cloudlet.setVmId();
					//}
				
					Log.printLine("***Test exectime****** " + t3 + " in VM# " + vm.getId() +" The cloudlet "+cloudlet.getCloudletId() +
						" : "+ cloudlet.getCloudletLength());
				
			}
			
			/*if (cloudlet.getCloudletId() == 0)
			{
				double t3 = cloudlet.getCloudletLength() / vm.getMips();
				Log.printLine("***Test exectime****** " + t3 + " in VM# " + vm.getId() +" The cloudlet "+cloudlet.getCloudletId() +
					" : "+ cloudlet.getCloudletLength());
			}
			*/
			// if user didn't bind this cloudlet and it has not been executed yet
			if (cloudlet.getVmId() == -1) {
				vm = getVmsCreatedList().get(vmIndex);
			} else { // submit to the specific vm

				/*if (t3 < 1000)
				{
					//vm.getId()
					cloudlet.setVmId(vmIndex);
					//vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());	
				}*/

				// TO DINEI STO TADE VM TO TASK
				vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
				
				//Log.printLine(">>>>>>TESTVM " + vm.getMips() );
				//Ypologismos executiontime kai to apothikeuw sto t3
				

				if (vm == null) { // vm was not created
					Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
							+ cloudlet.getCloudletId() + ": bount VM not available");
					continue;
				}
				
				//if (t3 < 1000)
				//{
				//	sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
				//}
				
			}

			Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
					+ cloudlet.getCloudletId() + " to VM #" + vm.getId());

			cloudlet.setVmId(vm.getId());
			//EDW STELNEI TA CLOUDLETS STA VM'S!!
			sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
			cloudletsSubmitted++;
			vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
			getCloudletSubmittedList().add(cloudlet);
		}


		// remove submitted cloudlets from waiting list
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			getCloudletList().remove(cloudlet);
		}
	}


protected void processCloudletReturn(SimEvent ev) {
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		getCloudletReceivedList().add(cloudlet);
		Log.printLine(CloudSim.clock() + ": " + getName() + ": Cloudlet " + cloudlet.getCloudletId()
				+ " received");
		cloudletsSubmitted--;
		if (getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all cloudlets executed
			Log.printLine(CloudSim.clock() + ": " + getName() + ": All Cloudlets executed. Finishing...");
			clearDatacenters();
			finishExecution();
			
		} else { // some cloudlets haven't finished yet
			if (getCloudletList().size() > 0 && cloudletsSubmitted == 0) {
				// all the cloudlets sent finished. It means that some bount
				// cloudlet is waiting its VM be created
				clearDatacenters();
				createVmsInDatacenter(0);
			}

		}
		//DIKO MOU
		double exectime = cloudlet.getFinishTime() - cloudlet.getExecStartTime(); //
			Log.printLine("-------Cloudlet "+cloudlet.getCloudletId()+" Execute time : " +exectime);
	}

	protected void processVmCreate(SimEvent ev) {
		int[] data = (int[]) ev.getData();
		int datacenterId = data[0];
		int vmId = data[1];
		int result = data[2];
		double t2 = CloudSim.clock();
		Log.printLine("----------------------- Time for vm to create : " + t2);

		//Log.printLine("ZWOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

		if (result == CloudSimTags.TRUE) {
			getVmsToDatacentersMap().put(vmId, datacenterId);
			getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
			Log.printLine(CloudSim.clock() + ": " + getName() + ": VM #" + vmId
					+ " has been created in Datacenter #" + datacenterId + ", Host #"
					+ VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
		} else {
			Log.printLine(CloudSim.clock() + ": " + getName() + ": Creation of VM #" + vmId
					+ " failed in Datacenter #" + datacenterId);
		}



		incrementVmsAcks();

		// all the requested VMs have been created
		if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) {
			submitCloudlets();
		} else {
			// all the acks received, but some VMs were not created
			if (getVmsRequested() == getVmsAcks()) {
				// find id of the next datacenter that has not been tried
				for (int nextDatacenterId : getDatacenterIdsList()) {
					if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
						createVmsInDatacenter(nextDatacenterId);
						return;
					}
				}

				// all datacenters already queried
				if (getVmsCreatedList().size() > 0) { // if some vm were created
					submitCloudlets();
				} else { // no vms created. abort
					Log.printLine(CloudSim.clock() + ": " + getName()
							+ ": none of the required VMs could be created. Aborting");
					finishExecution();
				}
			}
		}
	}
}
