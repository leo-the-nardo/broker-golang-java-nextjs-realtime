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
	matchChanOut := make(chan *dto.MatchOutput, 10)
	var wg sync.WaitGroup

	book := services.NewBook(orderChanIn, matchChanOut, &wg)

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

	close(matchChanOut)

	// Validate the results if needed
	var outputs []*dto.MatchOutput
	for output := range matchChanOut {
		outputs = append(outputs, output)
	}
	assert.Equal(t, 1, len(outputs))
	matchDTO := outputs[0]
	matchedSellOrder := matchDTO.SellOrder
	matchedBuyOrder := matchDTO.BuyOrder

	assert.Equal(t, 9, matchedSellOrder.Shares)
	assert.Equal(t, 7, matchedSellOrder.Partial)
	assert.Equal(t, 1, len(matchedSellOrder.TransactionsOutput))
	assert.Equal(t, 69.0, matchedSellOrder.TransactionsOutput[0].Price)
	assert.Equal(t, matchedSellOrder.TransactionsOutput[0].BuyerID, matchedBuyOrder.InvestorID)

	assert.Equal(t, 2, matchedBuyOrder.Shares)
	assert.Equal(t, 0, matchedBuyOrder.Partial)
	assert.Equal(t, 1, len(matchedBuyOrder.TransactionsOutput))
	assert.Equal(t, 69.0, matchedBuyOrder.TransactionsOutput[0].Price)
	assert.Equal(t, matchedBuyOrder.TransactionsOutput[0].SellerID, matchedSellOrder.InvestorID)

}
