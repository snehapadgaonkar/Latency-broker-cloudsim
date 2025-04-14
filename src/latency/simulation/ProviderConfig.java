package latency.simulation;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.*;

public class ProviderConfig {

    public static Datacenter createProvider(String name, double costPerSec, int latency) throws Exception {
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000))); // 1 core @ 1000 MIPS

        Host host = new Host(
                0,
                new RamProvisionerSimple(4096), // 4 GB RAM
                new BwProvisionerSimple(10000), // 10 Gbps BW
                1000000, // Storage
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                "x86", "Linux", "Xen", hostList,
                10.0,    // time zone
                3.0,     // cost per memory
                costPerSec, // cost per sec
                0.05,    // cost per storage
                0.1      // cost per BW
        );

        return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), new LinkedList<>(), latency);
    }
}
