"use client"
import * as z from "zod"
import { CardWrapper } from "@/components/auth/card-wrapper"
import { useForm } from "react-hook-form"
import { OrderSchema, RegisterSchema } from "@/schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { FormError } from "@/components/form-error"
import { FormSuccess } from "@/components/form-success"
import { useState, useTransition } from "react"
import { register } from "@/actions/register"

export const formatCpfCnpj = (value: string) => {
  if (!value) return ""
  const cleanedValue = value.replace(/\D/g, "") // remove caracteres não numéricos
  if (cleanedValue.length <= 11) {
    // CPF
    return cleanedValue
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d{1,2})/, "$1-$2")
      .replace(/(-\d{2})\d+?$/, "$1")
  } else {
    if (cleanedValue.length <= 14) {
      // CNPJ
      return cleanedValue
        .replace(/(\d{2})(\d)/, "$1.$2")
        .replace(/(\d{3})(\d)/, "$1.$2")
        .replace(/(\d{3})(\d)/, "$1/$2")
        .replace(/(\d{4})(\d{1,2})/, "$1-$2")
    }
  }
}

export function RegisterForm() {
  const [error, setError] = useState<string | undefined>("")
  const [success, setSuccess] = useState<string | undefined>("")
  const form = useForm<z.infer<typeof RegisterSchema>>({
    resolver: zodResolver(RegisterSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  })
  const [isPending, startTransition] = useTransition()

  const onSubmit = (data: z.infer<typeof RegisterSchema>) => {
    setError("") // reset
    setSuccess("")

    startTransition(() => {
      // server action
      register(data).then((data) => {
        setError(data.error)
        setSuccess(data.success)
      })
    })
  }

  return (
    <CardWrapper
      headerLabel="Create an account"
      backButtonLabel="Already have an account?"
      backButtonHref="/auth/login"
      showSocial
    >
      <Form {...form}>
        <form className="space-y-6" onSubmit={form.handleSubmit(onSubmit)}>
          <div className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Name</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      disabled={isPending}
                      placeholder="John Doe"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      disabled={isPending}
                      placeholder="john.doe@gmail.com"
                      type="email"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="cpfCnpj"
              render={({ field: { onChange, ...props } }) => (
                <FormItem>
                  <FormLabel>CPF/CNPJ*</FormLabel>
                  <FormControl>
                    <Input
                      onChange={(e) => {
                        const { value } = e.target
                        const cleanedValue = formatCpfCnpj(value)
                        if (cleanedValue !== undefined) {
                          e.target.value = cleanedValue
                          onChange(e)
                        }
                      }}
                      placeholder="CPF/CNPJ"
                      {...props}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      disabled={isPending}
                      placeholder="********"
                      type="password"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <FormError message={error} />
          <FormSuccess message={success} />
          <Button disabled={isPending} type="submit" className="w-full">
            Create account
          </Button>
        </form>
      </Form>
    </CardWrapper>
  )
}
