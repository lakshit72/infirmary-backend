# UPES UHS Portal Backend

This is the backend of the UPES UHS (University Health Services) Portal, built using **Spring** and connected to a **PostgreSQL** database. The backend is containerized using **Docker** and can be easily run with **Docker Compose**. Once set up, the server will be available at **http://localhost:8081** and will accept connections.

## Prerequisites

Before getting started, ensure that you have the following installed on your system:

- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)

## Getting Started

Follow these steps to get the project up and running on your local machine.

First, clone the repository to your local machine:

```bash
git clone https://github.com/lakshit72/infirmary-backend.git
cd infirmary-backend
```

Add the email password to application.properties file to the spring env variable

Then build and run the container:

```bash
docker-compose build
docker-compose up
```

The server is live at:

```bash
http://locahost:8081
```

### IMPORTANT MESSAGES

Please note that "ad" is now referred to as "nursing assistant" on the frontend of the application. This update reflects a change in terminology to better represent the functionality and user experience.
