"use client"
import {
  Badge,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeadCell,
  TableRow,
} from "flowbite-react"

import { useOrders } from "@/app/contexts/order-context"

const orderStatusColorMap = {
  FULFILLED: "green",
  PENDING: "cyan",
  OPEN: "blue",
  CLOSED: "gray",
  FAILED: "red",
  PARTIAL: "purple",
}

export default function MyOrders(props: { wallet_id: string }) {
  const { orders, updateOrders } = useOrders()
  console.log("MyOrders Renderized with : orders:", orders)
  return (
    <div className=" max-h-64 w-full overflow-x-hidden">
      <Table
        theme={{
          head: { cell: { base: "bg-[#252131] p-4" } },
          body: {
            base: "overflow-hidden  overflow-y-auto",
            cell: {
              base: "bg-[#191724] p-4",
            },
          },
        }}
      >
        <TableHead>
          <TableHeadCell>asset_id</TableHeadCell>
          <TableHeadCell>tipo</TableHeadCell>
          <TableHeadCell>status</TableHeadCell>
          <TableHeadCell>price</TableHeadCell>
          <TableHeadCell>quant.</TableHeadCell>
        </TableHead>
        <TableBody>
          {orders!.orders.map((order, key) => (
            <TableRow className=" border-gray-700 bg-gray-800" key={key}>
              <TableCell className="whitespace-nowrap font-medium text-white">
                {order.assetId}
              </TableCell>
              <TableCell>
                <Badge color={order.type === "BUY" ? "green" : "lime"}>
                  {order.type}
                </Badge>
              </TableCell>
              <TableCell className="relative flex items-center">
                <Badge color={orderStatusColorMap[order.status]}>
                  {order.status}
                </Badge>
                <span
                  className={
                    order.status === "PENDING" || order.status === "PARTIAL"
                      ? "absolute left-0 top-1/2 flex h-2 w-2 -translate-y-1/2 pr-1"
                      : ""
                  }
                >
                  <span
                    className={
                      order.status === "PENDING" || order.status === "PARTIAL"
                        ? "absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 pl-1 opacity-75"
                        : ""
                    }
                  ></span>
                  <span
                    className={
                      order.status === "PENDING" || order.status === "PARTIAL"
                        ? "relative inline-flex h-2 w-2 rounded-full bg-emerald-400 pl-2"
                        : ""
                    }
                  ></span>
                </span>
              </TableCell>

              <TableCell>{order.price}</TableCell>
              <TableCell>{order.shares}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
