"use client"
import { createContext, useContext, useState } from "react"
import { Order, OrderUpdatedEvent, WalletOrdersResponse } from "@/app/models"
import useSWR from "swr"

import { useWallets } from "@/app/contexts/wallet-context"
import { fetcherClient, syncCookie } from "@/lib/client-fetcher"
import { toast } from "sonner"
import useSWRSubscription from "swr/subscription"
import { API_URL } from "@/routes"

type OrderContextProps = {
  orders: Order[]
  updateOrders: (order: WalletOrdersResponse | Order) => void
  isLoading: boolean
}
const OrderContext = createContext<OrderContextProps>({
  orders: [
    {
      id: "",
      symbol: "",
      type: "BUY",
      status: "PENDING",
      price: 0,
      shares: 0,
      createdAt: "",
      partial: 0,
      updatedAt: "",
    },
  ],
  updateOrders: () => {},
  isLoading: true,
})

export const OrderContextProvider = ({
  children,
}: {
  children: React.ReactNode
}) => {
  const {
    data: orders,
    mutate: mutateOrders,
    error,
    isLoading,
  } = useSWR<WalletOrdersResponse>(`${API_URL}/orders`, fetcherClient, {
    revalidateOnReconnect: false,
    revalidateOnFocus: false,
    fallbackData: [
      {
        id: "",
        symbol: "",
        type: "BUY",
        status: "PENDING",
        price: 0,
        shares: 0,
        createdAt: "",
        partial: 0,
        updatedAt: "",
      },
    ],
  })
  if (error) {
    console.error(error)
  }

  const [retry, setRetry] = useState(0)
  useSWRSubscription(
    [retry, `${API_URL}/orders/events`],
    ([_, path], { next }) => {
      const eventSource = new EventSource(path, {
        withCredentials: true,
      })
      eventSource.addEventListener("order-fulfilled", async (event) => {
        const orderEvent: OrderUpdatedEvent = JSON.parse(event.data)
        await updateOrders({
          ...orderEvent,
          createdAt: Date.now().toString(),
          price: orderEvent.negotiatedPrice,
          partial: orderEvent.negotiatedShares,
          id: orderEvent.orderId,
          updatedAt: Date.now().toString(),
        })
        await updateWallet({
          assetId: orderEvent.symbol,
          sharesToCompute:
            orderEvent.type === "BUY"
              ? orderEvent.negotiatedShares
              : -orderEvent.negotiatedShares,
        })
        toast.success(
          `FULFILLED: ${orderEvent.negotiatedShares} ${orderEvent.symbol} ${
            orderEvent.type === "BUY" ? "comprados" : "vendidos"
          } a $${orderEvent.negotiatedPrice}`,
        )
        next(null, orderEvent)
      })
      eventSource.addEventListener("order-partial", async (event) => {
        const orderEvent: OrderUpdatedEvent = JSON.parse(event.data)
        await updateOrders({
          ...orderEvent,
          createdAt: Date.now().toString(),
          price: orderEvent.negotiatedPrice,
          partial: orderEvent.negotiatedShares,
          id: orderEvent.orderId,
          updatedAt: Date.now().toString(),
        })
        await updateWallet({
          assetId: orderEvent.symbol,
          sharesToCompute:
            orderEvent.type === "BUY"
              ? orderEvent.negotiatedShares
              : -orderEvent.negotiatedShares,
        })
        toast.success(
          `PARTIAL: ${orderEvent.negotiatedShares}  de ${orderEvent.shares} ${
            orderEvent.symbol
          } ${orderEvent.type === "BUY" ? "comprados" : "vendidos"} a $${
            orderEvent.negotiatedPrice
          }`,
        )
        next(null, orderEvent)
      })

      eventSource.onerror = (error) => {
        eventSource.close()
        syncCookie().then(() => {
          //for some reason, firefox(only) closes the connection randomly so i need it
          setTimeout(() => {
            setRetry((prev) => prev + 1)
          }, 2000)
        })
      }
      return () => {
        eventSource.close()
      }
    },
  )

  const { updateWallet } = useWallets()

  const updateOrders = async (order: Order | WalletOrdersResponse) => {
    if (Array.isArray(order)) {
      await mutateOrders(order, false)
      return
    }
    await mutateOrders((prev) => {
      //update status and put the updated on top
      const index = prev!.findIndex(
        (orderToFind) => orderToFind.id === order.id,
      )
      if (index !== -1) {
        prev![index] = order
        const elementToPutOnTop = prev![index]
        prev!.splice(index, 1)
        prev!.unshift(elementToPutOnTop)

        const newOrders = [...prev!]
        return newOrders
      }
      //insert on top
      prev!.unshift(order)
      const newOrders = [...prev!]
      return newOrders
    }, false)
  }
  return (
    <OrderContext.Provider
      value={{
        orders: orders!,
        updateOrders: updateOrders,
        isLoading: isLoading,
      }}
    >
      {children}
    </OrderContext.Provider>
  )
}

export const useOrders = () => useContext(OrderContext)
