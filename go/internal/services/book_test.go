package services_test

import (
	"fmt"
	"github.com/leo-the-nardo/internal/dto"
	"github.com/leo-the-nardo/internal/services"
	"github.com/stretchr/testify/assert"
	"sync"
	"testing"
	"time"
)

func TestBook_HandleOrders(t *testing.T) {
	orderChanIn := make(chan *dto.OrderInput, 10)
	orderChanOut := make(chan *dto.OrderOutput, 10)
	var wg sync.WaitGroup

	book := services.NewBook(orderChanIn, orderChanOut, &wg)

	// Start listening for orders in a goroutine
	go book.Listen()

	// Add 5 distinct buy orders with the same assetID
	assetID := "ABC"

	//index 1 buy should match index 3 sell
	buyShares := []int{1, 2, 3, 4, 5}
	buyPrices := []float64{65.0, 76, 60.0, 69.0, 70.0, 65.0}

	for i := 0; i < 5; i++ {
		order := &dto.OrderInput{
			OrderID:       fmt.Sprintf("BuyOrder%d", i+1),
			InvestorID:    fmt.Sprintf("Investor%d", i+1),
			AssetID:       assetID,
			CurrentShares: 100,
			Shares:        buyShares[i],
			Price:         buyPrices[i],
			OrderType:     "BUY",
		}
		orderChanIn <- order
		time.Sleep(100 * time.Millisecond)
	}
	time.Sleep(100 * time.Millisecond)
	buyQueue := book.GetBuyOrderQueue(assetID)
	sellQueue := book.GetSellOrderQueue(assetID)
	assert.Equal(t, 5, len(buyQueue.Orders))
	assert.Equal(t, 0, len(sellQueue.Orders))
	assert.Equal(t, buyQueue.Orders[0].Price, 76.0)
	assert.Equal(t, buyQueue.Orders[0].PendingShares, 2)

	// Add 5 distinct sell orders for the same assetID, ensuring only one match
	//index 1 buy should match index 3 sell
	sellShares := []int{6, 7, 8, 9, 10}
	sellPrices := []float64{200.0, 300.0, 290.0, 69.0, 500.0}
	for i := 0; i < 5; i++ {
		order := &dto.OrderInput{
			OrderID:       fmt.Sprintf("SellOrder%d", i+1),
			InvestorID:    fmt.Sprintf("Investor%d", i+6),
			AssetID:       assetID,
			CurrentShares: 50,
			Shares:        sellShares[i],
			Price:         sellPrices[i],
			OrderType:     "SELL",
		}
		orderChanIn <- order
		time.Sleep(100 * time.Millisecond)

	}
	time.Sleep(100 * time.Millisecond)
	// buy 2 shares a 76 should match sell of 9 shares at 69
	assert.Equal(t, 4, len(buyQueue.Orders))              // 1 buy was completely filled
	assert.Equal(t, 5, len(sellQueue.Orders))             // 1 sell was partially filled
	assert.Equal(t, 7, sellQueue.Orders[0].PendingShares) // sold 2 shares of 9
	close(orderChanIn)

	// Wait for all processing to finish
	wg.Wait()

	close(orderChanOut)

	// Validate the results if needed
	var outputs []*dto.OrderOutput
	for output := range orderChanOut {
		outputs = append(outputs, output)
	}
	assert.Equal(t, 2, len(outputs))
	sellerOrder := outputs[0]
	buyerOrder := outputs[1]
	assert.Equal(t, 9, sellerOrder.Shares)
	assert.Equal(t, 7, sellerOrder.Partial)
	assert.Equal(t, 1, len(sellerOrder.TransactionsOutput))
	assert.Equal(t, 69.0, sellerOrder.TransactionsOutput[0].Price)
	assert.Equal(t, sellerOrder.TransactionsOutput[0].BuyerID, buyerOrder.InvestorID)

	assert.Equal(t, 2, buyerOrder.Shares)
	assert.Equal(t, 0, buyerOrder.Partial)
	assert.Equal(t, 1, len(buyerOrder.TransactionsOutput))

	assert.Equal(t, 69.0, buyerOrder.TransactionsOutput[0].Price)
	assert.Equal(t, buyerOrder.TransactionsOutput[0].SellerID, sellerOrder.InvestorID)

}
