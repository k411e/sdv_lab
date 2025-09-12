# Use the official Rust image
FROM docker.io/library/rust:latest

# Install Git
RUN apt-get update && apt-get install -y git cmake

# Set working directory
WORKDIR /app

# Clone your Rust project from GitHub (replace with your repo)
RUN git clone --recurse-submodules https://github.com/eclipse-uprotocol/up-transport-mqtt5-rust.git .

# Build the project
RUN cargo build

# Run the binary (replace with your actual binary name)
CMD ["cargo", "run", "--example", "publisher_example"]