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
	buyOrdersQueue := NewOrderQueue()
	sellOrdersQueue := NewOrderQueue()
	heap.Init(buyOrdersQueue)
	heap.Init(sellOrdersQueue)

	for order := range this.OrdersChan {
		if order.OrderType == "BUY" {
			buyOrder := order
			buyOrdersQueue.Push(buyOrder)
			if sellOrdersQueue.Len() > 0 && sellOrdersQueue.Orders[0].Price <= buyOrder.Price {
				sellOrder := sellOrdersQueue.Pop().(*Order)
				if sellOrder.PendingShares > 0 { //sellOrder still has shares to sell
					transaction := NewTransaction(sellOrder, buyOrder, buyOrder.Shares, sellOrder.Price)
					this.AddTransaction(transaction, this.Wg)
					sellOrder.Transactions = append(sellOrder.Transactions, transaction)
					buyOrder.Transactions = append(buyOrder.Transactions, transaction)
					this.OrdersChanOut <- sellOrder
					this.OrdersChanOut <- buyOrder
					if sellOrder.PendingShares > 0 { //sellOrder still has shares to sell
						sellOrdersQueue.Push(sellOrder) //push back to queue
					}
				}

			}
		} else if order.OrderType == "SELL" {
			sellOrder := order
			sellOrdersQueue.Push(sellOrder)
			if buyOrdersQueue.Len() > 0 && buyOrdersQueue.Orders[0].Price >= sellOrder.Price {
				buyOrder := buyOrdersQueue.Pop().(*Order)
				if buyOrder.PendingShares > 0 { //buyOrder still has shares to buy
					transaction := NewTransaction(sellOrder, buyOrder, buyOrder.Shares, sellOrder.Price)
					this.AddTransaction(transaction, this.Wg)
					buyOrder.Transactions = append(buyOrder.Transactions, transaction)
					sellOrder.Transactions = append(sellOrder.Transactions, transaction)
					this.OrdersChanOut <- sellOrder
					this.OrdersChanOut <- buyOrder
					if buyOrder.PendingShares > 0 { //buyOrder still has shares to buy
						buyOrdersQueue.Push(buyOrder) //push back to queue
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
	transaction.SellingOrder.PendingShares -= minShares
	transaction.BuyingOrder.Investor.UpdateAssetPosition(transaction.BuyingOrder.Asset.ID, minShares)
	transaction.BuyingOrder.PendingShares -= minShares
	transaction.Total = float64(transaction.Shares) * transaction.BuyingOrder.Price
	if transaction.BuyingOrder.PendingShares == 0 {
		transaction.BuyingOrder.Status = "CLOSED"
	}
	if transaction.SellingOrder.PendingShares == 0 {
		transaction.SellingOrder.Status = "CLOSED"
	}
	this.Transactions = append(this.Transactions, transaction)
}
