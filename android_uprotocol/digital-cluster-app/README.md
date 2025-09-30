## Overview

**Digital Cluster App** is a modern automotive instrument cluster simulation built with Android and Jetpack Compose. It provides a customizable digital dashboard that can display vehicle information such as speed, RPM, battery usage , gear position and  power usage  level indicator. The app uses **UProtocol** for real-time communication, making it suitable for integration with vehicle data systems or simulators like CARLA.

## Features

- **Real-time Vehicle Data Display**: Speed, battery usage, gear position and power usage
- **Customizable Driving Modes**: Switch between different driving modes (Race, Sport+, City)
- **Status Indicators**: Cruise control, battery usage, lights, and warning indicators // Only cruise control functionality is working, the rest is only for UI purposes
- **Interactive Controls**: Increase/decrease speed, toggle cruise control, and activate sensors // activate sensors only UI purposes
- **UProtocol Communication**: transport-agnostic communication framework for software-defined vehicles (SDVs) that facilitates seamless data exchange between apps, services, and devices across different platforms, including vehicles, the cloud, and mobile devices.
- **Vehicle Type Support**: Displays appropriate gauges for electric vehicles

## Architecture

The app follows a modular clean architecture approach:

- **UI Layer**: Jetpack Compose UI components with a ViewModel
- **Domain Layer**: Business logic and models
- **Data Layer**: MQTT communication and repositories
- **Core Modules**: Design system, shared utilities, and common components

### Architecture Diagram

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│  Presentation   │     │     Domain      │     │      Data       │
│                 │     │                 │     │                 │
└────────┬────────┘     └────────┬────────┘     └────────┬────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────────┐
│  Compose UI     │     │  Domain Models  │     │  Repositories       │
│  ViewModels     │◄────┤  Use Cases      │◄────┤  Data Sources       │
│  UI Components  │     │  Actions        │     │  UPrtotocol Client  │
└─────────────────┘     └─────────────────┘     └─────────────────────┘
```

## Technology Stack

- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern UI toolkit for building native UI
- **Hilt**: Dependency injection
- **Coroutines & Flow**: Asynchronous programming and reactive streams
- **uProtocol**: Unified communication protocol for vehicle data (MQTT Broker)

## Project Structure

```
├── app/                          # Main application module
│   └── DigitalClusterApplication.kt  # Application class with resource preloading
├── core/
│   ├── data/                     # Data layer with repositories and MQTT
│   │   ├── di/                   # Dependency injection modules
│   │   ├── mqtt/                 # Defines all the UProtocol components (Subscriber & RPC Invoker)
│   │   └── repository/           # Data repositories
│   ├── designsystem/             # UI design system and theming
│   └── domain/                   # Business logic and models
│       ├── action/               # User actions and events
│       └── model/                # Domain models
└── feature/
    └── cluster/                  # Cluster display feature
        ├── ui/                   # UI components
        │   ├── component/        # Reusable UI components
        │   └── screen/           # Full screens
        └── viewmodel/            # ViewModels for the feature
```

## Key Components

### Core Components

1. **DigitalClusterApplication**: Main application class that initializes the app and preloads critical resources.
2. **MainActivity**: Entry point that sets up the UI and manages MQTT connections.
3. **MqtModule**: Provides dependencies through Hilt.

### Data Layer

1. **UProtocolRpcClientMethodInvoker**: Invoke a method on the RPC server level, and receive server callbacks.
2. **UProtocolSubscriber**: Subscribes to MQTT topics and processes incoming messages.
3. **UProtoMqtt**: Manages Uprotocol Utransport instance and allow method to start and close the connection.
5. **MqttClusterRepository**: Interface for cluster data operations.
6. **MqttClusterRepositoryImpl**: Implementation of the repository that handles data operations.

### Domain Layer

1. **ClusterState**: Data class representing the state of the vehicle's instrument cluster.
2. **ClusterAction**: Sealed class representing user actions on the cluster.
3. **CentralScreenState**: Enum defining different central display modes.

### UI Components

1. **Cluster**: Main composable that displays the entire instrument cluster.
2. **ClusterScreen**: Screen that hosts the Cluster composable and connects it to the ViewModel.
3. **ClusterSpeedDisplay**: Displays the vehicle speed with a gauge.
4. **ClusterBatteryDisplay**: Displays battery information for electric vehicles.
5. **ClusterMiddleDisplay**: Displays the central screen content based on the current state.
6. **ClusterTopBar**: Displays status indicators in the top bar.
7. **ControlButtons**: Interactive buttons for controlling the cluster.

### ViewModel

**ClusterViewModel**: Manages the state of the cluster and handles business logic.

## Communication Flow

1. **Data Reception**:
    
    - External data is received via UProtocol (MQTT)
    - `UProtocolSubscriber` process incoming messages
    - Data is converted to `ClusterState` objects
2. **State Management**:
    
    - `MqttClusterRepository` maintains the current state
    - `ClusterViewModel` exposes this state to the UI
    - State changes trigger UI updates through Compose's state management
3. **User Interaction**:
    
    - User actions are captured as `ClusterAction` objects
    - `ClusterViewModel` processes these actions
    - Actions may result in state updates or MQTT messages being published
4. **Method Invokation**:
    
    - `UProtocolRpcClientMethodInvoker` invoke remotely using RPC a server method
    - The server later will send a callback if the operation was successful or not.

## Usage

### Control Board

- **Speed Control**: Use the + and - buttons to increase or decrease speed
- **Cruise Control**: Toggle cruise control with the cruise control button
- **Location Sharing**: Toggle location sharing with the location button
- **Sensor Views**: Access different sensor information screens: // Only Static UI
    - Front/Rear sensors: Show forward collision detection view
    - Left/Right sensors: Toggle between blind spot detection and mode display

### UProtocol Topics

- `//cruise-control.app/C110/1/8000`: Main topic for vehicle data exchange, 
- `//cruise-control.app/C110/1/1`: Set target operation RPC.
- `//android-cruise-control.app/BBB/1/0`: Local Service Uri.

### Data Formats

1. **JSON Format** (Primary):

```json
{
  "Speed": 100,
  "CruiseControl": true,
  "RPM": 3500,
  "EngineTemperature": 90,
  "Gear": "D",
  "AmbientTemperature": 25,
  "Economy": "11.6 km/L",
  "SpeedUnit": "mph",
  "Battery": 80,
  "Range": 350,
  "TemperatureUnit": 0,
  "ShareLocation": true,
  "TypeOfVehicle": 0
}
```

2. **Key-Value Format** (Mosquitto):

```
speed=100
cruisecontrol=true
rpm=3500
enginetemp=90
gear=D
```

## Performance Optimizations

1. **Resource Preloading**: Critical drawable resources are preloaded at startup.
2. **State Caching**: The ViewModel caches state to avoid unnecessary repository calls.
3. **Composition Optimization**: UI components use `remember` and `derivedStateOf` to prevent unnecessary recompositions.
4. **Debouncing**: User actions are debounced to prevent rapid state changes.
