import { NextResponse } from "next/server";
import { currentRoles } from "@/actions/auth";

export async function GET() {
  const allowedRoles = ["ROLE_USER", "ROLE_ADMIN"];
  const roles = await currentRoles();
  if (!roles!.some((role) => allowedRoles.includes(role))) {
    return new NextResponse(null, { status: 403 });
  }
  return new NextResponse(null, { status: 200 });
}
