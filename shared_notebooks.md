
# Shared Notebook Installation

This can be ignored for participants, since the shared notebooks should be already setup when the events starts. It is for internal documentation and in the case a shared notebook must be setup newly because of issues.

## Install CARLA

Please follow the guideline for CARLA Setup: [Carla Quick Start](https://carla.readthedocs.io/en/latest/start_quickstart/)
Be sure to use the version [0.9.15](https://github.com/carla-simulator/carla/releases/tag/0.9.15/)

## Install AAOS Digital Cluster and Sync the Project

### Install Android Studio

Start by downloading and installing the latest version of Android Studio:

🔗 [Download Android Studio](https://developer.android.com/studio)

Make sure the following components are also set up:
- Android SDK properly configured
- Git installed
- Access to the project source code
- A physical device or emulator available for testing

---

### Sync the Project

Once the project is open in Android Studio:

- Navigate to **File > Sync Project with Gradle Files**
- Wait for Gradle to resolve dependencies and build the project

This step ensures your environment is correctly configured and ready for development.

---

### Create a Virtual Device (Emulator)

If you don’t have access to a physical device, you can create an emulator to test the app:

1. Go to **Tools > Device Manager**
2. Click **Create Device**
3. Select a device model (e.g., Pixel Tablet) and click **Next**
4. Choose a system image (e.g., Android 15) and download it if needed
5. Click **Finish** to create the virtual device
6. Launch the emulator by clicking the **Play** icon next to the device
7. Install adb by using the command: sudo apt install adb
8. Change the density to 200 using the command: adb shell wm density 200

---

### Run the Application

With your device ready:

- Select the emulator or connected physical device from the device dropdown
- Click the **Run** button (▶️) or press `Shift + F10`
- The app will build and launch on the selected device


## Install Eclipse Ankaios

Install podman first:

```shell
sudo apt-get update -y
sudo apt-get install -y podman
```

Install Ankaios v0.6.0:

```shell
curl -sfL https://github.com/eclipse-ankaios/ankaios/releases/download/v0.6.0/install.sh | bash -s -- -v v0.6.0
```

Systemd unit files are automatically installed for `ank-server` and `ank-agent`.

Set a persistent startup-config for `ank-server` by editing `/etc/systemd/system/ank-server.service`:

```shell
sudo vi /etc/systemd/system/ank-server.service
```

Add the CLI argument `--startup-config /etc/ankaios/state.yaml`.

Reload systemd settings:

```shell
sudo systemctl daemon-reload
```

Copy paste the content of [shared-notebooks-manifest.yaml](./shared-notebooks-manifest.yaml) content into `/etc/ankaios/state.yaml` on the shared notebook.

Start Ankaios Server and the Ankaios Agent:

```shell
sudo systemctl start ank-server ank-agent
```

Check the workload states (it might be take some time until the container images are downloaded and the workloads are up and running):

```shell
ank get workloads --watch
```

Press Ctrl+C if all workloads have reached the running state.

If you need to delete the workloads and start from scratch, just stopping the Ankaios Server and Ankaios agent does not delete the workloads, they would still be running (automotive use case!).

Instead you have to:

```shell
ank delete workloads ustreamer mqtt_broker
```

or 

```shell
ank apply -d /etc/ankaios/state.yaml
```

and then:

```shell
sudo systemctl stop ank-server ank-agent
```

Next time you execute `sudo systemctl start ank-server ank-agent` all workloads are deployed newly.

## Checking workload logs

```shell
ank logs -f <workload_name>
```

Replace workload_name with `ustreamer`, `mqtt_broker` or any other workload name you want to check logs from.
