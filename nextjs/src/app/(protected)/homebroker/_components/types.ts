//fetch
export type CandlestickDatum = {
  bucket: string
  open: number
  high: number
  low: number
  close: number
}

//fetch
export type AreaDatum = {
  time: string
  price: number
  symbol: string
}

//event data
export type AssetPointComputedEvent = {
  symbol: string
  price: number
  time: string
}
