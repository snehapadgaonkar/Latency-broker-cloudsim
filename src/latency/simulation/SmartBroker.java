package latency.simulation;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

import java.util.*;

public class SmartBroker extends DatacenterBroker {

    private List<Datacenter> datacenterList;

    public SmartBroker(String name, List<Datacenter> datacenterList) throws Exception {
        super(name);
        this.datacenterList = datacenterList;
    }

    protected void processCloudletSubmit(SimEvent ev, boolean ack) {
        Cloudlet cloudlet = (Cloudlet) ev.getData();

        // Find VM with lowest latency datacenter
        Vm bestVm = null;
        int bestLatency = Integer.MAX_VALUE;

        for (Vm vm : getVmList()) {
            int dcId = getVmDatacenter(vm);
            int latency = getLatencyForDatacenter(dcId);

            if (latency < bestLatency) {
                bestLatency = latency;
                bestVm = vm;
            }
        }

        if (bestVm != null) {
            cloudlet.setVmId(bestVm.getId());
            getCloudletList().add(cloudlet);
        }

        if (ack) {
            sendNow(cloudlet.getUserId(), CloudSimTags.CLOUDLET_SUBMIT_ACK, cloudlet);
        }
    }

    private int getVmDatacenter(Vm vm) {
        // Simulate mapping of VM to a datacenter
        // (this assumes VM ID range: 0–1 = AWS, 2–3 = Azure, 4 = GCP)
        if (vm.getId() < 2) return datacenterList.get(0).getId(); // AWS
        if (vm.getId() < 4) return datacenterList.get(1).getId(); // Azure
        return datacenterList.get(2).getId(); // GCP
    }

    private int getLatencyForDatacenter(int dcId) {
        for (Datacenter dc : datacenterList) {
            if (dc.getId() == dcId) {
                // Simulate latency using name (hacky but simple)
                String name = dc.getName();
                if (name.equals("AWS")) return 20;
                if (name.equals("Azure")) return 15;
                if (name.equals("GCP")) return 25;
            }
        }
        return Integer.MAX_VALUE;
    }
}
