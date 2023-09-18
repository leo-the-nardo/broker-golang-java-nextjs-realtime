export type Asset = {
  id: string
  symbol: string
  price: string
}

export type WalletAsset = {
  id: string
  wallet_id: string
  asset_id: string
  shares: string
  Asset: Asset
}
export type AssetsOnWalletResponse = {
  assetId: string
  shares: number
}[]
export type AssetOnWalletUpdatedEvent = {
  assetId: string
  shares: string
  preTakenShares: string
}[]
export type Order = {
  id: string
  type: "BUY" | "SELL"
  assetId: string
  price: number
  shares: number
  status: "FULFILLED" | "PENDING" | "OPEN" | "CLOSED" | "FAILED" | "PARTIAL"
  partial: number
  createdAt: string
  updatedAt: string | null
}
export type WalletOrdersResponse = {
  orders: Order[]
}
export type OrderUpdatedEvent = {
  orderId: string
  assetId: string
  shares: number
  negotiatedShares: number
  negotiatedPrice: number
  expectedPrice: number
  type: "BUY" | "SELL"
  status: "FULFILLED" | "PENDING" | "OPEN" | "CLOSED" | "FAILED" | "PARTIAL"
}

export type AssetsInfo = {
  assetId: string
  shares: number
  name: string
}
export type AssetsInfoInResponse = {
  symbol: string
  name: string
}[]
export type AssetsInfoResponse = {
  content: {
    symbol: string
    name: string
  }[]
  pageable: {
    pageNumber: number
    pageSize: number
    sort: {
      empty: boolean
      sorted: boolean
      unsorted: boolean
    }
    offset: number
    paged: boolean
    unpaged: boolean
  }
  last: boolean
  totalPages: number
  totalElements: number
  size: number
  number: number
  sort: {
    empty: boolean
    sorted: boolean
    unsorted: boolean
  }
  first: boolean
  numberOfElements: number
  empty: boolean
}
