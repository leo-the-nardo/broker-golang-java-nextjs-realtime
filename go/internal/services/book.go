package services

import (
	"container/heap"
	"fmt"
	"github.com/leo-the-nardo/internal/dto"
	"github.com/leo-the-nardo/internal/entity"
	"sync"
)

type Book struct {
	BuyOrdersQueueMap  map[string]*BuyOrderQueue
	SellOrdersQueueMap map[string]*SellOrderQueue
	OrdersChanIn       chan *dto.OrderInput //in (buy/sell)
	OrdersChanOut      chan *dto.OrderOutput
	Wg                 *sync.WaitGroup
}

func NewBook(orderChanIn chan *dto.OrderInput, orderChanOut chan *dto.OrderOutput, wg *sync.WaitGroup) *Book {
	return &Book{
		OrdersChanIn:       orderChanIn,
		OrdersChanOut:      orderChanOut,
		Wg:                 wg,
		SellOrdersQueueMap: make(map[string]*SellOrderQueue),
		BuyOrdersQueueMap:  make(map[string]*BuyOrderQueue),
	}
}

func (this *Book) Listen() {
	for orderDTO := range this.OrdersChanIn {
		order, errors := orderDTO.ToEntity()
		if errors != nil { //TODO: send to dead letter
			fmt.Println(errors)
			continue
		}

		buyAssetQueue := this.GetBuyOrderQueue(order.AssetID)
		sellAssetQueue := this.GetSellOrderQueue(order.AssetID)

		if orderDTO.OrderType == "BUY" {
			foundErrors := this.handleBuyOrder(order, buyAssetQueue, sellAssetQueue)
			if foundErrors != nil {
				fmt.Println(foundErrors) //TODO: send to dead letter
				continue
			}
		}
		if orderDTO.OrderType == "SELL" {
			foundErrors := this.handleSellOrder(order, buyAssetQueue, sellAssetQueue)
			if foundErrors != nil {
				fmt.Println(foundErrors) //TODO: send to dead letter
				continue
			}

		}
	}
}
func (this *Book) handleBuyOrder(theBuyOrder *entity.Order, buyAssetQueue *BuyOrderQueue, sellAssetQueue *SellOrderQueue) []error {
	if sellAssetQueue.IsEmpty() {
		buyAssetQueue.AddOrder(theBuyOrder)
		return nil
	}
	lowestSellPrice := sellAssetQueue.GetOrderWithLowestPrice().Price
	if theBuyOrder.Price >= lowestSellPrice {
		sellOrder := sellAssetQueue.TakeOutOrderWithLowestPrice()
		transaction, errors := entity.NewTransaction(sellOrder, theBuyOrder)
		if errors != nil {
			return errors
		}
		this.computeTransaction(transaction, this.Wg)
		sellerOutputDTO := dto.NewOrderOutput(sellOrder)
		buyerOutputDTO := dto.NewOrderOutput(theBuyOrder)
		this.OrdersChanOut <- sellerOutputDTO
		this.OrdersChanOut <- buyerOutputDTO
		if sellOrder.HasShares() {
			sellAssetQueue.AddOrder(sellOrder)
		}
	}
	if theBuyOrder.HasShares() {
		buyAssetQueue.AddOrder(theBuyOrder)
	}
	return nil
}
func (this *Book) handleSellOrder(theSellOrder *entity.Order, buyAssetQueue *BuyOrderQueue, sellAssetQueue *SellOrderQueue) []error {
	if buyAssetQueue.IsEmpty() {
		sellAssetQueue.AddOrder(theSellOrder)
		return nil
	}
	highestBuyPrice := buyAssetQueue.GetOrderWithHighestPrice().Price
	if highestBuyPrice >= theSellOrder.Price {
		buyOrder := buyAssetQueue.TakeOutOrderWithHighestPrice()
		transaction, errors := entity.NewTransaction(theSellOrder, buyOrder)
		if errors != nil {
			return errors
		}
		this.computeTransaction(transaction, this.Wg)
		sellerOutputDTO := dto.NewOrderOutput(theSellOrder)
		buyerOutputDTO := dto.NewOrderOutput(buyOrder)
		this.OrdersChanOut <- sellerOutputDTO
		this.OrdersChanOut <- buyerOutputDTO
		if buyOrder.HasShares() {
			buyAssetQueue.AddOrder(buyOrder)
		}
	}
	if theSellOrder.HasShares() {
		sellAssetQueue.AddOrder(theSellOrder)
	}
	return nil
}

func (this *Book) GetBuyOrderQueue(assetId string) *BuyOrderQueue {
	if this.BuyOrdersQueueMap[assetId] == nil { // queue not initialized
		this.BuyOrdersQueueMap[assetId] = NewBuyOrderQueue() //initialize queue
		heap.Init(this.BuyOrdersQueueMap[assetId])
	}
	return this.BuyOrdersQueueMap[assetId]
}
func (this *Book) GetSellOrderQueue(assetId string) *SellOrderQueue {
	if this.SellOrdersQueueMap[assetId] == nil { // queue not initialized
		this.SellOrdersQueueMap[assetId] = NewSellOrderQueue() //initialize queue
		heap.Init(this.SellOrdersQueueMap[assetId])
	}
	return this.SellOrdersQueueMap[assetId]
}

func (this *Book) computeTransaction(transaction *entity.Transaction, wg *sync.WaitGroup) {
	transaction.SellingOrder.RegisterTransaction(transaction)
	transaction.BuyingOrder.RegisterTransaction(transaction)
}
