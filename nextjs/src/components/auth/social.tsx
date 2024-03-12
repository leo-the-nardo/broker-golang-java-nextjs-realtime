"use client"
import { signIn } from "next-auth/react"

import { FcGoogle } from "react-icons/fc"
import { FaGithub } from "react-icons/fa"
import { DEFAULT_LOGIN_REDIRECT } from "@/routes"
import { useTransition } from "react"
import { LoadingButton } from "@/components/ui/button2"

export function Social() {
  const [isPending, startTransition] = useTransition()
  async function onClick(provider: "google" | "github") {
    startTransition(async () => {
      await signIn(provider, {
        callbackUrl: DEFAULT_LOGIN_REDIRECT,
      })
    })
  }
  return (
    <div className="flex w-full items-center gap-x-2">
      <LoadingButton
        size="lg"
        className="w-full bg-accent"
        variant="outline"
        onClick={() => onClick("google")}
        loading={isPending}
      >
        <FcGoogle className="h-5 w-5" />
      </LoadingButton>

      <LoadingButton
        size="lg"
        className="w-full bg-accent"
        variant="outline"
        onClick={() => onClick("github")}
        loading={isPending}
      >
        <FaGithub className="h-5 w-5" />
      </LoadingButton>
    </div>
  )
}
