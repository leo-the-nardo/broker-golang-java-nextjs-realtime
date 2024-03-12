"use server"
import { cookies } from "next/headers"

export async function setTheCookie(accessToken: string, expiresIn: number) {
  const expires = new Date()
  // const expiresIn5Minutes = Date.now() + 4.5 * 60 * 1000
  // const expiresIn5Minutes = Date.now() + 4.5 * 60 * 1000000
  expires.setFullYear(expires.getFullYear() + 1)
  // cookies().delete("jvse")
  // cookies().delete("expiresIn")
  cookies().set({
    name: "jvse",
    value: accessToken,
    sameSite: "lax",
    httpOnly: true,
    secure:
      process.env.NODE_ENV === "production" ||
      process.env.ENVIRONMENT === "prod" ||
      process.env.ENVIRONMENT === "PROD",
    expires: expires,
  })
  cookies().set({
    name: "expiresIn",
    value: expiresIn.toString(),
    sameSite: "lax",
    httpOnly: true,
    secure:
      process.env.NODE_ENV === "production" ||
      process.env.ENVIRONMENT === "prod" ||
      process.env.ENVIRONMENT === "PROD",
    expires: expires,
  })
}

export async function setTheCookieIfNotExists(
  accessToken: string,
  expiresIn: number,
) {
  // const actualCookie = cookies().get("jvse")
  await setTheCookie(accessToken, expiresIn)
}
