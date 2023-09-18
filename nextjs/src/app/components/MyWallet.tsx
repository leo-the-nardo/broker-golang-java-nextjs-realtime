"use client"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeadCell,
  TableRow,
} from "flowbite-react"
import Link from "next/link"
import { useWallets } from "@/app/contexts/wallet-context"

export default function MyWallet(props: {
  wallet_id: string
  className?: string
}) {
  const { wallet, updateWallet } = useWallets()

  console.log("wallet:", wallet)
  return (
    <div className={props.className ?? ""}>
      <Table
        theme={{
          head: { cell: { base: "bg-[#252131] p-4" } },
          body: {
            cell: {
              base: "bg-[#191724] p-4",
            },
          },
        }}
      >
        <TableHead>
          <TableHeadCell>Nome</TableHeadCell>
          <TableHeadCell>Quant.</TableHeadCell>
          <TableHeadCell>
            <span className="sr-only">Comprar/Vender</span>
          </TableHeadCell>
        </TableHead>
        <TableBody className="divide-y">
          {wallet!.map((assetInfo, key) => (
            <TableRow className="border-gray-700 bg-gray-800" key={key}>
              <TableCell className="whitespace-nowrap font-medium text-white">
                {assetInfo.assetId} ({assetInfo.name})
              </TableCell>
              {/*<TableCell>{"prc"}</TableCell>*/}
              <TableCell>{assetInfo.shares}</TableCell>
              <TableCell>
                <Link
                  className="font-medium text-cyan-500 hover:underline"
                  href={`/${props.wallet_id}/homebroker/${assetInfo.assetId}`}
                >
                  Comprar/Vender
                </Link>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
