package main

import (
	"encoding/json"
	"fmt"
	ckafka "github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/leo-the-nardo/internal/dto"
	"github.com/leo-the-nardo/internal/infra/kafka"
	"github.com/leo-the-nardo/internal/services"
	"sync"
)

func main() {
	// in: receive orders from kafka
	// out: send matched orders w/ transactions to kafka
	ordersInChannel := make(chan *dto.OrderInput)
	matchOutChannel := make(chan *dto.MatchOutput)
	wg := &sync.WaitGroup{}
	defer wg.Wait() // wait for all goroutines
	kafkaMsgChan := make(chan *ckafka.Message)
	configMap := &ckafka.ConfigMap{
		"bootstrap.servers": "host.docker.internal:9094",
		"group.id":          "myGroup",
		"auto.offset.reset": "latest",
	}
	producer := kafka.NewKafkaProducer(configMap)
	kafka := kafka.NewKafkaConsumer(configMap, []string{"input-orders"})
	go kafka.Consume(kafkaMsgChan)

	book := services.NewBook(ordersInChannel, matchOutChannel, wg)
	go book.Listen()

	go func() {
		for msg := range kafkaMsgChan {
			orderInput := &dto.OrderInput{}
			err := json.Unmarshal(msg.Value, orderInput)
			fmt.Println(orderInput)
			if err != nil {
				panic(err)
			} // TODO: Send to dead letter
			ordersInChannel <- orderInput
		}
	}()
	for matchOutput := range matchOutChannel {
		jsonOrder, err := json.MarshalIndent(matchOutput, "", "   ")
		fmt.Println(string(jsonOrder))
		if err != nil { //TODO: send to dead letter
			fmt.Println(err)
		}
		err = producer.Publish(jsonOrder, []byte("orders"), "match")
		if err != nil {
			panic(err)
		}
	}
}
