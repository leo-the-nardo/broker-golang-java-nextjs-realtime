"use server"
import * as z from "zod"
import { ResetSchema } from "@/schemas"
import { API_URL } from "@/routes"
export const sendResetPassMail = async (
  values: z.infer<typeof ResetSchema>,
) => {
  // on the backend because we don't trust the client
  // delay 3 sec
  await new Promise((resolve) => setTimeout(resolve, 3000))
  const validatedFields = ResetSchema.safeParse(values)
  if (!validatedFields.success) {
    return { error: "Invalid fields" } // in api route is something like return Response()...
  }
  const { email } = validatedFields.data
  const response = await fetch(`${API_URL}/api/reset-password`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      email: email,
    }),
  })
  if (!response.ok) {
    console.log("/api/reset-password response error:", response.status)
    console.log(await response.json())
    return { error: "Something goes wrong!. Try again later" }
  }
  return { success: "Email sent!" }
}
