# Getting Started

This service uses docker for development, tests and production images.

## How to use

First you must replace the value of `neows.apiKey` in `application.properties` with your api key, in place of `DEMO_KEY`.

TODO: Add support for, and/or test, reading this property from environment variables (or some other key management system) in the prod image, rather than setting it in a properties file.

### Development:
`docker-compose up` 
- Runs service in development mode
- Or optionally run `docker-compose up --build` to pickup new changes.

### Test
`docker build -t asteroid-service-tests --target test .` 
- Runs all unit tests

### Deploy
`docker build -t asteroid-service --target prod .`
 - Builds an image that can be deployed to production.

## TODO

- Add a pre-commit config that runs google java formatter on commit
- Add GitHub actions to run tests on creation of pull requests
- Extend actions to optionally allow deploying the service to AWS ECS

## Notes to interviewer:

I went with an in-memory caching approach, however quite late into the process I realised how slow the NASA API can be
(~18sec to load approach info for 1 date!) which limits the viability of this approach.

I have considered other approaches using the `/browse` endpoint to fetch and duplicate all data into a persistent 
database (relational or otherwise), which I hope we can discuss in person. My initial reasoning for avoiding this was to
avoid the complexity of having to maintain, and ensure consistency of, this data long term because the data provided by 
NASA can change. e.g. when a new asteroid is discovered.