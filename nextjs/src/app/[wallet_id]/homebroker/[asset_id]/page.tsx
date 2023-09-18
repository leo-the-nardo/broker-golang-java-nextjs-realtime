import MyOrders from "@/app/components/MyOrders"
import { OrderForm } from "@/app/components/OrderForm"
import { Tabs } from "flowbite-react"
import { TabItem } from "flowbite-react"
import { Card } from "flowbite-react"
import { HiArrowUp, HiShoppingCart } from "@/app/components/react-icons/icons"
import { AssetChartComponent } from "@/app/components/AssetChartComponent"
import { OrderContextProvider } from "@/app/contexts/order-context"
import MyWallet from "@/app/components/MyWallet"
import { WalletContextProvider } from "@/app/contexts/wallet-context"

export default function HomeBrokerPage({
  params,
}: {
  params: { wallet_id: string; asset_id: string }
}) {
  console.log("params:", params)
  return (
    <WalletContextProvider walletId={params.wallet_id}>
      <OrderContextProvider walletId={params.wallet_id}>
        <main className="container mx-auto flex flex-grow flex-col p-2">
          <article className="format format-invert">
            <h1>Home broker - {params.asset_id}</h1>
          </article>
          <div className="mt-2 grid flex-grow grid-cols-5 gap-2">
            <div className="col-span-2">
              <div>
                <Card
                  className="dark:bg-transparent"
                  theme={{
                    root: {
                      children:
                        "flex h-full flex-col justify-center gap-4 py-4 px-2 ",
                    },
                  }}
                >
                  <Tabs aria-label="Default tabs" style="pills">
                    <TabItem active title="Comprar" icon={HiShoppingCart}>
                      <OrderForm
                        walletId={params.wallet_id}
                        assetId={params.asset_id}
                        type="BUY"
                      />
                    </TabItem>
                    <TabItem title="Vender" icon={HiArrowUp}>
                      <OrderForm
                        walletId={params.wallet_id}
                        assetId={params.asset_id}
                        type="SELL"
                      />
                    </TabItem>
                  </Tabs>
                </Card>
              </div>
              <div className="mt-2">
                <Card
                  className="dark:bg-transparent"
                  theme={{
                    root: {
                      children:
                        "flex h-full  flex-col justify-center gap-4 py-4 px-2",
                    },
                  }}
                >
                  <article className="format format-invert">
                    <h2>Carteira</h2>
                  </article>
                  <div className="max-h-96 overflow-hidden overflow-y-auto ">
                    <MyWallet
                      className="mt-2 max-h-72 overflow-hidden overflow-y-auto "
                      wallet_id={params.wallet_id}
                    />
                  </div>
                </Card>
              </div>
            </div>
            <div className="col-span-3 flex flex-grow">
              <div className=" container mx-auto flex flex-grow flex-col p-2 ">
                <AssetChartComponent symbol={params.asset_id} />
                <article className="format format-invert mt-4">
                  <h2>Minha ordens</h2>
                </article>
                <Card
                  className="mt-2 overflow-hidden"
                  theme={{
                    root: {
                      children:
                        "flex h-full flex-col justify-center items-center  ",
                    },
                  }}
                >
                  <MyOrders wallet_id={params.wallet_id} />
                </Card>
              </div>
            </div>
          </div>
        </main>
      </OrderContextProvider>
    </WalletContextProvider>
  )
}
