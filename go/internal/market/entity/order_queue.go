package entity

type OrderQueue struct {
	Orders []*Order
}

func (this *OrderQueue) Less(i, j int) bool { //heap.Interface boilerplate
	return this.Orders[i].Price < this.Orders[j].Price
}

func (this *OrderQueue) Swap(i, j int) { //heap.Interface boilerplate
	this.Orders[i], this.Orders[j] = this.Orders[j], this.Orders[i] //swap order positions
}

func (this *OrderQueue) Len() int { //heap.Interface boilerplate
	return len(this.Orders)
}

func (this *OrderQueue) Push(x any) { //heap.Interface boilerplate
	this.Orders = append(this.Orders, x.(*Order))
}

func (this *OrderQueue) Pop() any { //heap.Interface boilerplate
	old := this.Orders         //old slice
	n := len(old)              //last element index
	x := old[n-1]              //last element
	this.Orders = old[0 : n-1] //remove last element
	return x
}

func NewOrderQueue() *OrderQueue {
	return &OrderQueue{
		Orders: []*Order{},
	}
}
