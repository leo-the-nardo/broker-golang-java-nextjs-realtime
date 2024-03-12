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
	MatchChanOut       chan *dto.MatchOutput
	Wg                 *sync.WaitGroup
}

func NewBook(orderChanIn chan *dto.OrderInput, matchChanOut chan *dto.MatchOutput, wg *sync.WaitGroup) *Book {
	return &Book{
		OrdersChanIn:       orderChanIn,
		MatchChanOut:       matchChanOut,
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

		buyOrderMatch, sellOrderMatch, hasMatch := matchOrAddToQueue(order, buyAssetQueue, sellAssetQueue)
		if !hasMatch {
			continue
		}

		transaction, errors := entity.NewTransaction(sellOrderMatch, buyOrderMatch)
		if errors != nil {
			fmt.Println(errors) //TODO: send to dead letter
			continue
		}

		matchOutput := dto.NewMatchOutput(buyOrderMatch, sellOrderMatch, transaction)
		this.MatchChanOut <- matchOutput

		if buyOrderMatch.HasShares() {
			buyAssetQueue.AddOrder(buyOrderMatch)
		}
		if sellOrderMatch.HasShares() {
			sellAssetQueue.AddOrder(sellOrderMatch)
		}
	}

}

func matchOrAddToQueue(order *entity.Order, buyAssetQueue *BuyOrderQueue, sellAssetQueue *SellOrderQueue) (matchBuyOrder *entity.Order, matchSellOrder *entity.Order, hasMatch bool) {
	var buyOrder *entity.Order
	var sellOrder *entity.Order

	if order.IsBuyOrder() {
		buyOrder = order
		if sellAssetQueue.IsEmpty() {
			buyAssetQueue.AddOrder(buyOrder)
			return nil, nil, false
		}
		lowestSellOrder := sellAssetQueue.GetOrderWithLowestPrice()
		lowestSellPrice := lowestSellOrder.Price
		if buyOrder.Price < lowestSellPrice || buyOrder.InvestorID == lowestSellOrder.InvestorID {
			buyAssetQueue.AddOrder(buyOrder)
			return nil, nil, false
		}
		sellOrder = sellAssetQueue.TakeOutOrderWithLowestPrice()
	}
	if order.IsSellOrder() {
		sellOrder = order
		if buyAssetQueue.IsEmpty() {
			sellAssetQueue.AddOrder(sellOrder)
			return nil, nil, false
		}
		highestBuyOrder := buyAssetQueue.GetOrderWithHighestPrice()
		highestBuyPrice := highestBuyOrder.Price
		if sellOrder.Price > highestBuyPrice || sellOrder.InvestorID == highestBuyOrder.InvestorID {
			sellAssetQueue.AddOrder(sellOrder)
			return nil, nil, false
		}
		buyOrder = buyAssetQueue.TakeOutOrderWithHighestPrice()
	}
	return buyOrder, sellOrder, true
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
