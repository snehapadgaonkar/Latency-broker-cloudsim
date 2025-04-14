# ☁️ Broker-Based Multi-Cloud Task Scheduling for Latency Optimization using CloudSim

![Java](https://img.shields.io/badge/Java-11-blue.svg)
![CloudSim](https://img.shields.io/badge/CloudSim-3.0.3-green)
![Build](https://github.com/snehapadgaonkar/Latency-broker-cloudsim/actions/workflows/build.yml/badge.svg)
![Platform](https://img.shields.io/badge/Platform-Eclipse%20IDE-lightgrey)
![License](https://img.shields.io/badge/License-MIT-lightblue)

This project simulates a smart cloud broker that dynamically provisions virtual machines (VMs) across multiple cloud providers (AWS, Azure, and GCP) based on latency and availability using the CloudSim simulation toolkit.

## 📦 Features

✅ SmartBroker with basic latency-awareness  
✅ Multi-cloud simulation with AWS, Azure, GCP  
✅ Dynamic VM creation and cloudlet scheduling  
✅ Retry mechanism for failed VM deployments (basic fault tolerance)  
✅ GitHub Actions CI/CD pipeline to automate builds  
✅ Modular and easy to extend 🧩

---

## 🛠️ Tech Stack

- **Language**: Java 11  
- **Simulator**: CloudSim 3.0.3  
- **Build Tool**: Eclipse IDE  
- **CI/CD**: GitHub Actions  

---

## 🧠 How it Works

- Each provider (AWS, Azure, GCP) has its own datacenter with specific latency and cost.
- The SmartBroker attempts to allocate VMs in the best-performing provider.
- If VM creation fails, it retries in the next best provider (basic fault tolerance).
- Cloudlets are distributed to created VMs, and execution results are collected.

---

## 🚀 How to Run (Eclipse)

1. **Clone the repo**
```bash
git clone https://github.com/snehapadgaonkar/Latency-broker-cloudsim.git
```
2. **Import into Eclipse** → File → Import → Existing Project into Workspace
3. **Add Libraries** → Create a `libs` folder and add:
   - `cloudsim-3.0.3.jar`
   - `cloudsim-examples-3.0.3.jar`
   - `commons-math3-3.6.1.jar`
4. **Run** → `LatencySim.java`

---

## 🔁 CI/CD Pipeline

This project includes a GitHub Actions workflow to:
- Set up Java
- Compile source files
- Run the simulation headlessly in CI

```yaml
name: Java CI for CloudSim
```
📄 See `.github/workflows/build.yml`

---

## 🧹 Future Improvements

- Implement SLA-aware scheduling
- Support for dynamic workloads
- Advanced fault tolerance (e.g. retry thresholds, live migration)

---

## 📜 License

This project uses [CloudSim](https://github.com/Cloudslab/cloudsim), licensed under the GPLv3 which is free to use for educational and research purposes.
