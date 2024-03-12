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
import { ButtonLoading } from "@/components/ui/button-loading"
import Spinner from "@/components/ui/spinner"
import { LoadingButton } from "@/components/ui/button2"

export default function SettingsAccessPage() {
  const [isPassOpen, setIsPassOpen] = useState(false)
  const { user } = useCurrentUser()
  console.log("session: ", user)
  const [isPending, startTransition] = useTransition()
  function onClickPassChange() {
    startTransition(() => {
      sendResetPassMail({
        email: user!.username,
      })
        .then((res) => {
          console.log(res)
          setIsPassOpen(false)
          toast.success("Email enviado com sucesso")
        })
        .catch((err) => {
          setIsPassOpen(false)
          console.error(err)
          toast.error("Erro ao enviar email")
        })
    })
  }
  const KeyIcon = Icons["key"]
  const MailIcon = Icons["mailOut"]
  const PassIcon = Icons["lockOut"]
  return (
    <>
      <div className="mb-8 flex items-center gap-3">
        <KeyIcon className=" h-6 w-6" />
        <h2 className="text-xl">Dados de acesso</h2>
      </div>
      <div className="rounded-[5px] border bg-card/15 p-6 shadow-sm md:px-12 md:py-10">
        <div className="grid grid-cols-1 gap-x-3 gap-y-6 md:grid-cols-[1.5fr_1fr]">
          <div>
            <label className="mb-2 block text-sm text-foreground/60">
              E-mail
            </label>
            <div className="flex w-full min-w-0 max-w-full items-center gap-3 rounded-sm bg-input bg-opacity-50 p-4 ">
              <MailIcon className="h-6 w-6 flex-shrink-0 text-accent-foreground/60 " />
              <p className="flex-basis-0 mr-auto max-w-[250px] flex-shrink flex-grow overflow-hidden overflow-ellipsis whitespace-nowrap font-light text-muted-foreground ">
                {user?.username}
              </p>
              <Button
                type="button"
                className="ml-auto"
                variant="link"
                size="sm"
                disabled
              >
                Alterar
              </Button>
            </div>
          </div>
          <div>
            <label className="mb-2 block text-sm text-foreground/60">
              Senha
            </label>
            <div className="flex w-full min-w-0 max-w-full items-center gap-3 rounded-sm bg-input bg-opacity-50 p-4 ">
              <PassIcon className="h-6 w-6 flex-shrink-0 text-accent-foreground/60 " />
              <p className="flex-basis-0 mr-auto max-w-[250px] flex-shrink flex-grow overflow-hidden overflow-ellipsis whitespace-nowrap font-light text-muted-foreground ">
                *********
              </p>
              <Dialog open={isPassOpen} onOpenChange={setIsPassOpen}>
                <DialogTrigger asChild>
                  <Button
                    type="submit"
                    className="ml-auto"
                    variant="customLink"
                    size="sm"
                  >
                    Alterar
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Are you absolutely sure?</DialogTitle>
                    <DialogDescription>
                      <span>
                        We will send a confirmation email to update your
                        password. The link will expire soon.
                      </span>
                    </DialogDescription>
                  </DialogHeader>
                  <DialogFooter>
                    <DialogClose asChild>
                      <Button type="button" variant="secondary">
                        Close
                      </Button>
                    </DialogClose>
                    <LoadingButton
                      onClick={onClickPassChange}
                      loading={isPending}
                    >
                      Send email confirmation
                    </LoadingButton>
                  </DialogFooter>
                </DialogContent>
              </Dialog>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}
