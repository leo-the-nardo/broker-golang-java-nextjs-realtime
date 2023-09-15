package services

import (
	"container/heap"
	"github.com/leo-the-nardo/internal/entity"
)

type SellOrderQueue struct {
	Orders []*entity.Order
}

func NewSellOrderQueue() *SellOrderQueue {
	return &SellOrderQueue{
		Orders: []*entity.Order{},
	}
}

func (this *SellOrderQueue) AddOrder(order *entity.Order) {
	heap.Push(this, order)
}

func (this *SellOrderQueue) GetOrderWithLowestPrice() *entity.Order {
	return this.Orders[0]
}
func (this *SellOrderQueue) TakeOutOrderWithLowestPrice() *entity.Order {
	return heap.Pop(this).(*entity.Order)

}

func (this *SellOrderQueue) IsEmpty() bool {
	return len(this.Orders) <= 0
}

// interface implementations ---------------------------------------------

func (this *SellOrderQueue) Less(i, j int) bool { //heap.Interface boilerplate
	return this.Orders[i].Price < this.Orders[j].Price // The lowest price is the first to be executed
}

func (this *SellOrderQueue) Swap(i, j int) { //heap.Interface boilerplate
	this.Orders[i], this.Orders[j] = this.Orders[j], this.Orders[i] //swap order positions
}

func (this *SellOrderQueue) Len() int { //heap.Interface boilerplate
	return len(this.Orders)
}

func (this *SellOrderQueue) Push(x any) { //heap.Interface boilerplate
	this.Orders = append(this.Orders, x.(*entity.Order))
}

func (this *SellOrderQueue) Pop() any { //heap.Interface boilerplate
	old := this.Orders         //old slice
	n := len(old)              //last element index
	x := old[n-1]              //last element
	this.Orders = old[0 : n-1] //remove last element
	return x
}
