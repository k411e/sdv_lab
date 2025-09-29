# Digital Cluster MQTT Integration

## Overview
This project implements a digital cluster application for Android that communicates with a vehicle system using MQTT protocol. The application subscribes to vehicle parameters and displays them in a digital dashboard, while also allowing users to control vehicle features like cruise control.

## Architecture
The system consists of three main components:

1. Android Application (Digital Cluster) - Subscribes to vehicle parameters and displays them in a user-friendly interface. Users can interact with the interface to control vehicle features.

2. Publisher Script (publisher.py) - Simulates vehicle sensors by periodically publishing vehicle parameters (speed, RPM, temperature, etc.) to the MQTT broker.

3. Subscriber Script (subscriber.py) - Listens for control commands from the Android application (like cruise control activation) and processes them.

## Communication Flow

```

┌─────────────────┐      MQTT Broker      ┌─────────────────┐
│                 │         Topic:         │                 │
│   publisher.py  │─────────────────────▶ │ Android App     │
│   (Vehicle      │  "vehicle/parameters"  │ (Digital        │
│    Simulator)   │                        │  Cluster)       │
│                 │                        │                 │
└─────────────────┘                        └────────┬────────┘
                                                    │
                                                    │ User activates
                                                    │ cruise control
                                                    ▼
┌─────────────────┐      MQTT Broker      ┌─────────────────┐
│                 │         Topic:         │                 │
│  subscriber.py  │◀────────────────────── │ Android App     │
│  (Vehicle       │  "vehicle/parameters"  │ (Digital        │
│   Controller)   │                        │  Cluster)       │
│                 │                        │                 │
└─────────────────┘                        └─────────────────┘
```

## Data Format

```
{
  "Speed": 75,
  "RPM": 2500,
  "CruiseControl": false,
  "Gear": "D",
  "AmbientTemperature": 22,
  "EngineTemperature": 90,
  "Battery": 95,
  "Range": 350,
  "Economy": "8.5L/100km",
  "TypeOfVehicle": 0,
  "SpeedUnit": "km/h",
  "TemperatureUnit": 0,
  "ShareLocation": true
}

```

## Implementation Details

### Android Application
The Android application uses a MQTT client library (HiveMQ) to connect to the MQTT broker and implements the following features:

- Connection Management: Establishes and maintains connection to the MQTT broker
- Subscription: Subscribes to the "vehicle/parameters" topic to receive vehicle data
- Publication: Publishes control commands when user interacts with the UI
- Data Processing: Parses incoming JSON messages and updates the UI accordingly
- Error Handling: Handles connection issues and message parsing errors

### Publisher Script (publisher.py)
The publisher script simulates a vehicle by:

- Connecting to the MQTT broker
- Generating realistic vehicle parameter values
- Publishing these values to the "vehicle/parameters" topic at regular intervals
- Simulating changes in vehicle state (speed)

### Subscriber Script (subscriber.py)
The subscriber script acts as the vehicle controller by:

- Connecting to the MQTT broker
- Subscribing to the "vehicle/parameters" topic to listen for control commands
- Processing cruise control activation/deactivation commands
- Acknowledging received commands by publishing status updates