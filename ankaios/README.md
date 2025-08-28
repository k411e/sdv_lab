The ankaios is a system used for container orchestration. 

The ankaios use podman, this is used to create images, manage containers and more. The podman is similar to docker.

To understand the steps in this file please chgeck the ankaios architecture in the following page https://eclipse-ankaios.github.io/ankaios/0.3/architecture/

# Commands to execute in the GPU server

## Prerequesites to run CARLA in a container
GPU server with NVIDIA graphics card\
NVIDIA Container toolkit installed

The NVIDIA container toolkit can be installed with the commands below:

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

## Podman installation
```
sudo apt-get update
sudo apt-get -y install podman
```
## Ankaios installation
```
curl -sfL https://github.com/eclipse-ankaios/ankaios/releases/latest/download/install.sh | bash -
```

## Run Ankaios server
```
sudo systemctl start ank-server
```

When this command is executed the service ank-server will read the configurations in the file /etc/ankaios/ank-server.conf

 
## Run Ankaios agent
In the file /etc/ankaios/ank-agent.conf in the field name set "agent_server"
```
sudo systemctl start ank-agent
```
## Apply Ankaios manifest to create the desired state
```
ank apply cruise_control.yaml
```

# Commands to execute in the user computer
Install the ankaios and podman like it was done in the GPU server

In the file /etc/ankaios/ank-agent.conf perform the following changes:
- In the field name set "agent_client"
- In the field server_url set "https://<SERVER_IP>:25551"

```
sudo systemctl start ank-agent
```
# Additional Ankaios commands

```
ank get state // Retrieve information about the current Ankaios system
ank get workload // Information about the worloads running in the Ankaios system
ank get agent // Information about the Ankaios agents connected to the Ankaios server
ank help // Get the help
```




