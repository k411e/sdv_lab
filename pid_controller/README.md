# PID Controller with Zenoh Pub/Sub in Python

This implements a PID (Proportional-Integral-Derivative) controller for velocity control of systems, using the Zenoh protocol for real-time communication via Pub/Sub.


## Description
The PID controller calculates the required acceleration to reach the desired velocity based on the current velocity, using the classical PID formula. The system receives current and desired velocitis through Zenoh topics, computes the acceleration, and publishes the result to another topic.


## Features

- Real-time PID controller using the standard control formula.
- Integration with the [Zenoh](https://zenoh.io/) publish/subscribe middleware.
- Configurable topics for input/output.
- Handles edge cases (e.g., zero delta time).
- Unit tests covering PID calculations and Zenoh integration (mocked).


## Requirements

- Python 3.10+
- Zenoh
- unittest for running tests


## Usage

1. Clone the repository:
    ```bash
    git clone git@github.com:The-Xverse/simulink-vecu.git 
    cd simulink-vecu/
    ```

2. Run the main code
    ```bash
    python3 pid_controller/main.py
    ```

3. Run a publisher for one (or both) the velocity topics (example)
    ```bash
    import zenoh, random, time

    def read_temp():
        return random.uniform(5.0, 10.0)

    if __name__ == "__main__":
        with zenoh.open(zenoh.Config()) as session:
            key = 'system/velocity/current'
            pub = session.declare_publisher(key)
            try:
                while True:
                    t = read_temp()
                    buf = f"{t}"
                    print(f"Putting Data ('{key}': '{buf}')...")
                    pub.put(buf)
                    time.sleep(5)
            except KeyboardInterrupt:
                pass
            finally:
                pub.undeclare()
                session.close()
    ```


## Tests

Run tests with:
    ```bash
    python3 -m unittest discover -s tests
    ```

For coverage report, use:
    ```bash
    pip install coverage
    coverage run -m unittest discover -s tests
    coverage report -m
    ```