"use client"
import { Button, Label, TextInput } from "flowbite-react"
import { Order } from "@/app/models"
import { FormEvent } from "react"
import { useOrders } from "@/app/contexts/order-context"

type OrderFormProps = {
  assetId: string
  walletId: string
  type: "BUY" | "SELL"
}
export function OrderForm(props: OrderFormProps) {
  const { orders, updateOrders } = useOrders()
  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const body = JSON.stringify({
      shares: event.currentTarget.shares.value,
      price: event.currentTarget.price.value,
      assetId: props.assetId,
      type: props.type,
    })
    // console.log(body)
    console.log("FETCHING POST")
    const response = await fetch(
      `http://localhost:8080/wallets/${props.walletId}/orders`,
      {
        method: "POST",
        body: body,
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-store",
      },
    )
    console.log("FETCHING POST DONE")
    const order: Order = await response.json()
    console.log(
      "OrderForm , order event received and send to updateOrders Context",
      order,
    )
    updateOrders(order)
    console.log("Update orders completed", orders)
  }
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input name="asset_id" type="hidden" defaultValue={props.assetId} />
        <input name="wallet_id" type="hidden" defaultValue={props.walletId} />
        <input name="type" type="hidden" defaultValue={props.type} />
        <div>
          <div className="mb-2 block">
            <Label htmlFor="shares" value="Quantidade" />
          </div>
          <TextInput
            id="shares"
            name="shares"
            required
            type="number"
            min={1}
            step={1}
            defaultValue={1}
          />
        </div>
        <br />
        <div>
          <div className="mb-2 block">
            <Label htmlFor="shares" value="Preço R$" />
          </div>
          <TextInput
            id="price"
            name="price"
            required
            type="number"
            min={1}
            step={0.01}
            defaultValue={1}
          />
        </div>
        <br />
        <Button
          type="submit"
          className={
            props.type === "BUY" ? "dark:bg-[#228F75]" : "dark:bg-[#D73A5E]"
          }
          color={props.type === "BUY" ? "green" : "red"}
        >
          Confirmar {props.type === "BUY" ? "compra" : "venda"}
        </Button>
      </form>
    </div>
  )
}
