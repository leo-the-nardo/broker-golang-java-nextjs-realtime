import { auth } from "../auth"
import {
  apiAuthPrefix,
  authRoutes,
  publicRoutes,
  DEFAULT_LOGIN_REDIRECT,
} from "@/routes"

//default return = allow access
export default auth((req) => {
  const { nextUrl } = req
  const isLoggedIn = !!req.auth

  const isApiAuthRoute = nextUrl.pathname.startsWith(apiAuthPrefix)
  const isPublicRoute = publicRoutes.includes(nextUrl.pathname)
  const isAuthRoute = authRoutes.includes(nextUrl.pathname)

  if (isApiAuthRoute) {
    console.log("--------A---------")
    return
  }
  if (isAuthRoute) {
    console.log("--------B---------")
    if (isLoggedIn) {
      console.log("--------C---------")

      const defaultUrl = new URL(DEFAULT_LOGIN_REDIRECT, nextUrl)
      return Response.redirect(defaultUrl)
    }
    return
  }
  if (!isLoggedIn && !isPublicRoute) {
    console.log("--------D---------")
    const loginUrl = new URL("/auth/login", nextUrl)
    return Response.redirect(loginUrl)
  }
  console.log("--------E---------")
  return
})

// Optionally, don't invoke Middleware on some paths
export const config = {
  matcher: ["/((?!.+\\.[\\w]+$|_next).*)", "/", "/(api|trpc)(.*)"],
}
