"use client"
import { useState } from "react"
import * as React from "react"
import { cn } from "@/lib/utils"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Drawer, DrawerContent, DrawerTrigger } from "@/components/ui/drawer"
import { Button } from "@/components/ui/button"
import { OrderForm } from "@/app/(protected)/homebroker/_components/OrderForm"
import { Icons } from "@/components/icons"
import PrimaryButton from "@/app/(protected)/homebroker/_components/primary-button"

type MobileOrderFormProps = {
  assetId: string
}

export default function MobileOrderForm({ assetId }: MobileOrderFormProps) {
  const [showMobileMenu, setShowMobileMenu] = useState<boolean>(false)
  return (
    <Drawer open={showMobileMenu} onOpenChange={setShowMobileMenu}>
      <DrawerTrigger asChild>
        <div className="flex items-center justify-center  md:hidden">
          <PrimaryButton className="max-w-[220px] ">
            Comprar/Vender
          </PrimaryButton>
        </div>
      </DrawerTrigger>

      <DrawerContent>
        <div className=" pt-8">
          <Tabs defaultValue="BUY">
            <TabsList>
              <TabsTrigger value="BUY">
                <Icons.buy />
                Comprar
              </TabsTrigger>
              <TabsTrigger value="SELL">
                <Icons.sell /> Vender
              </TabsTrigger>
            </TabsList>
            <TabsContent value="BUY">
              <OrderForm
                assetId={assetId}
                type="BUY"
                onClick={() => {
                  setShowMobileMenu(false)
                }}
              />
            </TabsContent>
            <TabsContent value="SELL">
              <OrderForm
                assetId={assetId}
                type="SELL"
                onClick={() => {
                  setShowMobileMenu(false)
                }}
              />
            </TabsContent>
          </Tabs>
        </div>
      </DrawerContent>
    </Drawer>
  )
}
