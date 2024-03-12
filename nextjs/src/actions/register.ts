"use server"
import * as z from "zod"
import { RegisterSchema } from "@/schemas"
import { API_URL } from "@/routes"

export const register = async (values: z.infer<typeof RegisterSchema>) => {
  // on the backend because we don't trust the client
  const validatedFields = RegisterSchema.safeParse(values)
  if (!validatedFields.success) {
    return { error: "Invalid fields" } // in api route is something like return Response()...
  }
  const { name, password, email } = validatedFields.data
  const response = await fetch(`${API_URL}/api/signup`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username: email,
      password: password,
      name: name,
    }),
  })
  if (response.status === 409) {
    return { error: "User already exists" }
  }
  if (!response.ok) {
    console.log(await response.json())
    return { error: "Not able to authenticate" }
  }
  return { success: "Confirmation email sent!" }
}
