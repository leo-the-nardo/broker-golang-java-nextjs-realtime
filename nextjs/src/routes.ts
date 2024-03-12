/**
 * An array of routes that are accessible to public.
 * @type {string[]}
 */
export const publicRoutes = [
  "/",
  "/auth/new-verification",
  "/auth/new-password",
  "/auth/teste",
]

/**
 * These routes will redirectly logged in users to /settings.
 * @type {string[]}
 */
export const authRoutes = [
  "/auth/login",
  "/auth/register",
  "/auth/error",
  "/auth/reset",
]

/**
 * The prefix for API authentication routes.
 * Routes that start with this prefix are used for API Authentication purposes.
 * @type {string}
 */
export const apiAuthPrefix = "/api/auth"

/**
 * The default redirect path after logging in.
 * @type {string}
 */
export const DEFAULT_LOGIN_REDIRECT = "/homebroker"

export const API_URL =
  process.env.NODE_ENV === "production"
    ? "https://localhost:8080"
    : "http://localhost:8080"
