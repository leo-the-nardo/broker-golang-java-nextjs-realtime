package entity

type Asset struct {
	ID           string
	Name         string
	MarketVolume int //quantas ações dessa empresa tem no mercado
}

func NewAsset(id string, name string, marketVolume int) *Asset {
	return &Asset{
		ID:           id,
		Name:         name,
		MarketVolume: marketVolume,
	}
}
