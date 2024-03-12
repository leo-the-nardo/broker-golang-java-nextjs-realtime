import NextAuth from "next-auth"
import Credentials from "next-auth/providers/credentials"
import Github from "next-auth/providers/github"
import Google from "next-auth/providers/google"
import { LoginSchema } from "@/schemas"
import { cookies } from "next/headers"
import { EmailVerification, TwoFactor } from "./custom-auth-errors"
import { setTheCookie } from "@/actions/setthecookie"
import { fetcherServer } from "@/lib/server-fetcher"
import { API_URL } from "@/routes"
import { fetcherClient } from "@/lib/client-fetcher"

export const {
  handlers: { GET, POST },
  auth,
  signIn, //signIn only can be used in server components or server actions (i think)
  signOut,
} = NextAuth({
  pages: {
    signIn: "/auth/signin",
    error: "/auth/error", // Error code passed in query string as ?error=
  },
  providers: [
    Github({
      clientId: process.env.GITHUB_CLIENT_ID,
      clientSecret: process.env.GITHUB_CLIENT_SECRET,
    }),
    Google({
      clientId: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    }),
    Credentials({
      // this is the callback that will be called when the user tries to sign in
      //@ts-ignore

      async authorize(
        credentials,
      ): Promise<AuthorizeResult | null | undefined> {
        const validatedFields = LoginSchema.safeParse(credentials)
        if (validatedFields.success) {
          const { email, password } = validatedFields.data
          const cookie = cookies().get("jubileu")
          const body = JSON.stringify({
            username: email,
            password: password,
            token: cookie?.value,
          })
          const response = await fetch(`${API_URL}/api/auth/signin`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: body,
            credentials: "include",
          })
          if (response.status === 403 || response.status === 401) {
            console.log("403 or 401", await response.json())
            return null
          }
          if (response.status === 422) {
            throw new EmailVerification()
          }
          if (response.status === 428) {
            throw new TwoFactor()
          }
          if (!response.ok) {
            console.log("not ok", await response.json())
            return null
          }
          const { accessToken, refreshToken, expiresIn } = await response.json()
          const toReturn = { accessToken, refreshToken, expiresIn }
          await setTheCookie(accessToken, expiresIn)
          return toReturn
        }
      },
    }),
  ],
  callbacks: {
    //login flow : authorize -> signIn -> jwt -> session -> jwt -> session
    //user is the object returned from the provider authorize callback
    async signIn({ user, account, credentials }) {
      if (account?.provider === "google") {
        const response = await fetch(`${API_URL}/api/auth/signin/google`, {
          method: "POST",
          body: JSON.stringify({ idToken: account.id_token }),
          headers: {
            "Content-Type": "application/json",
          },
        })
        if (
          response.status === 403 ||
          response.status === 401 ||
          response.status === 409 ||
          !response.ok
        ) {
          console.log("Status code /signin/google: ", response.status)
          console.log("not ok", await response.json())
          return false
        }
        const { accessToken, refreshToken, expiresIn } = await response.json()
        user.accessToken = accessToken
        user.refreshToken = refreshToken
        user.expiresIn = expiresIn
      }
      if (account?.provider === "github") {
        console.log(JSON.stringify({ accessToken: account.access_token }))
        const response = await fetch(`${API_URL}/api/auth/signin/github`, {
          method: "POST",
          body: JSON.stringify({ accessToken: account.access_token }),
          headers: {
            "Content-Type": "application/json",
          },
        })
        if (
          response.status === 403 ||
          response.status === 401 ||
          response.status === 409 ||
          !response.ok
        ) {
          console.log("Status code /signin/github: ", response.status)
          console.log("not ok", await response.json())
          return false
        }
        const { accessToken, refreshToken, expiresIn, firstTimeLogin } =
          await response.json()
        user.accessToken = accessToken
        user.refreshToken = refreshToken
        user.expiresIn = expiresIn
      }
      if (user.accessToken) {
        const response = await fetch(`${API_URL}/api/profile`, {
          headers: {
            Authorization: `Bearer ${user.accessToken}`,
          },
        })
        if (
          response.status === 403 ||
          response.status === 401 ||
          !response.ok
        ) {
          console.log("not ok /api/profile: ", response.status, response)
          return false
        }
        const userOnBack = await response.json()
        console.log("userOnBack:", userOnBack)
        const emailVerified = userOnBack.emailVerified
        if (!emailVerified) {
          console.log("email not verified")
          return false
        }
        user.user = userOnBack
      }
      // true means the user is allowed to sign in
      console.log("-----------------END signIn callback-----------------")
      return true
    },
    // session callback and middleware will receive the return from this callback
    //@ts-ignore
    async jwt({ token, user, session, trigger }) {
      console.log("-----------------START jwt callback-----------------")
      if (user) {
        //Note: user is only available on sign in (first call) user is the user from signIn callback
        //if you need to add more data to the token, add it to the return or edit the token object
        const toReturn = {
          ...token,
          ...user,
        }
        console.log("jwt callback returning:", toReturn)
        console.log("-----------------END jwt callback-----------------")
        return toReturn //edit if needed
      }
      // @ts-ignore
      if (trigger === "update") {
        token = { ...token, ...session }
      }
      const isExpired = Date.now() > 1000 * token.expiresIn
      if (isExpired) {
        console.log("token expired")
        console.log("updating token on spring" + token.accessToken)
        const response = await fetch(`${API_URL}/api/auth/refreshToken`, {
          method: "POST",
          body: JSON.stringify({ refreshToken: token.refreshToken }),
          headers: {
            "Content-Type": "application/json",
          },
        })
        if (!response.ok) {
          console.log("refresh token not ok", await response.json())
          return false
        }

        const { accessToken, refreshToken, expiresIn } = await response.json()
        console.log("token refreshed on spring" + accessToken)
        token.accessToken = accessToken
        token.refreshToken = refreshToken
        token.expiresIn = expiresIn
        console.log("jwt callback returning:", token)
        console.log("-----------------END jwt callback-----------------")
        return token
      }

      console.log("token valid")
      console.log("jwt callback returning:", token)
      console.log("-----------------END jwt callback-----------------")
      return token
    },

    //server or client component,  useSession , auth() will receive the returned session object from this callback
    async session({ session, token, user }) {
      //token is the object returned from the jwt callback. If you need to add more data to the session, add it to the session.user object (maybe return a new object)
      console.log("-----------------START session callback-----------------")
      const cleanedToken = { ...token }
      delete cleanedToken.refreshToken
      delete cleanedToken.twoFactorConfirmations
      const toReturn = { ...session, ...cleanedToken }
      console.log("session callback returning:", toReturn)
      console.log(
        "--------------------END session callback-------------------------",
      )
      return toReturn
    },
  },
  session: {
    strategy: "jwt",
  },
  // ...authConfig,
})

interface AuthorizeResult {
  accessToken: string
  refreshToken: string
  expiresIn: number
}
