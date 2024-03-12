"use client"

import * as React from "react"
import { CaretSortIcon, CheckIcon } from "@radix-ui/react-icons"

import { cn } from "@/lib/utils"
import { Dialog } from "@/components/ui/dialog"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { Button } from "@/components/ui/button"
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command"
import { useSearchParams } from "next/navigation"
import Link from "next/link"
import { useEffect } from "react"
import stocks from "@/lib/assets.json"
type PopoverTriggerProps = React.ComponentPropsWithoutRef<typeof PopoverTrigger>

type AssetSwitcherProps = {
  defaultAsset?: string
} & PopoverTriggerProps

export default function AssetSwitcher({
  className,
  defaultAsset,
}: AssetSwitcherProps) {
  const [open, setOpen] = React.useState(false)
  const [showNewAssetDialog, setShowNewAssetDialog] = React.useState(false)
  const searchParams = useSearchParams()
  const assetParam = searchParams.get("asset") || defaultAsset

  return (
    <Dialog open={showNewAssetDialog} onOpenChange={setShowNewAssetDialog}>
      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button
            variant="outline"
            role="combobox"
            aria-expanded={open}
            aria-label="Select a asset"
            className={cn(
              "w-[200px] justify-between transition-all",
              className,
            )}
          >
            {assetParam && `${assetParam}`}
            <CaretSortIcon className="ml-auto h-4 w-4 shrink-0 opacity-50" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-[200px] p-0">
          <Command>
            <CommandList>
              <CommandInput placeholder="Search asset..." />
              <CommandEmpty>No asset found.</CommandEmpty>
              <CommandGroup key="assets" heading="Choose Asset :">
                {stocks.map((asset) => (
                  <Link
                    href={`/homebroker?asset=${asset.symbol}`}
                    key={asset.symbol}
                  >
                    <CommandItem
                      key={asset.symbol}
                      onSelect={() => {
                        setOpen(false)
                      }}
                      className="text-sm"
                    >
                      {asset.symbol === asset.name
                        ? asset.symbol
                        : `${asset.symbol}  - ${asset.name}`}
                      <CheckIcon
                        className={cn(
                          "ml-auto h-4 w-4",
                          assetParam === asset.symbol
                            ? "opacity-100"
                            : "opacity-0",
                        )}
                      />
                    </CommandItem>
                  </Link>
                ))}
              </CommandGroup>
            </CommandList>
          </Command>
        </PopoverContent>
      </Popover>
    </Dialog>
  )
}
