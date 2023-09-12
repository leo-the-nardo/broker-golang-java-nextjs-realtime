import MyOrders from "@/app/components/MyOrders"
import { OrderForm } from "@/app/components/OrderForm"
import { Tabs } from "flowbite-react"
import { TabItem } from "flowbite-react"
import { Card } from "flowbite-react"
import { HiArrowUp, HiShoppingCart } from "@/app/components/react-icons/hi"
import { ChartComponent } from "@/app/components/ChartComponent"

export default async function HomeBrokerPage({
  params,
}: {
  params: { wallet_id: string; asset_id: string }
}) {
  return (
    <main className="container mx-auto flex flex-grow flex-col p-2">
      <article className="format format-invert">
        <h1>Home broker - {params.asset_id}</h1>
      </article>
      <div className="mt-2 grid flex-grow grid-cols-5 gap-2">
        <div className="col-span-2">
          <div>
            <Card
              theme={{
                root: {
                  children:
                    "flex h-full flex-col justify-center gap-4 py-4 px-2",
                },
              }}
            >
              <Tabs aria-label="Default tabs" style="pills">
                <TabItem active title="Comprar" icon={HiShoppingCart}>
                  <OrderForm
                    wallet_id={params.wallet_id}
                    asset_id={params.asset_id}
                    type="BUY"
                  />
                </TabItem>
                <TabItem title="Vender" icon={HiArrowUp}>
                  <OrderForm
                    wallet_id={params.wallet_id}
                    asset_id={params.asset_id}
                    type="SELL"
                  />
                </TabItem>
              </Tabs>
            </Card>
          </div>
          <div className="mt-2">
            <Card
              theme={{
                root: {
                  children:
                    "flex h-full flex-col justify-center gap-4 py-4 px-2",
                },
              }}
            >
              <MyOrders wallet_id={params.wallet_id} />
            </Card>
          </div>
        </div>
        <div className="col-span-3 flex flex-grow">
          <ChartComponent header="Asset 1 - R$ 100" />
        </div>
      </div>
    </main>
  )
}
