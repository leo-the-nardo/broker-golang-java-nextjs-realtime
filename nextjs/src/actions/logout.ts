//another method
"use server"
import { signOut } from "../../auth"
// this is if you want to do something in the server before signing out, etc
export async function logout() {
  // do something
  await signOut()
}
