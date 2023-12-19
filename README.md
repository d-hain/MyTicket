# MyTicket

A Java Spring project which was an assignement from school.

## How to run

### Databases

This uses `docker-compose` so you should have that installed.\
List of all targets:

- Run production and the test database
```shell
make all
```

- Stop both databases
```shell
make down
```

- Only run production or the test database
`make prod` or `make test`

- Build both databases
```shell
make build-all
```

- Build each
`make build-prod` or `make build-test`

### Project

Make sure the database is started.\
Run the `MyTicketApplication` in IntelliJ IDEA.

(I have no idea how else one starts a Spring application)
