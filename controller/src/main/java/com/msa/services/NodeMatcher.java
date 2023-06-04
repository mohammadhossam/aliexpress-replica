package com.msa.services;

import com.msa.models.Machine;
import com.msa.models.RunningInstance;
import com.msa.models.ServiceType;
import com.msa.repos.MachinesRepo;
import com.msa.repos.RunningInstanceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Component
@AllArgsConstructor
public class NodeMatcher {

    private final MachinesRepo machinesRepo;
    private final RunningInstanceRepo runningInstanceRepo;
    // This class is responsible for finding a suitable machine to deploy a service on

    // 1. get the list of all machines
    // 2. get the list of all running services
    // 3. match the service with the machine that has the least number of services running on it

    public Machine findNode(ServiceType serviceType) {
        // get the list of all machines
        Iterable<Machine> machines = machinesRepo.findAll();
        // get the list of all running services
        Iterable<RunningInstance> runningInstances = runningInstanceRepo.findAll();
        // create a hashtable that maps machine id to machine object
        Hashtable<String, Machine> machineHashtable = new Hashtable<>();
        for (Machine machine : machines) {
            machineHashtable.put(machine.getId(), machine);
        }
        // create a hashtable that maps machine id to the number of services running on it
        Hashtable<String, Integer> machineServicesCount = new Hashtable<>();
        for (Machine machine : machines) {
            machineServicesCount.put(machine.getId(), 0);
        }
        // find the machine that has the least number of services running on it
        // and doesn't have the same serviceType running on it
        for (RunningInstance runningInstance : runningInstances) {
            // if the machine is running a service of the same type, skip it
            if (runningInstance.getServiceType().equals(serviceType)) {
                machineServicesCount.remove(runningInstance.getHost().getId());
            } else {
                if (machineServicesCount.containsKey(runningInstance.getHost().getId()))
                    machineServicesCount.put(
                            runningInstance.getHost().getId(),
                            machineServicesCount.get(runningInstance.getHost().getId()) + 1
                    );
            }
        }

        // find the machine with the least number of services running on it
        String minMachineId = null;
        int minServicesCount = Integer.MAX_VALUE;
        for (String machineId : machineServicesCount.keySet()) {
            if (machineServicesCount.get(machineId) < minServicesCount) {
                minServicesCount = machineServicesCount.get(machineId);
                minMachineId = machineId;
            }
        }

        // if there is no machine that doesn't have the same serviceType running on it
        if (minMachineId == null)
            return null;

        // return the machine
        return machineHashtable.get(minMachineId);
    }

}
