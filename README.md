# Zenoh Pub/Sub Docker Development Environment

This repository contains a Dockerfile that sets up a development environment
for testing the Zenoh Pub/Sub API with Python.

## Features

- Based on Ubuntu 22.04 LTS
- Python 3.10 and pip installed
- Zenoh Pub/Sub API installed via `pip install eclipse-zenoh`
- Example publisher and subscriber scripts included
- Interactive shell by default
- Ports exposed for Zenoh protocol (TCP/UDP) and REST API

## Getting Started


### Build the Docker Image

From the root directory (where the Dockerfile is located), run:

```bash
docker build -t zenoh-dev-env .
```

### Run the Docker Container

Start the container interactively amd expose ports:

```bash
docker run -it --rm --name zenoh-dev \ 
 -p 7447:7447/tcp \
 -p 8000:8000/tcp \
  zenoh-dev-env
```

### Using the Zenoh Pub/Sub Examples

Open two terminals

Terminal 1:
```bash
docker exec -it zenoh-dev bash
```
```bash
python3 examples/z_pub.py
```
Terminal 2:
```bash
docker exec -it zenoh-dev bash
```

```bash
python3 examples/z_sub.py
```

### Cleanup

to stop and remove the container:
```bash
docker stop zenoh-dev
```

```bash
docker rm zenoh-dev
```
