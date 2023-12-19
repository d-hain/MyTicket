.PHONY: all build-all build-prod prod build-test test down

all: prod test

build-all: build-prod build-test

build-prod:
	cd production && docker-compose up --build -d

prod:
	cd production && docker-compose up -d

build-test:
	cd test && docker-compose up --build -d

test:
	cd test && docker-compose up -d

down:
	cd production && docker-compose down
	cd test && docker-compose down
