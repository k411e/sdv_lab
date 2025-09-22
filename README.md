# SDV Lab

Simulate a realistic, modular, and cyber-physical Software-Defined Vehicle (SDV) development environment where teams can design, implement, and demonstrate an SDV feature that
operates across a virtual platform.

The challenge comes with a demo scenario, called the `Cruise Control`, running a cruise control ADAS application in the SDV Lab environment. Be creative and innovative and enhance the existing `Cruise Control` scenario or develop applications in the SDV Lab environment for your custom use case.

## Cruise Control Scenario Architecture

The architecture of the project is described in the file `architecture/Cruise_Control.svg`.

## Prerequisites

You will need two computers with Ubuntu versions 22.04 or 24.04. One for deploying the applications inside the SDV Lab and the other one for deploying the CARLA simulator. If you do not have a notebook supporing CARLA to run, get a notebook from the hack coache setup with CARLA already or connect to CARLA notebooks hosted in Porto using `tailscale` if you are in Berlin.

## Installation

### PC1

#### CARLA

Setup the notebook with CARLA or get one from the hack coaches.

Download CARLA from [GitHub Repository](https://github.com/carla-simulator/carla/releases/tag/0.9.15/)

For additional details please check the page [CARLA installation](https://carla.readthedocs.io/en/latest/start_quickstart/#carla-installation)

#### Install the Android Cuttlefish IVI

On the PC where CARLA is running, install the Android Cuttlefish IVI (`aaos_digital_cluster`).

### PC2

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

We will use the systemd files as user service to start and stop Eclipse Ankaios, so copy the created systemd service files with the following commands:

```shell
mkdir -p ~/.config/systemd/user/
cp /etc/systemd/system/ank-server.service ~/.config/systemd/user/ank-server.service
cp /etc/systemd/system/ank-agent.service ~/.config/systemd/user/ank-agent.service
```

## Run

Start the `cruise control` scenario by starting:

- CARLA on PC1
- AAOS Digital Cluster (Android Cuttlefish IVI) on PC1
- Eclipse Ankaios cluster on PC2
- Applying the Ankaios manifest [cruise_control.yaml](./cruise_control.yaml)


### Run CARLA

```
cd path/to/carla/root
./CarlaUE4.sh

```

### Run AAOS Digital Cluster


TODO!
```shell
```

### Build the container images

For the EgoVehicle like described in the [EgoVehicle/README.md](./EgoVehicle/README.md#build-for-deployment) (just one script call!).

### Run Ankaios

```shell
systemctl --user status ank-server ank-agent
```

### Apply the cruise control manfiest

```shell
ank apply cruise_control.yaml
```

**Note:** If you want to remove all workloads specified in the `cruise_control.yaml` you can simply add `-d` paramter to the `ank apply` like the following:
`ank apply -d cruise_control.yaml`. This might be helpful for incremental development.

#### Additional Ankaios commands

```
ank logs <workload_name> // Retrieve the logs from a workload
ank get state // Retrieve information about the current Ankaios system
ank get workloads // Information about the worloads running in the Ankaios system
```


