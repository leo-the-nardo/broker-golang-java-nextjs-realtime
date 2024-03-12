"use server";

import { currentRoles } from "@/actions/auth";

export async function protectedAction() {
  const role = await currentRoles();
  const allowedRoles = ["ROLE_ADMIN", "ROLE_USER"];
  if (!role.some((role) => allowedRoles.includes(role))) {
    return { error: "Forbidden!" };
  }
  return { success: "Allowed!" };
}
