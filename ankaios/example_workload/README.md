# Ankaios Workload

In order to enhance the SDV lab with additional custom applications managed by Eclipse Ankaios, a containerised application is required.

This folder can be used as a starting point to create a workload and provides the following:

| File             | Description                                                                                                                                    |
|------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| Dockerfile       | To create a container image from the app.                                                                                                      |
| app.py           | The file contains the code of your app written in Python. In this case, it uses the [ank-sdk-python](https://github.com/eclipse-ankaios/ank-sdk-python/tree/v0.6.0) (Ankaios Python SDK) to instruct Ankaios to create another workload dynamically. |
| manifest.yaml    | The [Ankaios manifest](https://eclipse-ankaios.github.io/ankaios/0.6/reference/startup-configuration/) to start the new workload with Ankaios. |
| requirements.txt | The Python packages required by the app.                                                                                                       |

## Ankaios Control Interface

A workload managed by Ankaios can communicate via the [Ankaios Control Interface](https://eclipse-ankaios.github.io/ankaios/0.6/reference/control-interface/) with Ankaios itself. This is useful when your custom workload needs information about the state of other workloads or to request Ankaios to start or stop another workload dynamically.

For easy integration of the [Control Interface](https://eclipse-ankaios.github.io/ankaios/0.6/reference/control-interface/) in your app, there are two Ankaios SDKs ([ank-sdk-python](https://github.com/eclipse-ankaios/ank-sdk-python/tree/v0.6.0) and [ank-sdk-rust](https://github.com/eclipse-ankaios/ank-sdk-rust/tree/v0.6.0)) available.

The example workload uses the `ank-sdk-python` and demonstrates a dynamic workload start by:

- creating a workload `dynamic_workload` dynamically
- using the workload states to wait until the newly added workload is in the state `Running(Ok)`
- retrieving the current workload states of Ankaios

The [ank-sdk-python examples](https://github.com/eclipse-ankaios/ank-sdk-rust/tree/v0.6.0/examples) provide lots of other examples of what you can do with the Control Interface.

Using the Control Interface is optional and you can remove the Python code from `app.py`, the `ankaios-sdk` python package from the `requirements.txt` and the `controlInterfaceAccess` configuration in the [manifest.yaml](manifest.yaml) if you do not need it in your specific use case.

## How to run

First you need to build your app with `Podman`:

```shell
sudo podman build -t localhost/example_workload:0.1 .
```

Afterwards, if not already running, start the Ankaios cluster:

```shell
sudo systemctl start ank-server ank-agent
```

Apply the new manifest containing your new app:

```shell
ank apply manifest.yaml
```

Get the states of the workloads:

```shell
ank get workloads
```

Delete the workloads:

```shell
ank delete workloads dynamic_workload example
```

### Workload logs

For local development and debugging you might want to see the log output of your Python workload.

Use the `ank-cli` to show the logs from your custom workload:

```shell
ank logs -f example
```