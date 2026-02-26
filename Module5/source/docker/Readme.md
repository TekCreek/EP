
```bash

# Navigate to the docker directory
# Use docker compose to start the ActiveMQ broker

docker compose up -d

# Check the logs to ensure the broker is running
docker compose logs -f

# Open browser and navigate to http://localhost:8161/admin/ to access the ActiveMQ web console
# Use the default credentials (admin/admin) to log in
```