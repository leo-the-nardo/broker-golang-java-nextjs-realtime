"use server"

import { API_URL } from "@/routes"

export async function newVerification(token: string) {
  const response = await fetch(`${API_URL}/api/signup/confirm?token=${token}`)
  if (!response.ok) {
    return {
      error: "Something went wrong! maybe is expired or doesn't exists.",
    }
  }
  return { success: "Email verified!" }
}
