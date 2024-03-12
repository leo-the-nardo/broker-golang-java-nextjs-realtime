"use server"
import { setTheCookie } from "@/actions/setthecookie"
import { auth } from "../../auth"
import { cookies } from "next/headers"

export async function clientSetTheCookie() {
  const actualCookie = cookies().get("jvse")
  const session = await auth()
  if (!session || session?.user) {
    await setTheCookie("BBBBBBB" + Math.random())
    return
  }
  if (!actualCookie || !actualCookie.value) {
    await setTheCookie(session.accessToken)
    if (!cookies().get("jvse") || !cookies()!.get("jvse")!.value) {
      console.log("actualCookie is null")
      await setTheCookie("AAAAAAAA")
    }
  }
  // console.log("clientSetTheCookie", accessToken)
  console.log("clientSetTheCookie done")
}
