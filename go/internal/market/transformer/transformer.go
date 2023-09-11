package transformer

import (
	"github.com/leo-the-nardo/internal/market/dto"
	"github.com/leo-the-nardo/internal/market/entity"
)

func TransformInput(input dto.TradeInput) *entity.Order { //hydrate
	asset := entity.NewAsset(input.AssetID, input.AssetID, 1000)
	investor := entity.NewInvestor(input.InvestorID)
	order := entity.NewOrder(input.OrderID, investor, asset, input.Shares, input.Price, input.OrderType)
	if input.CurrentShares > 0 {
		investorAssetPosition := entity.NewInvestorAssetPosition(input.AssetID, input.CurrentShares)
		investor.AddAssetPosition(investorAssetPosition)
	}
	return order
}

func TransformOutput(order *entity.Order) *dto.OrderOutput { //dehydrate
	orderOutput := dto.OrderOutput{
		OrderID:    order.ID,
		InvestorID: order.Investor.ID,
		AssetID:    order.Asset.ID,
		OrderType:  order.OrderType,
		Status:     order.Status,
		Partial:    order.PendingShares,
		Shares:     order.Shares,
	}
	for _, transaction := range order.Transactions {
		transactionOutput := dto.TransactionOutput{
			TransactionID: transaction.ID,
			BuyerID:       transaction.BuyingOrder.Investor.ID,
			SellerID:      transaction.SellingOrder.Investor.ID,
			AssetID:       transaction.BuyingOrder.Asset.ID,
			Price:         transaction.Price,
			Shares:        transaction.SellingOrder.Shares - transaction.SellingOrder.PendingShares,
		}
		orderOutput.TransactionOutput = append(orderOutput.TransactionOutput, transactionOutput)
	}
	return &orderOutput
}
