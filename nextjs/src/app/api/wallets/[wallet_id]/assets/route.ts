import { WalletAsset } from "@/app/models"
import { NextRequest, NextResponse } from "next/server"

export async function GET(
  req: NextRequest,
  { params }: { params: { wallet_id: string } },
) {
  const response = await fetch(
    `http://localhost:8080/wallets/${params.wallet_id}/assets`,
    {
      // cache: "no-store", <- always fetch from server
      next: {
        // revalidate: isHomeBrokerClosed() ? 60 * 60 : 5,
        revalidate: 1,
      },
    },
  )
  const json = await response.json()
  // console.log(params)
  // console.log(json)
  return NextResponse.json(json)
}
