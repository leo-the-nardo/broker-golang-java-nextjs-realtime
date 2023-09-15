package entity

import "errors"

type Order struct {
	ID            string
	InvestorID    string  //quem fez a ordem/ dono da ordem
	AssetID       string  //qual ação quer negociar
	Shares        int     //quantas cotas quer negociar
	PendingShares int     //quantas cotas ainda não foram negociadas/ ainda precisam ser negociadas
	Price         float64 //preço da ação
	OrderType     string  //buy | sell
	Status        string
	Transactions  []*Transaction //transações que essa ordem participou
}

func NewOrder(id string, investorID string, assetID string, shares int, price float64, orderType string) (*Order, []error) {
	order := &Order{
		ID:            id,
		InvestorID:    investorID,
		AssetID:       assetID,
		Shares:        shares,
		PendingShares: shares,
		Price:         price,
		OrderType:     orderType,
		Status:        "OPEN",
		Transactions:  []*Transaction{},
	}
	foundErrors := order.validate()
	if len(foundErrors) > 0 {
		return nil, foundErrors
	}
	return order, nil
}

func (this *Order) HasShares() bool {
	return this.PendingShares > 0
}

func (this *Order) RegisterTransaction(transaction *Transaction) {
	this.Transactions = append(this.Transactions, transaction)
}

func (this *Order) validate() []error {
	var foundErrors []error
	if this.PendingShares <= 0 {
		foundErrors = append(foundErrors, errors.New("pendingShares must be greater than 0"))
	}
	if this.Price <= 0 {
		foundErrors = append(foundErrors, errors.New("price must be greater than 0"))
	}
	if this.OrderType != "BUY" && this.OrderType != "SELL" {
		foundErrors = append(foundErrors, errors.New("orderType must be BUY or SELL"))
	}
	if this.AssetID == "" {
		foundErrors = append(foundErrors, errors.New("assetID cannot be empty"))
	}
	if this.InvestorID == "" {
		foundErrors = append(foundErrors, errors.New("investorID cannot be empty"))
	}
	if this.ID == "" {
		foundErrors = append(foundErrors, errors.New("id cannot be empty"))
	}
	if len(this.Transactions) > 0 {
		foundErrors = append(foundErrors, errors.New("transactions must be empty"))
	}
	return foundErrors
}
