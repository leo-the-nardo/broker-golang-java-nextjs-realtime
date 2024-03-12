import { DefaultSession } from "next-auth"

export type ExtendedUser = {
  id: number
  username: string
  name: string
  roles: string[]
  emailVerified: string
  provider: string
  picture: string | null
  twoFactorEnabledAt: string | null
}
declare module "next-auth" {
  interface User {
    user: ExtendedUser
    accessToken: string
    refreshToken: string
    expiresIn: number
  }
  interface Session {
    user: ExtendedUser
    // accessToken: string;
  }
}
