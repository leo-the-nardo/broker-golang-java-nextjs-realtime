import { Poppins } from "next/font/google"
import { cn } from "@/lib/utils"
import { LoginButton } from "@/components/auth/login-button"
import { Icons } from "@/components/icons"
import * as React from "react"
import { LoadingButton } from "@/components/ui/button2"

const font = Poppins({
  subsets: ["latin"],
  weight: ["600"],
})
export default function Home() {
  return (
    <main className="flex h-full flex-col items-center justify-center">
      <div className="space-y-6">
        <h1
          className={cn(
            "flex animate-fade-in-up items-center gap-3 text-6xl font-semibold text-white drop-shadow-md",
            font.className,
          )}
        >
          <Icons.logo />
          Leothenardo
        </h1>
        <p className="animate-fade-in text-lg text-white">
          A simple homebroker service
        </p>
        <div className=" ">
          <LoginButton mode="redirect">
            <LoadingButton variant="default" size="lg">
              Sign in
            </LoadingButton>
          </LoginButton>
        </div>
      </div>
    </main>
  )
}
