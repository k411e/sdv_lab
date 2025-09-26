# SDV Lab

Simulate a realistic, modular, and cyber-physical Software-Defined Vehicle (SDV) development environment where teams can design, implement, and demonstrate an SDV feature that
operates across a virtual platform.

The challenge comes with demo applications in the SDV Lab environment. Be creative and innovative and enhance the existing demo scenarios or develop applications in the SDV Lab environment for your custom use case.

## Prerequisites

- Linux Notebook Ubuntu 22.04/24.04 or WSL2 or a Linux VM
- Shared Notebook per team that you will get from the hack coaches

## Installation

### User Notebook

On your notebook you will use [Eclipse Ankaios](https://eclipse-ankaios.github.io/ankaios/0.6) as embedded software orchestrator to start containerized workloads for this challenge. The example applications you can run are also managed by Ankaios.

#### Install Podman

As [Eclipse Ankaios](https://eclipse-ankaios.github.io/ankaios/0.6) as embedded software orchestrator is used to manage workloads inside the SDV Lab Challenge, you will need to install [Podman](https://docs.podman.io/en/latest/) as a container engine.

You can install Podman easily with the package manager like in Ubuntu:

```
sudo apt-get update
sudo apt-get -y install podman
```

Otherwise follow the official [Podman installation instructions](https://podman.io/docs/installation#installing-on-linux).

#### Install Eclipse Ankaios

Install [Eclipse Ankaios](https://eclipse-ankaios.github.io/ankaios/0.6) with a single curl according to the [Ankaios installation guide](https://eclipse-ankaios.github.io/ankaios/latest/usage/installation).

Follow the `Setup with script` section and install the version Eclipse Ankaios [v0.6.0](https://github.com/eclipse-ankaios/ankaios/releases/tag/v0.6.0).

**Note:** When using Ubuntu-24.04 disable AppArmor like discribed in the Ankaios installation guide.

The installation script will automatically create a systemd service file for the Ankaios server and an Ankaios agent.

### Shared Notebook

As an participant you can ignore this section. The shared notebooks were already setup and this is only linked to reinstall the services if the shared notebook has some troubles.
Look into the detailed [shared_notebooks.md](./shared_notebooks.md) about how to install the shared services on the shared notebooks from the hack coaches.

## Run

Start the `cruise control` scenario by starting:

- CARLA on PC1
- AAOS Digital Cluster (Android Cuttlefish IVI) on PC1
- Eclipse Ankaios cluster on PC2
- Applying the Ankaios manifest [cruise_control.yaml](./cruise_control.yaml)

### Run Ankaios

```shell
sudo systemctl start ank-server ank-agent
```

### Apply the workload example manifest

In each example workload folder you will find an Ankaios manifest which you can apply to your local running Ankaios cluster to start this example demo workload. Since the example workloads need to connect to services on the shared notebook, please find out the ip address of the shared notebook, connect your notebook to the Hack Wifi and replace remote address e.g. for MQTT broker to point to the external shared notebook NIC's ip address.

Navigate to the example workload folder you want to run and apply the manifest, example:

```shell
cd uprotocol/cruise-control-app
ank apply cruise-control-app.yaml
```

**Note:** If you want to remove all workloads specified in the Ankaios manifest for cleaning up you can simply add `-d` paramter to the `ank apply` like the following:
`ank apply -d cruise-control-app.yaml`. This might be helpful for incremental development, when you change the example code.

## Additional Ankaios commands

```
ank logs <workload_name> // Retrieve the logs from a workload
ank get state // Retrieve information about the current Ankaios system
ank get workloads // Information about the worloads running in the Ankaios system
```

## Workload starting other workloads

Inside [ankaios/example_workloads/README.md](./ankaios/example_workloads/README.md) there are two example workloads, one using the Ankaios Python SDK and the other one using the Ankaios Rust SDK, both using the [Ankaios Control Interface](https://eclipse-ankaios.github.io/ankaios/0.6/reference/control-interface/) to instruct Ankaios as a workload to start dynamically other workloads. This is common in the SDV world since workloads do not have to run always. Workloads can start other workloads or you can manage the Ankaios cluster also from within a workload. If your specific use case in the SDV Lab needs such feature, you can start with the example workloads there as a template and adapt it for your needs.