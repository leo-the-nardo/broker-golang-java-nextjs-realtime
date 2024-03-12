"use server"
import { SessionProvider } from "next-auth/react"
import { MarketingConfig } from "@/components/nav"
import { UserButton } from "@/components/auth/user-button"
import { MainNav } from "@/app/(protected)/_components/mainnav"
import { auth } from "../../../auth"
import { WalletContextProvider } from "@/app/contexts/wallet-context"
import { OrderContextProvider } from "@/app/contexts/order-context"
const config: MarketingConfig = {
  mainNav: [
    {
      title: "Broker",
      href: "/homebroker",
    },
    {
      title: "Server",
      href: "/server",
    },
    {
      title: "Client",
      href: "/client",
    },
    {
      title: "Settings",
      href: "/settings",
    },
    {
      title: "Admin",
      href: "/admin",
    },
  ],
}

export default async function SessionLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  const session = await auth()
  return (
    <div className="grid h-full min-h-screen grid-cols-1 grid-rows-[auto_1fr] lg:grid-cols-[auto_1fr]">
      <SessionProvider refetchOnWindowFocus={false} session={session}>
        <WalletContextProvider>
          <OrderContextProvider>
            <header className=" z-50 col-span-full flex h-[54px] items-center border border-transparent bg-background px-4 shadow md:h-[72px] lg:h-[64px]  lg:px-12">
              <MainNav items={config.mainNav} />
              <nav className="ml-auto flex items-center">
                <UserButton />
              </nav>
            </header>
            <div className="hidden flex-col gap-3 border border-transparent border-r-gray-700 transition-all duration-300 ease-in-out lg:flex ">
              {/* --- sidebar here if you want ----*/}
            </div>
            <div className="h-[calc(100vh-72px)] overflow-auto  lg:h-[calc(100vh-80px)]">
              <div className="mx-auto flex h-full max-w-[1552px] flex-grow flex-col overflow-x-hidden p-2  px-4 ">
                {children}
              </div>
            </div>
          </OrderContextProvider>
        </WalletContextProvider>
      </SessionProvider>
    </div>
  )
}
