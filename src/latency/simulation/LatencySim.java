package latency.simulation;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.*;

public class LatencySim {

    public static void main(String[] args) {

        Log.printLine("Starting Multi-Cloud Latency Simulation...");

        try {
            // 1. Initialize CloudSim
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;
            CloudSim.init(numUsers, calendar, traceFlag);

            // 2. Create Datacenters
            Datacenter aws = createDatacenter("AWS", 0.12, 20);    // latency 20ms
            Datacenter azure = createDatacenter("Azure", 0.10, 15); // latency 15ms
            Datacenter gcp = createDatacenter("GCP", 0.09, 25);     // latency 25ms

            // 3. Create Smart Broker
            SmartBroker broker = new SmartBroker("SmartBroker", Arrays.asList(aws, azure, gcp));

            // 4. Create VMs
            List<Vm> vmlist = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Vm vm = new Vm(i, broker.getId(), 1000, 1, 1024, 1000, 1000, "Xen", new CloudletSchedulerTimeShared());
                vmlist.add(vm);
            }
            broker.submitVmList(vmlist);

            // 5. Create Cloudlets
            List<Cloudlet> cloudletList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
                cloudlet.setUserId(broker.getId());
                cloudletList.add(cloudlet);
            }
            broker.submitCloudletList(cloudletList);

            // 6. Start Simulation
            CloudSim.startSimulation();

            // 7. Stop Simulation
            CloudSim.stopSimulation();

            // 8. Print Results
            List<Cloudlet> receivedList = broker.getCloudletReceivedList();
            printCloudletResults(receivedList);

            Log.printLine("Simulation finished!");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Simulation failed.");
        }
    }

    // ---------- Helper Methods Below ----------

    private static Datacenter createDatacenter(String name, double costPerSec, int latency) throws Exception {
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        hostList.add(new Host(0, new RamProvisionerSimple(4096),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)));

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                "x86", "Linux", "Xen", hostList,
                10.0, 3.0, costPerSec, 0.05, 0.1);

        Datacenter datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), new LinkedList<>(), latency);
        return datacenter;
    }

    private static void printCloudletResults(List<Cloudlet> list) {
        String indent = "    ";
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data Center ID" + indent +
                "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        for (Cloudlet cloudlet : list) {
            Log.print(cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS" + indent);
                Log.print(cloudlet.getResourceId() + indent + indent);
                Log.print(cloudlet.getVmId() + indent + indent);
                Log.print(cloudlet.getActualCPUTime() + indent + indent);
                Log.print(cloudlet.getExecStartTime() + indent + indent);
                Log.printLine(cloudlet.getFinishTime());
            }
        }
    }
}
