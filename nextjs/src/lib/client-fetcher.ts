"use server"
import { currentJWT } from "@/actions/auth"
import { setTheCookieIfNotExists } from "@/actions/setthecookie"
// import { } from "cookies-next"
import { cookies } from "next/headers"

export const fetcherClient = async (url: string, options?: RequestInit) => {
  await syncCookie()
  const cookie = cookies().get("jvse")
  const cookieString = cookie ? `${cookie.name}=${cookie.value}` : undefined
  const res = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      Cookie: cookieString!,
    },
    credentials: "include",
    ...options,
  })

  return await res.json()
}

export const postFetcher = async (url: string, options?: RequestInit) => {
  await syncCookie()
  const cookie = cookies().get("jvse")
  const cookieString = cookie ? `${cookie.name}=${cookie.value}` : undefined
  const res = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Cookie: cookieString!,
    },
    credentials: "include",
    ...options,
  })
  const response = res.ok ? await res.json() : null

  return { body: response, status: res.status, ok: res.ok }
}

export async function syncCookie() {
  const exists = cookies().get("jvse")?.value
  const expiresIn = cookies().get("expiresIn")?.value
  const isExpired = expiresIn ? Date.now() > 1000 * parseInt(expiresIn) : true
  if (isExpired || !exists) {
    console.log("FETCHER CLIENT IS EXPIRED!!")
    const jwt = await currentJWT()
    if (!jwt || !jwt.accessToken) {
      console.log("CURRENT JWT IS NULL OR ACCESS TOKEN IS NULL!!")
    } else {
      await setTheCookieIfNotExists(jwt!.accessToken, jwt.expiresIn)
    }
  }
}
