package entity

type Order struct {
	ID            string
	Investor      *Investor //quem fez a ordem/ dono da ordem
	Asset         *Asset    //qual ação quer negociar
	Shares        int       //quantas cotas quer negociar
	PendingShares int       //quantas cotas ainda não foram negociadas
	Price         float64   //preço da ação
	OrderType     string    //buy | sell
	Status        string
	Transactions  []*Transaction
}

func NewOrder(id string, investor *Investor, asset *Asset, shares int, price float64, orderType string) *Order {
	return &Order{
		ID:            id,
		Investor:      investor,
		Asset:         asset,
		Shares:        shares,
		PendingShares: shares,
		Price:         price,
		OrderType:     orderType,
		Status:        "OPEN",
		Transactions:  []*Transaction{},
	}
}
