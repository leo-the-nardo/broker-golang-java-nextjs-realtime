import { useSession } from "next-auth/react";

// just a wrapper around useSession to make it more readable
export function useCurrentRoles() {
  const session = useSession();
  return session.data?.user.roles;
}
