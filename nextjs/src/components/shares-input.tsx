"use client"
import { useReducer, useState } from "react"

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

export default function SharesInput(props: TextInputProps) {
  const { form, name } = props
  const { setValue } = form
  const [value, setValueState] = useState<number>()

  const handleIncrement = () => {
    const newValue = value ? value + 1 : 1
    setValue(name, newValue) // Update form value
    setValueState(newValue) // Update local state
  }

  const handleDecrement = () => {
    if (!value) return
    if (value > 0) {
      const newValue = value - 1
      setValue(name, newValue) // Update form value
      setValueState(newValue) // Update local state
    }
  }

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = parseInt(event.target.value)
    setValue(name, newValue) // Update form value
    setValueState(newValue) // Update local state
  }
  return (
    <FormField
      control={props.form.control}
      name={props.name}
      render={({ field }) => {
        return (
          <FormItem className="mx-auto flex max-w-[150px] flex-col">
            <FormLabel>{props.label}</FormLabel>
            <FormControl>
              <div className=" flex  rounded-lg ">
                <button
                  type="button"
                  className="flex h-9 items-center rounded-l border border-r-0 border-gray-700 bg-input   p-3 hover:bg-input/0 focus:outline-none focus:ring-2 focus:ring-gray-100/10"
                  onClick={handleDecrement}
                >
                  -
                </button>
                <Input
                  className="block h-9 w-full rounded-none  py-2.5 text-center  text-sm text-white  "
                  placeholder={props.placeholder}
                  type="text"
                  {...field}
                  onChange={handleChange}
                  autoComplete="off"

                  // value={value}
                />
                <button
                  type="button"
                  className="flex h-9 items-center rounded-r border border-l-0 border-gray-700 bg-input   p-3 hover:bg-input/0 focus:outline-none focus:ring-2 focus:ring-gray-100/10"
                  onClick={handleIncrement}
                >
                  +
                </button>
              </div>
            </FormControl>
            <FormMessage />
          </FormItem>
        )
      }}
    />
  )
}
