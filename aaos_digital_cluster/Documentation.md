# Overview

**Digital Cluster App** is a modern automotive instrument cluster simulation built with Android and Jetpack Compose. It provides a customizable digital dashboard that can display vehicle information such as speed, RPM, driving modes, and various status indicators. The app uses MQTT and uProtocol for real-time communication, making it suitable for integration with vehicle data systems or simulators (CARLA).


## Features

    - Real-time Vehicle Data Display: Speed, // *RPM*, *gear position*, *and ambient temperature*
    - //Customizable Driving Modes: Switch between different driving modes (Race, Sport+, City)
    - Status Indicators: Cruise control, // battery status, lights, and warning indicators
    - Interactive Controls: // Increase/decrease speed,|  toggle cruise control, // and activate sensors
    - Multiple Display Screens: Switch between mode display, map view, and sensor information // just missing the screen but navigation already implemented
    - MQTT Communication: Real-time data exchange using MQTT and uProtocol // local server , mosquitto
    - // Vehicle Type Support: Displays appropriate gauges for combustion or electric vehicles


## Architecture

The app follows a modular clean architecture approach:

    UI Layer: Jetpack Compose UI components with a ViewModel
    Domain Layer: Business logic and models
    Data Layer: MQTT communication and repositories
    Core Modules: Design system, shared utilities, and common components


## Technology Stack
    
    Kotlin: Primary programming language
    Jetpack Compose: Modern UI toolkit for building native UI
    Hilt: Dependency injection
    Coroutines & Flow: Asynchronous programming and reactive streams
    MQTT: Message communication protocol
    uProtocol: Unified communication protocol for vehicle data


## Usage

### Control Board

    - Speed Control: Use the + and - buttons to increase or decrease speed // not functional
    - Cruise Control: Toggle cruise control with the cruise button // need to be tested
    - Driving Modes: Switch between different driving modes // just UI not functional
    - Sensor Views: Access different sensor information screens // just UI not functional 

## MQTT Messages

The app accepts MQTT messages in the following format:

    - speed=100 - Sets speed to 100
    - cruisecontrol=true - Enables cruise control

**Project Structure**

::
├── app/                          # Main application module
├── core/
│   ├── data/                     # Data layer with repositories and MQTT
│   ├── designsystem/             # UI design system and theming
│   └── domain/                   # Business logic and models
└── feature/
    └── cluster/                  # Cluster display feature
        ├── ui/                   # UI components
        │   ├── component/        # Reusable UI components
        │   └── screen/           # Full screens
        └── viewmodel/            # ViewModels for the feature