package dto

type AssetPoint struct {
	Symbol string  `json:"symbol"`
	Price  float64 `json:"price"`
	Shares int     `json:"shares"`
}

func NewAssetPoint(symbol string, price float64, shares int) *AssetPoint {
	return &AssetPoint{
		Symbol: symbol,
		Price:  price,
		Shares: shares,
	}
}
