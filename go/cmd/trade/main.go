package main

import (
	"encoding/json"
	"fmt"
	ckafka "github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/leo-the-nardo/internal/infra/kafka"
	"github.com/leo-the-nardo/internal/market/dto"
	"github.com/leo-the-nardo/internal/market/entity"
	"github.com/leo-the-nardo/internal/market/transformer"
	"sync"
)

func main() {
	// in: receive orders from kafka
	// out: send orders to kafka
	ordersInChannel := make(chan *entity.Order)
	ordersOutChannel := make(chan *entity.Order)
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
	go kafka.Consume(kafkaMsgChan) //T2F
	//recebe do canal do kafka , joga no input, processa, joga no output , publica pro kafka
	book := entity.NewBook(ordersInChannel, ordersOutChannel, wg)
	go book.Trade() //T3

	go func() {
		for msg := range kafkaMsgChan {
			wg.Add(1)
			fmt.Println(string(msg.Value))
			tradeInput := dto.TradeInput{}
			err := json.Unmarshal(msg.Value, &tradeInput)
			if err != nil {
				panic(err)
			}
			order := transformer.TransformInput(tradeInput)
			ordersInChannel <- order
		}
	}()
	for res := range ordersOutChannel {
		orderOutput := transformer.TransformOutput(res)
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
