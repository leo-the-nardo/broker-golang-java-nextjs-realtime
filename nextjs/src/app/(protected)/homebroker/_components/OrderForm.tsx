"use client"

import { useOrders } from "@/app/contexts/order-context"

import { Form } from "@/components/ui/form"
import MoneyInput from "@/components/money-input"
import { useForm } from "react-hook-form"

import { zodResolver } from "@hookform/resolvers/zod"
import { OrderSchema } from "@/schemas"
import * as z from "zod"
import SharesInput from "@/components/shares-input"
import { fetcherClient, postFetcher } from "@/lib/client-fetcher"
import { LoadingButton } from "@/components/ui/button2"
import { useState, useTransition } from "react"
import { toast } from "sonner"
import { cn } from "@/lib/utils"
import { API_URL } from "@/routes"

type OrderFormProps = {
  assetId: string
  type: "BUY" | "SELL"
  className?: string
  onClick?: React.MouseEventHandler<HTMLButtonElement>
}

export function OrderForm(props: OrderFormProps) {
  const { updateOrders } = useOrders()
  const form = useForm<z.infer<typeof OrderSchema>>({
    resolver: zodResolver(OrderSchema),
    defaultValues: {
      shares: "" as any,
      price: 0,
    },
  })
  const [isPending, startTransition] = useTransition()
  async function onSubmit(data: z.infer<typeof OrderSchema>) {
    console.log("OrderForm , onSubmit data:", data)
    const validatedFields = OrderSchema.safeParse(data)
    if (!validatedFields.success) {
      return
    }
    startTransition(async () => {
      const body = JSON.stringify({
        shares: data.shares,
        price: data.price,
        assetId: props.assetId,
        type: props.type,
      })
      const res = await postFetcher(`${API_URL}/orders`, {
        method: "POST",
        body: body,
        cache: "no-store",
      })
      if (res.status === 402) {
        toast.error(
          "Você não possui ativos suficientes para realizar esta operação",
        )
        return
      }
      toast.success("Ordem enviada com sucesso")
      updateOrders(res.body)
    })
  }

  return (
    <div>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className={cn(" space-y-7", props.className)}
        >
          <input name="asset_id" type="hidden" defaultValue={props.assetId} />
          <input name="type" type="hidden" defaultValue={props.type} />
          <MoneyInput
            form={form}
            name="price"
            label="Price"
            placeholder="0,00"
          />
          <SharesInput
            form={form}
            name="shares"
            label="Choose quantity:"
            placeholder="5"
          />
          <div>
            <p
              id="helper-text-explanation"
              className="mb-2.5 pt-1.5 text-xs text-gray-500 dark:text-gray-400"
            >
              This action can not be undone.
            </p>

            <LoadingButton
              onClick={props.onClick}
              loading={isPending}
              type="submit"
              className="group relative mb-2 inline-flex h-auto  items-center justify-center overflow-hidden rounded-lg bg-gradient-to-br from-green-400 to-cyan-600 p-0.5 text-white shadow  transition-all hover:bg-cyan-400 focus:outline-none focus:ring-4 focus:ring-green-800 group-hover:from-green-400 group-hover:to-blue-600"
            >
              <span className="relative  w-full rounded-md bg-white px-5 py-2.5 text-xs font-semibold uppercase tracking-wide transition-all duration-75 ease-in group-hover:bg-opacity-0 dark:bg-[#1D2C3A] md:w-auto">
                CONFIRMAR {props.type === "BUY" ? "COMPRA" : "VENDA"}
              </span>
            </LoadingButton>
          </div>
        </form>
      </Form>
    </div>
  )
}
