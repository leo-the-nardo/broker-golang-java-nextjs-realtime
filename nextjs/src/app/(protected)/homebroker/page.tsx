import { Card } from "@/components/ui/card"
import { CSSProperties } from "react"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Icons } from "@/components/icons"
import * as React from "react"
import { OrderForm } from "@/app/(protected)/homebroker/_components/OrderForm"
import { HiShoppingCart } from "react-icons/hi2"
import { HiArrowUp } from "react-icons/hi"
import MyWallet from "@/app/(protected)/homebroker/_components/MyWallet"
import { AssetChartComponent } from "@/app/(protected)/homebroker/_components/AssetChartComponent"
import MyOrders from "@/app/(protected)/homebroker/_components/MyOrders"

export default function HomeBrokerPage({
  // params,
  searchParams,
}: {
  // params: { asset_id: string }
  searchParams: { asset: string }
}) {
  return (
    <main className="flex flex-grow flex-col gap-6 md:grid md:grid-cols-6 md:gap-8">
      <div className="order-2 col-span-2  flex flex-col md:order-1 md:gap-8 md:pt-6">
        <div
          style={
            {
              // "--background": "30 41 59",
              "--highlight": "255 255 255",
              "--bg-color":
                "linear-gradient(145deg, rgba(12, 117, 125, 1) 0%, rgba(59, 30, 61, 0.4) 100%)",
              // "linear-gradient(145deg, rgba(59, 30, 61, 1) 0%, rgba(12, 117, 125, 0.1) 100%)",
              "--border-color": `linear-gradient(145deg,rgb(var(--highlight)) 0%, rgb(var(--highlight) / 0.3) 33.33%,rgb(var(--highlight) / 0.14) 66.67%, rgb(var(--highlight) / 0.1) 100%)`,
            } as CSSProperties
          }
          className="hidden rounded-xl border border-transparent p-4 text-center [background:padding-box_var(--bg-color),border-box_var(--border-color)] md:block lg:p-8 lg:px-14"
        >
          <Tabs defaultValue="BUY" className="justify-items-center">
            <TabsList>
              <TabsTrigger value="BUY">
                <Icons.buy />
                Comprar
              </TabsTrigger>
              <TabsTrigger value="SELL">
                <Icons.sell /> Vender
              </TabsTrigger>
            </TabsList>
            <TabsContent value="BUY">
              <OrderForm assetId={searchParams.asset ?? "IBM"} type="BUY" />
            </TabsContent>
            <TabsContent value="SELL">
              <OrderForm assetId={searchParams.asset ?? "IBM"} type="SELL" />
            </TabsContent>
          </Tabs>
        </div>
        <div className="mt-2">
          <Card className="flex h-full flex-col  justify-center gap-4 border border-gray-700/80 bg-transparent py-4 shadow-md drop-shadow-md">
            <article className="format format-invert pl-4">
              <h2 className="flex items-center gap-4 text-xl">
                <Icons.wallet className="opacity-80" />
                Minha Carteira
              </h2>
            </article>
            <div className="mt-2 max-h-96 overflow-hidden overflow-y-auto">
              <MyWallet />
            </div>
          </Card>
        </div>
      </div>
      <div className="container order-1 col-span-4 flex flex-grow flex-col py-2 md:order-2 md:p-2">
        <div>
          <AssetChartComponent symbol={searchParams.asset ?? "IBM"} />
        </div>
        <div>
          <article className="format format-invert mt-4">
            <h2 className="text-xl">Historico de compra</h2>
          </article>
          <Card className="mt-2 flex w-full  flex-col items-center overflow-hidden  border border-gray-700/80 bg-transparent shadow-md drop-shadow-md">
            <MyOrders />
          </Card>
        </div>
      </div>
    </main>
  )
}
