"use client"
import { createContext, useContext, useEffect, useState } from "react"
import useSWR from "swr"
import { AssetsInfo, AssetsOnWalletResponse } from "@/app/models"
import { fetcherClient } from "@/lib/client-fetcher"
import useSWRImmutable from "swr/immutable"
import { API_URL } from "@/routes"
// import assets from "@/lib/assets.json"
type WalletContextProps = {
  wallet: AssetsInfo[]
  updateWallet: (updateProps: {
    assetId: string
    sharesToCompute: number
  }) => Promise<void>
  isLoading: boolean
}
const WalletContext = createContext<WalletContextProps>({
  wallet: [
    {
      symbol: "",
      shares: 0,
    },
  ],
  updateWallet: async () => {},
  isLoading: true,
})

export const WalletContextProvider = ({
  children,
}: {
  children: React.ReactNode
}) => {
  const {
    data: walletAssetsFetch,
    mutate: mutateWallets,
    error,
    isLoading,
  } = useSWR<AssetsOnWalletResponse>(
    `${API_URL}/wallet/assets`,
    fetcherClient,
    {
      revalidateOnReconnect: false,
      revalidateOnFocus: false,
      fallbackData: [],
    },
  )
  if (error) {
    console.error(error)
  }

  const [assetsInfo, setAssetsInfo] = useState<AssetsInfo[]>([])

  useEffect(() => {
    if (
      !walletAssetsFetch ||
      isLoading ||
      walletAssetsFetch.length === 0 ||
      !walletAssetsFetch[0] ||
      walletAssetsFetch[0].assetId === ""
    ) {
      return
    }

    const converted: AssetsInfo[] = []

    walletAssetsFetch.forEach((walletAsset) => {
      const index = converted.findIndex(
        (asset) => asset.symbol === walletAsset.assetId,
      )
      if (index !== -1) {
        converted[index].shares = walletAsset.shares
      } else {
        converted.push({
          symbol: walletAsset.assetId,
          shares: walletAsset.shares,
        })
      }
    })
    setAssetsInfo(converted)
  }, [walletAssetsFetch])

  const updateWallets = async (walletComing: {
    assetId: string
    sharesToCompute: number
  }) => {
    setAssetsInfo((prev) => {
      const index = prev.findIndex(
        (asset) => asset.symbol === walletComing.assetId,
      )
      if (index !== -1) {
        console.log("walletComing", walletComing)
        prev[index].shares += walletComing.sharesToCompute
        console.log("updating prev assetInfo", prev)
        const newAssetsInfo = [...prev]
        newAssetsInfo.splice(index, 1)
        newAssetsInfo.unshift(prev[index])
        console.log("returning update asset info", newAssetsInfo)
        return newAssetsInfo
      } else {
        const copy = [...prev]
        //put on top
        copy.unshift({
          symbol: walletComing.assetId,
          shares: walletComing.sharesToCompute,
        })
        return copy
      }
    })
  }

  console.log("walletsInfo", assetsInfo)
  return (
    <WalletContext.Provider
      value={{
        wallet: assetsInfo!,
        updateWallet: updateWallets,
        isLoading: isLoading,
      }}
    >
      {children}
    </WalletContext.Provider>
  )
}
export const useWallets = () => useContext(WalletContext)
