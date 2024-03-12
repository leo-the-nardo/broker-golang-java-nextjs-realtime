"use client"
import { useCurrentUser } from "@/hooks/use-current-user"
import { Button } from "@/components/ui/button"
import { Icons } from "@/components/icons"
import Link from "next/link"
import { Card } from "@/components/ui/card"

//Client side example
export default function SettingsPage() {
  const { user } = useCurrentUser()
  const KeyIcon = Icons["key"]
  const UserIcon = Icons["user"]
  const MailIcon = Icons["mail"]
  const PhoneIcon = Icons["telephone"]
  const AddressIcon = Icons["location"]
  const NameIcon = Icons["personLines"]
  const PassIcon = Icons["lock"]
  return (
    <div className="flex flex-col gap-2.5">
      <Card className="rounded-[5px] border bg-background p-8 shadow-sm">
        <div className="mb-6 flex items-center gap-3">
          <KeyIcon className="h-6 w-6" />
          <h2>Dados de acesso</h2>
          <Button variant="customLink" size="sm" asChild>
            <Link href="/settings/access">Alterar</Link>
          </Button>
        </div>
        <div className="flex flex-col items-start gap-1">
          <div className="flex items-center gap-4">
            <MailIcon className="h-4 text-foreground/50" />
            <p className="font-light ">{user?.username}</p>
          </div>
          <div className="flex items-center gap-4">
            <PassIcon className="h-4 text-foreground/50" />
            <p className="font-light ">Senha : *********</p>
          </div>
        </div>
      </Card>
      <Card className="rounded-[5px] border bg-background p-8 shadow-sm">
        <div className="mb-6 flex items-center gap-3">
          <UserIcon className="h-6 w-6" />
          <h2>Dados pessoais</h2>
          <Button variant="customLink" size="sm" asChild>
            <Link href="/settings/personal">Alterar</Link>
          </Button>
        </div>
        <div className="flex flex-col gap-1">
          <div className="grid grid-cols-[repeat(auto-fit,minmax(200px,1fr))] gap-4">
            <div className=" items-start">
              <div className="flex items-center gap-4">
                <NameIcon className=" h-4 text-foreground/50" />
                <p className="font-light ">{user?.name}</p>
              </div>
            </div>
            <div className="flex flex-col gap-4">
              <div className="flex items-center gap-4">
                <PhoneIcon className=" h-4 text-foreground/50" />
                <p className="font-light ">614231231312</p>
              </div>
              <div className="flex items-center gap-4">
                <AddressIcon className="w-6 text-foreground/50" />
                <p className="font-light ">
                  Rua Finlândia, Jardim das Nações, Taubaté - SP, CEP -
                  12030-202
                </p>
              </div>
            </div>
          </div>
        </div>
      </Card>
    </div>
  )
}
