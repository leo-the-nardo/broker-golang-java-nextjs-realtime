"use server"
import { auth } from "../../auth"
import { ExtendedUser } from "../../next-auth"
import { useSession } from "next-auth/react"
//these can be used in : Server Components, ServerActions and API Routes(anything server side)
export async function currentUser(): Promise<ExtendedUser> {
  const session = await auth()
  if (!session || session?.user) {
    // await logout();
    console.log("auth.ts 9 , no session.user")
  }
  return session!.user
}
export async function currentRoles(): Promise<string[]> {
  const session = await auth()
  if (!session || session?.user) {
    console.log("auth.ts 16 , no session.user")
  }
  return session!.user.roles
}

export const fetchExample = async () => {
  //sleep 2000ms
  await new Promise((resolve) => setTimeout(resolve, 2000))
  const res = await fetch("https://api.github.com/users/leo-the-nardo")
  return res.json()
}

export const currentJWT = async () => {
  const session = await auth()
  if (!session || !session?.user) {
    console.log("auth.ts 29 , no session.user")
    // const session2 =
    return
  }
  const token = session!.accessToken
  const toreturn = { ...session }
  return {
    accessToken: toreturn.accessToken as string,
    expiresIn: toreturn.expiresIn as number,
  }
}
