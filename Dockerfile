FROM alpine:latest

WORKDIR /api

COPY ./ /api

RUN apk add --no-cache openjdk21 git nodejs bash

RUN addgroup -S automaat && adduser -S automaat -G automaat && chown -R automaat:automaat /api

USER automaat

CMD ["./mvnw"]
