"use client"
import { createContext, useContext, useEffect, useState } from "react"
import useSWR from "swr"
import { fetcher } from "@/app/utils"
import {
  AssetsInfo,
  AssetsInfoInResponse,
  AssetsInfoResponse,
  AssetsOnWalletResponse,
} from "@/app/models"

type WalletContextProps = {
  wallet: AssetsInfo[]
  updateWallet: (updateProps: {
    assetId: string
    sharesToCompute: number
  }) => Promise<void>
}
const WalletContext = createContext<WalletContextProps>({
  wallet: [
    {
      assetId: "",
      name: "",
      shares: 0,
    },
  ],
  updateWallet: async () => {},
})

export const WalletContextProvider = ({
  children,
  walletId,
}: {
  children: React.ReactNode
  walletId: string
}) => {
  const {
    data: walletAssetsFetch,
    mutate: mutateWallets,
    error,
    isLoading,
  } = useSWR<AssetsOnWalletResponse>(
    `http://localhost:8080/wallets/${walletId}/assets`,
    fetcher,
    {
      revalidateOnReconnect: false,
      revalidateOnFocus: false,
      fallbackData: [
        {
          assetId: "",
          shares: 0,
        },
      ],
    },
  )
  if (error) {
    console.error(error)
  }

  const [ownerSymbols, setOwnerSymbols] = useState<string>("")
  const [assetsInfo, setAssetsInfo] = useState<AssetsInfo[]>([
    {
      assetId: "",
      name: "",
      shares: 0,
    },
  ])
  useEffect(() => {
    if (!walletAssetsFetch || isLoading || walletAssetsFetch.length <= 1) {
      return
    }
    if (ownerSymbols !== "") {
      return
    }
    setOwnerSymbols(
      walletAssetsFetch!.map((walletAsset) => walletAsset.assetId).join(","),
    )
    console.log("owner effect ----", ownerSymbols)
  }, [isLoading, walletAssetsFetch])
  useEffect(() => {
    if (!walletAssetsFetch || isLoading || walletAssetsFetch.length <= 1) {
      return
    }
    if (ownerSymbols === "") {
      return
    }
    const fetchAssets = async () => {
      const url = `http://localhost:8080/assets/in?symbols=${ownerSymbols}`
      console.log("URL: ---", url)
      const assetsInfo: AssetsInfoInResponse = await fetcher(url)
      const converted: AssetsInfo[] = []
      console.log("ASSETS INFO ", assetsInfo)
      assetsInfo.forEach((asset) => {
        converted.push({
          assetId: asset.symbol,
          name: asset.name,
          shares: 0,
        })
      })
      //hydrate shares in converted
      walletAssetsFetch.forEach((walletAsset) => {
        const index = converted.findIndex(
          (asset) => asset.assetId === walletAsset.assetId,
        )
        if (index !== -1) {
          converted[index].shares = walletAsset.shares
        }
      })
      const url2 = `http://localhost:8080/assets?page=1&size=10&notIn=${ownerSymbols}`
      const assetsInfo2: AssetsInfoResponse = await fetcher(url2)
      const converted2: AssetsInfo[] = assetsInfo2.content.map((asset) => ({
        assetId: asset.symbol,
        name: asset.name,
        shares: 0,
      }))
      setAssetsInfo([...converted, ...converted2])
    }
    fetchAssets()
    console.log("hydrate assets effect ----", ownerSymbols)
  }, [walletAssetsFetch, ownerSymbols])

  const updateWallets = async (walletComing: {
    assetId: string
    sharesToCompute: number
  }) => {
    console.log("walletComing", walletComing)
    console.log("assets info on the start of updateWallets:", assetsInfo)
    setAssetsInfo((prev) => {
      const index = prev.findIndex(
        (asset) => asset.assetId === walletComing.assetId,
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
        console.log(
          "Unexpected event, this should not happen, coming id is unknown",
          walletComing,
        )
        return prev
      }
    })
  }

  console.log("walletsInfo", assetsInfo)
  return (
    <WalletContext.Provider
      value={{ wallet: assetsInfo!, updateWallet: updateWallets }}
    >
      {children}
    </WalletContext.Provider>
  )
}
export const useWallets = () => useContext(WalletContext)
