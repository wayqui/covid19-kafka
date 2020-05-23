# Covid 19 api Kafka

## Introduction

## Modules and components

### Kafka cluster, zookeeper and schema registry 


```Shell
kafka-topics --topic covid-daily-stats --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-accumulate-stats --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-confirmed-per-country --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-recovered-per-country --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-dead-per-country --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-confirmed-per-day --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-recovered-per-day --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
kafka-topics --topic covid-dead-per-day --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1
```

### Project Modules

#### covid19-statistics-producer
#### covid19-statistics-consumer
#### covid19-avro-schemas
#### common-library

### Producer Api client

## Configuration

## How to start

## To-do list

* Using a Kafka connect consumer sink for populating the MongoDB
* Configure manual retries and recovery of the messages for the consumer.
* Unit test and integration tests 
* Add Swagger-UI api documentation and generate a client.
* Generate Id for message (in order to avoid duplicates)
