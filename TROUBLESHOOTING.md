# Troubleshooting

## Docker build is slow

- The backend images compile the selected Maven module inside Docker.
- The first build is the slowest because Maven dependencies are downloaded into the image layer cache.

## A MySQL container will not start

- Check if the host port is already in use.
- `user-service-db` uses host port `3312`.
- Product through notification databases use `3307` through `3311`.

## A service cannot fetch config

- Verify `config-server` is healthy:

```bash
curl -u admin:admin http://localhost:8888/actuator/health
```

## A service is missing from Eureka

- Verify discovery health:

```bash
curl -u admin:admin http://localhost:8761/actuator/health
```

- Then inspect logs:

```bash
docker compose logs discovery-server config-server api-gateway user-service
```

## Frontend cannot reach the backend

- Confirm gateway health:

```bash
curl http://localhost:8080/actuator/health
```

- Confirm frontend health:

```bash
curl http://localhost:4200
```
