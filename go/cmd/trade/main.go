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
	ordersOutChannel := make(chan *dto.OrderOutput)
	wg := &sync.WaitGroup{}
	defer wg.Wait() //wait for all goroutines to finish
	kafkaMsgChan := make(chan *ckafka.Message)
	configMap := &ckafka.ConfigMap{
		"bootstrap.servers": "host.docker.internal:9094",
		"group.id":          "myGroup",
		"auto.offset.reset": "latest",
	}
	producer := kafka.NewKafkaProducer(configMap)
	kafka := kafka.NewKafkaConsumer(configMap, []string{"input-orders"})
	go kafka.Consume(kafkaMsgChan)

	book := services.NewBook(ordersInChannel, ordersOutChannel, wg)
	go book.Listen()

	go func() {
		for msg := range kafkaMsgChan {
			wg.Add(1)
			orderInput := &dto.OrderInput{}
			err := json.Unmarshal(msg.Value, orderInput)
			fmt.Println(orderInput)
			if err != nil {
				panic(err)
			} // TODO: Send to dead letter
			ordersInChannel <- orderInput
		}
	}()
	for orderOutput := range ordersOutChannel {
		jsonOrder, err := json.MarshalIndent(orderOutput, "", "   ")
		fmt.Println(string(jsonOrder))
		if err != nil {
			fmt.Println(err)
		}
		err = producer.Publish(jsonOrder, []byte("orders"), "output-transactions")
		if err != nil {
			panic(err)
		}
	}
}
