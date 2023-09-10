package entity

type Investor struct {
	ID            string
	Name          string
	AssetPosition []*InvestorAssetPosition
}

func NewInvestor(id string) *Investor {
	return &Investor{
		ID:            id,
		AssetPosition: []*InvestorAssetPosition{},
	}
}
func (this *Investor) AddAssetPosition(assetPosition *InvestorAssetPosition) {
	this.AssetPosition = append(this.AssetPosition, assetPosition)
}
func (this *Investor) UpdateAssetPosition(assetId string, qtdShares int) {
	assetPosition := this.GetAssetPosition(assetId)
	if assetPosition == nil {
		this.AddAssetPosition(&InvestorAssetPosition{
			AssetID: assetId,
			Shares:  qtdShares,
		})
		return
	}
	assetPosition.Shares += qtdShares
}

func (this *Investor) GetAssetPosition(id string) *InvestorAssetPosition {
	for _, assetPosition := range this.AssetPosition {
		if assetPosition.AssetID == id {
			return assetPosition
		}
	}
	return nil
}

type InvestorAssetPosition struct { //ações
	AssetID string
	Shares  int
}

func NewInvestorAssetPosition(assetId string, qtdShares int) *InvestorAssetPosition {
	return &InvestorAssetPosition{
		AssetID: assetId,
		Shares:  qtdShares,
	}
}
