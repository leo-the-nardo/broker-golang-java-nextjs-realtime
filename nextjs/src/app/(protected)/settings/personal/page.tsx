"use client"
import { useState, useTransition } from "react"
import { useCurrentUser } from "@/hooks/use-current-user"
import { Button } from "@/components/ui/button"
import { Icons } from "@/components/icons"
import Link from "next/link"
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"

import { toast } from "sonner"
import { fetchExample } from "@/actions/auth"
import { sendResetPassMail } from "@/actions/sendResetPassMail"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { useForm } from "react-hook-form"
import * as z from "zod"
import { ProfileSchema, RegisterSchema } from "@/schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import { Input } from "@/components/ui/input"
import { Card } from "@/components/ui/card"

export default function SettingsAccessPage() {
  const { user } = useCurrentUser()
  const form = useForm<z.infer<typeof ProfileSchema>>({
    resolver: zodResolver(RegisterSchema),
    defaultValues: {},
  })
  const PersonalIcon = Icons["user"]
  function onSubmit() {}

  return (
    <>
      <div className="mb-8 flex items-center gap-3">
        <PersonalIcon className=" h-6 w-6" />
        <h2 className="text-xl">Dados pessoais</h2>
      </div>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="flex flex-col gap-4   "
        >
          <Card className="rounded-[5px] bg-card/15 p-8">
            <section className="grid auto-cols-[1fr]  gap-4 md:grid-flow-col md:grid-cols-[2fr_1fr]">
              <FormField
                control={form.control}
                name="name"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      Name
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder={user?.name}
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="cpf"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      CPF
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="123.456.789-00"
                          required={false}
                          disabled={true}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </section>
            <section className="mt-6 grid auto-cols-[1fr] gap-4 md:grid-flow-col md:grid-cols-[2fr_1fr]">
              <section className="grid gap-4 md:grid-flow-col md:grid-cols-[140px_1fr]">
                <FormField
                  control={form.control}
                  name="name"
                  rules={{ required: false }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="mb-2 block text-sm text-foreground/60">
                        Nascimento
                      </FormLabel>
                      <div className="relative flex w-full transition-opacity duration-700 ease-out">
                        <FormControl>
                          <Input
                            placeholder="31/12/2000"
                            required={false}
                            {...field}
                            className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                          />
                        </FormControl>
                      </div>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="name"
                  rules={{ required: false }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="mb-2 block text-sm text-foreground/60">
                        Genero
                      </FormLabel>
                      <div className="relative flex w-full transition-opacity duration-700 ease-out">
                        <FormControl>
                          <Input
                            placeholder={user?.name}
                            required={false}
                            {...field}
                            className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                          />
                        </FormControl>
                      </div>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </section>
              <div>
                <FormField
                  control={form.control}
                  name="name"
                  rules={{ required: false }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="mb-2 block text-sm text-foreground/60">
                        Telefone
                      </FormLabel>
                      <div className="relative flex w-full transition-opacity duration-700 ease-out">
                        <FormControl>
                          <Input
                            placeholder="(61)99999-9999"
                            required={false}
                            {...field}
                            className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                          />
                        </FormControl>
                      </div>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
            </section>
          </Card>
          <Card className="flex flex-col gap-6 rounded-[5px] bg-card/15 p-8">
            <div className="flex items-center border-b border-foreground/15">
              <h2>Endereço</h2>
            </div>
            <section className="grid auto-cols-[1fr] gap-4 md:grid-flow-col md:grid-cols-[180px_1fr]">
              <FormField
                control={form.control}
                name="cep"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      CEP
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="12345-678"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="street"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      Rua
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="Rua Finlândia"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </section>
            <section className="grid auto-cols-[1fr] gap-4 md:grid-flow-col md:grid-cols-[180px_1fr]">
              <FormField
                control={form.control}
                name="cep"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      Número
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="S/N"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="complement"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      Complemento
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="Casa,Apto etc"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </section>
            <section className="grid auto-cols-[1fr] gap-4 md:grid-flow-col md:grid-cols-[1fr_1fr_80px]">
              <FormField
                control={form.control}
                name="cep"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      Bairro
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="Jardim Europa"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="complement"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      Cidade
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="Itauapé"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />{" "}
              <FormField
                control={form.control}
                name="state"
                rules={{ required: false }}
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="mb-2 block text-sm text-foreground/60">
                      UF
                    </FormLabel>
                    <div className="relative flex w-full transition-opacity duration-700 ease-out">
                      <FormControl>
                        <Input
                          placeholder="SP"
                          required={false}
                          {...field}
                          className=" h-12 w-full p-4  transition-all duration-300 ease-in-out "
                        />
                      </FormControl>
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </section>
          </Card>
          <div className="flex justify-end">
            <Button
              className="relative inline-flex flex-shrink-0 px-8 py-3 font-semibold transition-colors duration-200 ease-in-out "
              type="submit"
            >
              Salvar
            </Button>
          </div>
        </form>
      </Form>
    </>
  )
}
