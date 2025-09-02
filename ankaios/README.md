The projec use the [Eclipse Ankaios](https://eclipse-ankaios.github.io/ankaios/0.6). This is an embedded software orchestrator targeted for automotive High Performance Computer (HPC) which suppports pluggable container runtimes. In the hackathon, Eclipse Ankaios with version v0.6.0 is used.

Ankaios supports [Podman](https://docs.podman.io/en/latest/) as container runtime.

To understand the next steps, please view the architecture diagram in the folder architecture

# Prerequisites

- Two computers with Ubuntu versions 20.04, 22.04 or 24.04.
- One computer with a dedicated GPU equivalent to an NVIDIA 2070 or better with at least 8Gb of VRAM Or more.

# PC1 setup

## CARLA installation and execution

### CARLA running in a container (requires NVIDIA graphics card)

To run CARLA in a container is required the following:
- NVIDIA graphics card
- NVIDIA container toolkit installed

The NVDIA container toolkit can be installed with the commands below:

```
curl -fsSL https://nvidia.github.io/libnvidia-container/gpgkey | sudo gpg --dearmor -o /usr/share/keyrings/nvidia-container-toolkit-keyring.gpg \
  && curl -s -L https://nvidia.github.io/libnvidia-container/stable/deb/nvidia-container-toolkit.list | \
    sed 's#deb https://#deb [signed-by=/usr/share/keyrings/nvidia-container-toolkit-keyring.gpg] https://#g' | \
    sudo tee /etc/apt/sources.list.d/nvidia-container-toolkit.list

sudo apt-get update

export NVIDIA_CONTAINER_TOOLKIT_VERSION=1.17.8-1
  sudo apt-get install -y \
      nvidia-container-toolkit=${NVIDIA_CONTAINER_TOOLKIT_VERSION} \
      nvidia-container-toolkit-base=${NVIDIA_CONTAINER_TOOLKIT_VERSION} \
      libnvidia-container-tools=${NVIDIA_CONTAINER_TOOLKIT_VERSION} \
      libnvidia-container1=${NVIDIA_CONTAINER_TOOLKIT_VERSION}
```

In the file cruise_control.yaml the carla workload is commented, please uncomment it.

### CARLA standalone

Download CARLA from [GitHub Repository](https://github.com/carla-simulator/carla/releases/tag/0.9.15/)

To run CARLA execute the commands below

```
cd path/to/carla/root
./CarlaUE4.sh

```

For additional details please check the page [CARLA installation](https://carla.readthedocs.io/en/latest/start_quickstart/#carla-installation)



## Podman installation

You can install Podman easily with the package manager like in Ubuntu:

```
sudo apt-get update
sudo apt-get -y install podman
```

Otherwise follow the official [Podman installation instructions](https://podman.io/docs/installation#installing-on-linux).

## Ankaios installation

Install Eclipse Ankaios with a single curl according to the [Ankaios installation guide](https://eclipse-ankaios.github.io/ankaios/latest/usage/installation).

Follow the `Setup with script` section and install the version Eclipse Ankaios v0.6.0.

**Note:** When using Ubuntu-24.04 disable AppArmor like discribed in the Ankaios installation guide.

The installation script will automatically create a systemd service file for the Ankaios server and an Ankaios agent.

## Run Ankaios server

```
sudo systemctl start ank-server
```

When this command is executed the service ank-server will read the configurations in the file `/etc/ankaios/ank-server.conf`.
 
## Run Ankaios agent

```
sudo systemctl start ank-agent
```

## MQTT broker

The mqtt broker can be configured in the file /ankaios/mosquitto/mosquitto.conf

## Start the Cruise Control Demo Scenario

To start the cruise control demo scenario, apply the following Ankaios manifest:

```
ank -k apply cruise_control.yaml
```

**Note:** If you want to remove all workloads specified in the `cruise_control.yaml` you can simply add `-d` paramter to the `ank apply` like the following:
`ank apply -d cruise_control.yaml`. This might be helpful for incremental development.

# PC2 setup

Install Eclipse Ankaios and the Podman container engine like done for [PC1 setup](#pc1-setup).

In the file /etc/ankaios/ank-agent.conf perform the following changes:
- In the field `name` set "agent_client"
- In the field `server_url` set "https://<SERVER_IP>:25551"

```
sudo systemctl start ank-agent
```

# Additional Ankaios commands

```
ank get state // Retrieve information about the current Ankaios system
ank get workload // Information about the worloads running in the Ankaios system
ank get agent // Information about the Ankaios agents connected to the Ankaios server
ank logs <workload_name> // Retrieve the logs from a workload
ank help // Get the help
```




