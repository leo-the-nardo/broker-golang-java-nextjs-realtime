import { useSession } from "next-auth/react"
import { logout } from "@/actions/logout"
import { ExtendedUser } from "../../next-auth"

// just a wrapper around useSession to make it more readable
export function useCurrentUser() {
  const { data, update } = useSession()
  if (!data?.user || !data.user.name) {
    logout()
  }
  async function updateUser(datavar: Partial<ExtendedUser>) {
    await update({
      ...data,
      user: {
        ...data?.user,
        ...datavar,
      },
    })
  }
  const toreturn = {
    user: data?.user,
    updateUser,
  }
  return toreturn
}
