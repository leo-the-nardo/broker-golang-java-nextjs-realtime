package kafka

import ckafka "github.com/confluentinc/confluent-kafka-go/kafka"

type Producer struct {
	ConfigMap *ckafka.ConfigMap
}

func NewKafkaProducer(configMap *ckafka.ConfigMap) *Producer {
	return &Producer{
		ConfigMap: configMap,
	}
}

func (this *Producer) Publish(msg any, key []byte, topic string) error {
	producer, err := ckafka.NewProducer(this.ConfigMap)
	if err != nil {
		return err
	}
	message := &ckafka.Message{
		TopicPartition: ckafka.TopicPartition{
			Topic:     &topic,
			Partition: ckafka.PartitionAny, //kafka vai escolher a partição
		},
		Key:   key,
		Value: msg.([]byte),
	}
	err = producer.Produce(message, nil)
	if err != nil {
		return err
	}
	return nil
}
