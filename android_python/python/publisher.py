import json
import time
import paho.mqtt.client as mqtt

# Load configuration
with open("mqtt_config.json") as f:
    config = json.load(f)

broker = config["broker"]
port = config["port"]
keepalive = config.get("keepalive", 60)
topic = config["topics"]["vehicle_parameters"]

# Load payload from data.json file
with open("data.json") as f:
    payload = json.load(f)
print(f"Loaded payload from data.json")

# Create MQTT client
client = mqtt.Client()

# Connect to broker
client.connect(broker, port, keepalive)

# Publish the payload
client.loop_start()  # Start network loop
client.publish(topic, json.dumps(payload))
print(f"Published to topic {topic}: {json.dumps(payload)}")
time.sleep(1)  # Wait to ensure message is sent
client.loop_stop()
client.disconnect()
