"use server"

import { Card } from "@/components/ui/card"
import { DashboardNav, SidebarNavItem } from "@/components/nav"
import AvatarC from "@/app/(protected)/settings/_components/avatar"
const sideBarItems: SidebarNavItem[] = [
  {
    icon: "general",
    title: "Visão geral",
    href: "/settings",
  },
  {
    title: "Dados de acesso",
    href: "/settings/access",
    icon: "key",
  },
  {
    icon: "user",
    title: "Dados pessoais",
    href: "/settings/personal",
  },
  {
    icon: "user",
    title: "Profile",
    href: "/settings/profile",
  },
]

export default async function DashboardLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <Card className="border-b p-3 shadow-lg md:px-10">
      <div className="md:my-2.75rem mx-auto grid w-full grid-cols-1 items-start gap-x-6 gap-y-10 md:my-10 md:grid-cols-312px-1fr">
        <div className="col-span-full flex items-center gap-4">
          <AvatarC />
          <div>
            <h1 className="text-2xl font-bold ">Minha conta</h1>
            <p className="text-sm text-muted-foreground">
              Gerencie as informações de conta, dados pessoais e acesso
            </p>
          </div>
        </div>
        <aside className=" md:items-startt my-0 flex flex-col items-center rounded-[5px] sm:mx-[-20px] md:mx-0 md:border md:py-4 md:shadow-sm">
          <DashboardNav items={sideBarItems} />
        </aside>
        <main className="w-full">{children}</main>
      </div>
    </Card>
  )
}
