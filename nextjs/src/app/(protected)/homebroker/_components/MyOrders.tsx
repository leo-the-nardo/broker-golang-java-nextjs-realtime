"use client"

import { useOrders } from "@/app/contexts/order-context"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Button } from "@/components/ui/button"
import { LuMoreHorizontal } from "react-icons/lu"
import { ScrollArea } from "@radix-ui/react-scroll-area"
import SkeletonC from "@/app/(protected)/homebroker/_components/skeleton"

const orderStatusColorMap = {
  FULFILLED: "bg-green-500 dark:bg-emerald-600/40 text-white",
  PENDING: " text-white bg-cyan-500 dark:bg-cyan-700/40",
  OPEN: " text-white bg-blue-500 dark:bg-blue-700/40",
  CLOSED: " text-white bg-gray-500 dark:bg-gray-700/40",
  FAILED: " text-white bg-red-500 dark:bg-red-700/40",
  PARTIAL: " text-white bg-purple-500 dark:bg-cyan-600/40",
}

export default function MyOrders() {
  const { orders, isLoading } = useOrders()
  const handleCopyOrderId = (orderId: string) => {
    navigator.clipboard.writeText(orderId)
  }

  const handleViewCustomer = () => {
    // Implement view customer action
  }

  const handleViewPaymentDetails = () => {
    // Implement view payment details action
  }

  return (
    <>
      {isLoading && <SkeletonC className="h-72 w-full p-8" />}
      <Table className="text-xs md:text-sm">
        <TableHeader className={cn(isLoading && "hidden")}>
          <TableRow className="shadow">
            <TableHead>Asset</TableHead>
            <TableHead>Type</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Price</TableHead>
            <TableHead>Quantity</TableHead>
            <TableHead className="hidden md:flex">Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody className={cn("max-h-72 px-2", isLoading && "hidden")}>
          {(!orders || orders.length === 0) && (
            <TableRow>
              <TableCell colSpan={6} className="text-center">
                Você não possui ordens
              </TableCell>
            </TableRow>
          )}
          {orders &&
            orders.length > 0 &&
            orders.map((order, key) => (
              <TableRow className="flex items-center py-2  md:p-0 " key={key}>
                <TableCell className=" font-mono">{order.symbol}</TableCell>
                <TableCell>
                  <Badge
                    className={cn(
                      order.type === "BUY"
                        ? "bg-[#39E9BE]/65 text-gray-700"
                        : "bg-[#D73A5E]/70 text-white",
                      "h-full w-full max-w-[35px] justify-center  md:max-w-[50px]",
                    )}
                  >
                    {order.type}
                  </Badge>
                </TableCell>
                <TableCell className="relative">
                  {(order.status === "PENDING" ||
                    order.status === "PARTIAL") && (
                    <span className="absolute left-[-0.5rem] top-1/2 flex h-2 w-2 -translate-y-1/2">
                      <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 opacity-75 md:pl-1"></span>
                      <span className="relative inline-flex h-2 w-2 rounded-full bg-emerald-400 md:pl-2"></span>
                    </span>
                  )}
                  <Badge
                    className={cn(
                      orderStatusColorMap[order.status],
                      " h-full w-[68px]  max-w-[68px] justify-center md:max-w-[80px] ",
                    )}
                  >
                    {order.status}
                  </Badge>
                </TableCell>
                <TableCell className="font-mono">
                  {order.price && order.price.toFixed(2)}
                </TableCell>
                <TableCell>{order.shares}</TableCell>
                <TableCell className="hidden md:flex">
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" className="h-8 w-8 p-0">
                        <span className="sr-only">Open menu</span>
                        <LuMoreHorizontal className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuLabel>Actions</DropdownMenuLabel>
                      <DropdownMenuItem
                        onClick={() => handleCopyOrderId(order.id)}
                      >
                        Copy Order ID
                      </DropdownMenuItem>
                      <DropdownMenuSeparator />
                      <DropdownMenuItem onClick={handleViewCustomer}>
                        View Customer
                      </DropdownMenuItem>
                      <DropdownMenuItem onClick={handleViewPaymentDetails}>
                        View Payment Details
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
        </TableBody>
        {/*)}*/}
      </Table>
      <div className="absolute bottom-0 left-0 h-3 w-full bg-gradient-to-t from-gray-300 dark:from-[#221223]/20"></div>
    </>
  )
}
