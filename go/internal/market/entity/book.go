package entity

import (
	"container/heap"
	"sync"
)

type Book struct {
	Orders        []*Order
	Transactions  []*Transaction
	OrdersChan    chan *Order //in (buy/sell)
	OrdersChanOut chan *Order
	Wg            *sync.WaitGroup
}

func NewBook(orderChan chan *Order, orderChanOut chan *Order, wg *sync.WaitGroup) *Book {
	return &Book{
		Orders:        []*Order{},
		Transactions:  []*Transaction{},
		OrdersChan:    orderChan,
		OrdersChanOut: orderChanOut,
		Wg:            wg,
	}
}

// Trade TODO: Refactor abstractions
func (this *Book) Trade() {
	buyOrdersQueueMap := make(map[string]*OrderQueue)  //assetId -> queue
	sellOrdersQueueMap := make(map[string]*OrderQueue) //assetId -> queue
	//heap.Init(buyOrdersQueueMap)
	//heap.Init(sellOrdersQueueMap)

	for order := range this.OrdersChan {
		assetId := order.Asset.ID
		if buyOrdersQueueMap[assetId] == nil { // queue not initialized
			buyOrdersQueueMap[assetId] = NewOrderQueue() //initialize queue
			heap.Init(buyOrdersQueueMap[assetId])
		}
		if sellOrdersQueueMap[assetId] == nil { // queue not initialized
			sellOrdersQueueMap[assetId] = NewOrderQueue() //initialize queue
			heap.Init(sellOrdersQueueMap[assetId])
		}

		if order.OrderType == "BUY" {
			buyOrder := order
			buyOrdersQueueMap[assetId].Push(buyOrder)
			if sellOrdersQueueMap[assetId].Len() > 0 && sellOrdersQueueMap[assetId].Orders[0].Price <= buyOrder.Price {
				sellOrder := sellOrdersQueueMap[assetId].Pop().(*Order)
				if sellOrder.PendingShares > 0 { //sellOrder still has shares to sell
					transaction := NewTransaction(sellOrder, buyOrder, buyOrder.Shares, sellOrder.Price)
					this.AddTransaction(transaction, this.Wg)
					sellOrder.Transactions = append(sellOrder.Transactions, transaction)
					buyOrder.Transactions = append(buyOrder.Transactions, transaction)
					this.OrdersChanOut <- sellOrder
					this.OrdersChanOut <- buyOrder
					if sellOrder.PendingShares > 0 { //sellOrder still has shares to sell
						sellOrdersQueueMap[assetId].Push(sellOrder) //push back to queue
					}
				}

			}
		}
		if order.OrderType == "SELL" {
			sellOrder := order
			sellOrdersQueueMap[assetId].Push(sellOrder)
			if buyOrdersQueueMap[assetId].Len() > 0 && buyOrdersQueueMap[assetId].Orders[0].Price >= sellOrder.Price {
				buyOrder := buyOrdersQueueMap[assetId].Pop().(*Order)
				if buyOrder.PendingShares > 0 { //buyOrder still has shares to buy
					transaction := NewTransaction(sellOrder, buyOrder, buyOrder.Shares, sellOrder.Price)
					this.AddTransaction(transaction, this.Wg)
					buyOrder.Transactions = append(buyOrder.Transactions, transaction)
					sellOrder.Transactions = append(sellOrder.Transactions, transaction)
					this.OrdersChanOut <- sellOrder
					this.OrdersChanOut <- buyOrder
					if buyOrder.PendingShares > 0 { //buyOrder still has shares to buy
						buyOrdersQueueMap[assetId].Push(buyOrder) //push back to queue
					}

				}
			}

		}
	}
}

func (this *Book) AddTransaction(transaction *Transaction, wg *sync.WaitGroup) {
	defer wg.Done()
	sellingShares := transaction.SellingOrder.PendingShares
	buyingShares := transaction.BuyingOrder.PendingShares
	minShares := sellingShares
	if buyingShares < sellingShares {
		minShares = buyingShares
	}
	transaction.SellingOrder.Investor.UpdateAssetPosition(transaction.SellingOrder.Asset.ID, -minShares)
	transaction.AddSellOrderPendingShares(-minShares)
	transaction.BuyingOrder.Investor.UpdateAssetPosition(transaction.BuyingOrder.Asset.ID, minShares)
	transaction.AddBuyOrderPendingShares(-minShares)
	transaction.CalculateTotal(transaction.Shares, transaction.BuyingOrder.Price)
	transaction.CloseBuyOrder()
	transaction.CloseSellOrder()
	this.Transactions = append(this.Transactions, transaction)
}
