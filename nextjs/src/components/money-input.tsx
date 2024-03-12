"use client"
import { useReducer } from "react"

import { UseFormReturn } from "react-hook-form"
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"

type TextInputProps = {
  form: UseFormReturn<any>
  name: string
  label: string
  placeholder: string
}

const moneyFormatter = Intl.NumberFormat("pt-BR", {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
})

export default function MoneyInput(props: TextInputProps) {
  const initialValue = props.form.getValues()[props.name]
    ? moneyFormatter.format(props.form.getValues()[props.name])
    : ""

  const [value, setValue] = useReducer((_: any, next: string) => {
    const digits = next.replace(/\D/g, "")
    return moneyFormatter.format(Number(digits) / 100)
  }, initialValue)

  function handleChange(realChangeFn: Function, formattedValue: string) {
    const digits = formattedValue.replace(/\D/g, "")
    const realValue = Number(digits) / 100
    realChangeFn(realValue)
  }

  return (
    <FormField
      control={props.form.control}
      name={props.name}
      render={({ field }) => {
        field.value = value
        const _change = field.onChange

        return (
          <FormItem>
            <FormLabel>{props.label}</FormLabel>
            <FormControl>
              <div className="relative w-full ">
                <div className="pointer-events-none absolute inset-y-0 start-0 top-0 flex items-center ps-3.5">
                  $
                </div>
                <Input
                  className="  text-right text-base "
                  placeholder={props.placeholder}
                  type="text"
                  {...field}
                  onChange={(ev) => {
                    setValue(ev.target.value)
                    handleChange(_change, ev.target.value)
                  }}
                  value={value}
                  autoComplete="off"
                />
              </div>
            </FormControl>
            <FormMessage />
          </FormItem>
        )
      }}
    />
  )
}
