"use client"
import { createContext, useContext } from "react"
import { Order, OrderUpdatedEvent, WalletOrdersResponse } from "@/app/models"
import useSWR from "swr"
import { fetcher } from "@/app/utils"
import useSWRSubscription from "swr/subscription"
import { useWallets } from "@/app/contexts/wallet-context"

type OrderContextProps = {
  orders: WalletOrdersResponse
  updateOrders: (order: WalletOrdersResponse | Order) => void
}
const OrderContext = createContext<OrderContextProps>({
  orders: {
    orders: [
      {
        id: "",
        assetId: "",
        type: "BUY",
        status: "PENDING",
        price: 0,
        shares: 0,
        createdAt: "",
        partial: 0,
        updatedAt: "",
      },
    ],
  },
  updateOrders: () => {},
})

export const OrderContextProvider = ({
  children,
  walletId,
}: {
  children: React.ReactNode
  walletId: string
}) => {
  const {
    data: orders,
    mutate: mutateOrders,
    error,
  } = useSWR<WalletOrdersResponse>(
    `http://localhost:8080/wallets/${walletId}/orders`,
    fetcher,
    {
      revalidateOnReconnect: false,
      revalidateOnFocus: false,
      fallbackData: {
        orders: [
          {
            id: "",
            assetId: "",
            type: "BUY",
            status: "PENDING",
            price: 0,
            shares: 0,
            createdAt: "",
            partial: 0,
            updatedAt: "",
          },
        ],
      },
    },
  )
  if (error) {
    console.error(error)
  }

  const { data: ordersUpdated } = useSWRSubscription(
    `http://localhost:8080/wallets/${walletId}/orders/events`,
    (path, { next }) => {
      const eventSource = new EventSource(path)
      eventSource.addEventListener("order-fulfilled", async (event) => {
        console.log("BATEU NO ORDER FULFILLED")
        const orderUpdatedEvent: OrderUpdatedEvent = JSON.parse(event.data)
        await mutateOrders((prev) => {
          const orders = prev
          const orderToUpdateIndex = orders!.orders.findIndex(
            (order) => order.id === orderUpdatedEvent.orderId,
          )
          const orderToUpdate = orders!.orders[orderToUpdateIndex]
          const ordersUpdated = [...orders!.orders]
          ordersUpdated[orderToUpdateIndex] = {
            ...orderToUpdate,
            status: "FULFILLED",
          }
          return { orders: ordersUpdated }
        }, false)
        console.log("updatingWallet on ordercontext", orderUpdatedEvent)
        await updateWallet({
          assetId: orderUpdatedEvent.assetId,
          sharesToCompute:
            orderUpdatedEvent.type === "BUY"
              ? orderUpdatedEvent.negotiatedShares
              : -orderUpdatedEvent.negotiatedShares,
        })
        next(null, orderUpdatedEvent)
      })
      eventSource.addEventListener("order-partial", async (event) => {
        const orderUpdatedEvent: OrderUpdatedEvent = JSON.parse(event.data)
        await mutateOrders((prev) => {
          const orders = prev
          const orderToUpdateIndex = orders!.orders.findIndex(
            (order) => order.id === orderUpdatedEvent.orderId,
          )
          const orderToUpdate = orders!.orders[orderToUpdateIndex]
          const ordersUpdated = [...orders!.orders]
          ordersUpdated[orderToUpdateIndex] = {
            ...orderToUpdate,
            status: "PARTIAL",
          }
          return { orders: ordersUpdated }
        }, false)
        next(null, orderUpdatedEvent)
      })
      eventSource.onerror = (error) => {
        console.error(error)
        eventSource.close()
      }
      return () => {
        eventSource.close()
      }
    },
  )

  const { updateWallet } = useWallets()

  const updateOrders = async (order: Order | WalletOrdersResponse) => {
    if ("orders" in order) {
      console.log("If order.orders ----")
      await mutateOrders(order, false)
      return
    }
    console.log("initiind mutateOrders with order: ", order)
    await mutateOrders((prev) => {
      console.log("prev", prev)
      //update status and put the updated on top
      const index = prev!.orders.findIndex(
        (orderToFind) => orderToFind.id === order.id,
      )
      if (index !== -1) {
        prev!.orders[index] = order
        const elementToPutOnTop = prev!.orders[index]
        prev!.orders.splice(index, 1)
        prev!.orders.unshift(elementToPutOnTop)

        const newOrders = [...prev!.orders]
        console.log("returninr new orders : ", { orders: newOrders })
        return { orders: newOrders }
      }
      //insert on top
      prev!.orders.unshift(order)
      const newOrders = [...prev!.orders]
      console.log("returninr new orders : ", { orders: newOrders })
      return { orders: newOrders }
    }, false)
  }
  return (
    <OrderContext.Provider
      value={{ orders: orders!, updateOrders: updateOrders }}
    >
      {children}
    </OrderContext.Provider>
  )
}

export const useOrders = () => useContext(OrderContext)
