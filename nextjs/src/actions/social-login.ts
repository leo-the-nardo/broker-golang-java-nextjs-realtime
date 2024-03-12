"use server"

import { signIn } from "../../auth"
import { DEFAULT_LOGIN_REDIRECT } from "@/routes"
import { AuthError } from "next-auth"
import { WrongProviderError } from "../../custom-auth-errors"
export const socialLogin = async (provider: string) => {
  try {
    await signIn(provider, {
      redirectTo: DEFAULT_LOGIN_REDIRECT, // callbackUrl || DEFAULT_LOGIN_REDIRECT,
      // redirect: false,
    })
  } catch (err) {
    if (err instanceof WrongProviderError) {
      console.log("WrongProviderError", err)
      return { error: "This email exists with another provider" }
    }
    throw err
  }
}
