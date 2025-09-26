# MQTT Python Publisher & Subscriber

A Python-based MQTT implementation for publishing and subscribing to vehicle parameter data with intelligent CruiseControl status monitoring.

## 🚀 Features

- **JSON Data Exchange**: Publisher sends structured vehicle data, subscriber parses and displays it
- **CruiseControl Monitoring**: Smart detection and notification of CruiseControl state changes
- **Modular Configuration**: All settings centralized in `mqtt_config.json`
- **Error Handling**: Robust JSON parsing with graceful error handling
- **Real-time Communication**: Continuous listening and publishing capabilities

## 📋 Prerequisites

1. **Python 3.x** installed on your system
2. **MQTT Broker** (e.g., [Mosquitto](https://mosquitto.org/))
3. **Required Python packages** (see Installation section)

## 🛠️ Installation

1. **Install dependencies**:
   ```bash
   pip install -r requirements.txt
   ```

2. **Set up MQTT Broker** (Mosquitto example):
   ```bash
   # Ubuntu/Debian
   sudo apt install mosquitto
   sudo systemctl enable mosquitto
   sudo systemctl start mosquitto
   
   # Windows (using Chocolatey)
   choco install mosquitto
   
   # macOS (using Homebrew)
   brew install mosquitto
   ```

## 📁 Project Structure

```
PythonMQTT/
├── mqtt_config.json      # Configuration file
├── publisher.py          # MQTT publisher script
├── subscriber.py         # MQTT subscriber script
├── requirements.txt      # Python dependencies
├── mqtt_python_setup.md  # Detailed setup documentation
└── README.md            # This file
```

## ⚙️ Configuration

The `mqtt_config.json` file contains all MQTT settings:

```json
{
  "broker": "localhost",
  "port": 1883,
  "keepalive": 60,
  "topics": {
    "vehicle_parameters": "vehicle/parameters"
  }
}
```

### Configuration Fields

| Field     | Description                           | Default |
|-----------|---------------------------------------|---------|
| broker    | MQTT broker address                   | localhost |
| port      | Broker port number                    | 1883 |
| keepalive | Keepalive interval in seconds         | 60 |
| topics    | Dictionary of topic names             | vehicle/parameters |

## 🚗 Vehicle Data Format

The publisher sends JSON data with the following structure:

```json
{
  "AmbientTemperature": 22,
  "Battery": 80,
  "CruiseControl": false,
  "Economy": "Normal",
  "Engine Temperature": 90,
  "Gear": "P",
  "RPM": 0.0,
  "Range": 320,
  "ShareLocation": false,
  "Speed": 0,
  "SpeedUnit": "km/h",
  "TemperatureUnit": 0,
  "TypeOfVehicle": 0
}
```

## 🎯 Usage

### 1. Start the MQTT Broker

Make sure your MQTT broker is running:

```bash
# Check if Mosquitto is running
sudo systemctl status mosquitto

# Start if not running
sudo systemctl start mosquitto
```

### 2. Run the Subscriber

Start the subscriber to listen for messages:

```bash
python subscriber.py
```

**Expected Output**:
```
Subscribed to topic: vehicle/parameters
Received message on vehicle/parameters: {
  "AmbientTemperature": 22,
  "Battery": 80,
  "CruiseControl": false,
  ...
}
```

### 3. Run the Publisher

In a separate terminal, run the publisher:

```bash
python publisher.py
```

**Expected Output**:
```
Published to topic vehicle/parameters: {"AmbientTemperature": 22, "Battery": 80, ...}
```

## 🎛️ CruiseControl Monitoring

The subscriber includes intelligent CruiseControl monitoring:

- **When CruiseControl activates**: `Cruise Control active. Speed: {speed}`
- **When CruiseControl deactivates**: `Cruise Control deactivated.`
- **No spam**: Only prints messages when state actually changes

### Example CruiseControl Output

```
Received message on vehicle/parameters: { ... "CruiseControl": true, "Speed": 65 ... }
Cruise Control active. Speed: 65

Received message on vehicle/parameters: { ... "CruiseControl": false, "Speed": 0 ... }
Cruise Control deactivated.
```

## 🔧 Customization

### Adding More Topics

Edit `mqtt_config.json` to add additional topics:

```json
{
  "broker": "localhost",
  "port": 1883,
  "keepalive": 60,
  "topics": {
    "vehicle_parameters": "vehicle/parameters",
    "engine_status": "vehicle/engine",
    "location_data": "vehicle/location"
  }
}
```

### Modifying Vehicle Data

Edit the `payload` dictionary in `publisher.py`:

```python
payload = {
    "AmbientTemperature": 25,
    "Battery": 85,
    "CruiseControl": True,
    "Speed": 70,
    # Add your custom fields here
}
```

### Continuous Publishing

For continuous data publishing, modify `publisher.py`:

```python
while True:
    client.publish(topic, json.dumps(payload))
    time.sleep(5)  # Publish every 5 seconds
```

## 🐛 Troubleshooting

### Common Issues

1. **Connection Refused**:
   - Ensure MQTT broker is running
   - Check broker address and port in `mqtt_config.json`

2. **Permission Denied (GitHub)**:
   - Create repository on GitHub first
   - Set up SSH keys or use HTTPS authentication

3. **Import Errors**:
   - Install dependencies: `pip install -r requirements.txt`

4. **JSON Decode Errors**:
   - Check if publisher is sending valid JSON
   - Verify topic names match between publisher and subscriber

### Debug Mode

Add debug logging to see detailed MQTT communication:

```python
import logging
logging.basicConfig(level=logging.DEBUG)
```

## 📚 Dependencies

- `paho-mqtt==1.6.1` - MQTT client library for Python

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m "Add feature"`
4. Push to branch: `git push origin feature-name`
5. Submit a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 📞 Support

For questions or issues:
- Create an issue on GitHub
- Check the detailed documentation in `mqtt_python_setup.md`

---

**Happy MQTT-ing! 🚀**
