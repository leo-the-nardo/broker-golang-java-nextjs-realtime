"use server"
import * as z from "zod"
import { LoginSchema } from "@/schemas"
import { signIn } from "../../auth"
import { API_URL, DEFAULT_LOGIN_REDIRECT } from "@/routes"
import { AuthError } from "next-auth"
import { cookies } from "next/headers"
export const login = async (values: z.infer<typeof LoginSchema>) => {
  // on the backend because we don't trust the client
  const validatedFields = LoginSchema.safeParse(values)
  if (!validatedFields.success) {
    return { error: "Invalid fields" } // in api route is something like return Response()...
  }
  const { email, password, code } = validatedFields.data
  if (code) {
    const response = await fetch(`${API_URL}/api/auth/twoFactor`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ token: code }),
    })
    if (response.status === 401) {
      return { error: "Invalid code", twoFactor: true }
    }
    const { token } = await response.json()
    // set response.cookie
    const expires = new Date()
    expires.setFullYear(expires.getFullYear() + 1)
    cookies().set({
      expires: expires,
      name: "jubileu",
      value: token,
      httpOnly: true,
      sameSite: "lax",
      secure:
        process.env.NODE_ENV === "production" ||
        process.env.ENVIRONMENT === "prod" ||
        process.env.ENVIRONMENT === "PROD",
    })
  }
  try {
    await signIn("credentials", {
      email,
      password,
      redirect: true, // the url is in middleware.ts
    })
    return { success: "Logged in!" }
  } catch (err) {
    if (err instanceof AuthError) {
      switch (err.type) {
        case "CredentialsSignin":
          return { error: "Invalid credentials" }
        case "EmailVerification":
          return { success: "Email verification sent!" }
        case "TwoFactor":
          return { twoFactor: true }
        default:
          return { error: "Something went wrong!" }
      }
    }
    console.error(err)
    // return { error: "Something went wrong!" };
    throw err
  }
}
