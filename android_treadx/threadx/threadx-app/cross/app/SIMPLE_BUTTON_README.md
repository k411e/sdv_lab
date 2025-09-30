# Simple Button MQTT Controller

This is a simplified version of the ThreadX application that reads button inputs and publishes MQTT messages.

## Features

- **Button A (PA4)**: Publishes brake signals to MQTT topic `vehicle/brake`
- **Button B (PA10)**: Toggles cruise control and publishes to MQTT topic `vehicle/cruise_control`
- **Display**: Shows current status including button press counts and cruise control state
- **MQTT**: Connects to MQTT broker and publishes button events

## Button Behavior

### Button A - Brake Signal
- When pressed: Publishes "BRAKE_PRESSED" to `vehicle/brake` topic
- Display shows "Braking..." while pressed
- Counter tracks total brake presses

### Button B - Cruise Control Toggle
- When pressed: Toggles cruise control state (ON/OFF)
- Publishes "ENABLED" or "DISABLED" to `vehicle/cruise_control` topic
- Display shows current cruise control status
- Counter tracks total cruise control toggles

## Configuration

Update the following constants in the code:
- `ssid`: WiFi network name
- `password`: WiFi password
- `broker_ip`: MQTT broker IP address (default: 5.196.78.28)

## MQTT Topics

- `vehicle/brake`: Receives brake press events
- `vehicle/cruise_control`: Receives cruise control state changes

## Display Information

The display shows:
- Brake press count
- Cruise control toggle count  
- Current cruise control state (ON/OFF)

## Building and Running

This uses the same build system as the original application. The simplified version removes:
- Temperature sensor functionality
- Complex vehicle parameter handling
- Measurement thread
- Queue-based communication

The application focuses purely on button input handling and MQTT publishing.
