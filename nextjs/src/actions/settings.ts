"use server";

import * as z from "zod";

import { SettingsSchema } from "@/schemas";
import { currentUser } from "@/actions/auth";

export async function settings(values: z.infer<typeof SettingsSchema>) {
  const user = await currentUser();
  if (!user) {
    return { error: "Unauthorized!" };
  }
  return { ...user, ...values };
}
