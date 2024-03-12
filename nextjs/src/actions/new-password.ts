"use server"

import { NewPasswordSchema } from "@/schemas"
import * as z from "zod"
import { API_URL } from "@/routes"

export async function newPassword(
  values: z.infer<typeof NewPasswordSchema>,
  token: string,
) {
  const validatedFields = NewPasswordSchema.safeParse(values)
  if (!validatedFields.success) {
    return { error: "Invalid fields" } // in api route is something like return Response()...
  }
  const { password } = validatedFields.data
  const response = await fetch(`${API_URL}/api/reset-password?token=${token}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      password: password,
    }),
  })
  if (!response.ok) {
    if (response.status === 410) {
      return {
        error: "Token invalid. If expired we sent a new Token for you email.",
      }
    }
    console.log("/api/reset-password?token response error:", response.status)
    console.log(await response.json())
    return { error: "Something goes wrong!. Try again later" }
  }
  return { success: "Password reset done!" }
}
