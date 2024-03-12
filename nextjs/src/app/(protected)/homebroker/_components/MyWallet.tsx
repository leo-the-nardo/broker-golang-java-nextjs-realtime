"use client"

import Link from "next/link"
import { useWallets } from "@/app/contexts/wallet-context"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { cn } from "@/lib/utils"
import SkeletonC from "@/app/(protected)/homebroker/_components/skeleton"

export default function MyWallet() {
  const { wallet, isLoading } = useWallets()

  return (
    <>
      <div className="absolute bottom-0 left-0 z-50 h-3 w-full backdrop-blur-sm "></div>
      {isLoading && <SkeletonC className="max-h-72" />}
      <Table className={cn(isLoading && "hidden")}>
        <TableHeader>
          <TableRow className="px-4 shadow">
            <TableHead className="justify-start ">Ação</TableHead>
            <TableHead>Quant.</TableHead>
            <TableHead>
              <span className="sr-only">Comprar/Vender</span>
            </TableHead>
          </TableRow>
        </TableHeader>
        <TableBody className="max-h-72 px-4">
          {(!wallet || wallet.length === 0) && (
            <TableRow>
              <TableCell colSpan={3} className="text-center">
                Você não possui ações na carteira
              </TableCell>
            </TableRow>
          )}
          {wallet &&
            wallet.map((assetInfo, key) => (
              <TableRow className="" key={key}>
                <TableCell className="justify-start">
                  {assetInfo.symbol}
                </TableCell>
                {/*<TableCell>{"prc"}</TableCell>*/}
                <TableCell className="font-mono">
                  {new Intl.NumberFormat("en-US", {
                    minimumIntegerDigits: 2,
                    useGrouping: false,
                  }).format(assetInfo.shares as any)}
                </TableCell>
                <TableCell>
                  <Link
                    className="font-medium text-cyan-500 hover:underline"
                    href={`/homebroker?asset=${assetInfo.symbol}`}
                  >
                    Visualizar
                  </Link>
                </TableCell>
              </TableRow>
            ))}
        </TableBody>
      </Table>
    </>
  )
}
