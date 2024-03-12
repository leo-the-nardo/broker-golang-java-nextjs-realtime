import { cookies } from "next/headers"
import { currentJWT } from "@/actions/auth"

export function isHomeBrokerClosed() {
  const currentDate = new Date()
  const closeDate = new Date(
    currentDate.getFullYear(),
    currentDate.getMonth(),
    currentDate.getDate(),
    18,
    0,
    0,
  )

  return currentDate > closeDate
}

export const fetcher = (url: string) => fetch(url).then((res) => res.json())

export const fetcherServer = async (url: string, options?: RequestInit) => {
  const res = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      Cookie: cookies().toString(),
    },
    credentials: "include",
    ...options,
  })
  const json = await res.json()
  return { json: json, status: res.status, ok: res.ok }
}
